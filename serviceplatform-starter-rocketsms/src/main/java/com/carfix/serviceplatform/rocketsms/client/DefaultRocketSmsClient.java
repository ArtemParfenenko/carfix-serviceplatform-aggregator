package com.carfix.serviceplatform.rocketsms.client;

import com.carfix.serviceplatform.core.exception.ServicePlatformException;
import com.carfix.serviceplatform.rocketsms.util.RocketSmsPropertiesProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultRocketSmsClient implements RocketSmsClient {

    private static final Logger LOG = LoggerFactory.getLogger(RocketSmsClient.class);

    private static final String PHONE_REGEXP = "375(29|44|33|25)[1-9][0-9]{2}[0-9]{2}[0-9]{2}";

    private static final String ROCKET_SMS_URN = "http://api.rocketsms.by/simple";

    private static final String SEND_SMS_URL = "/send?username={username}&password={password}&phone={phone}&text={text}";

    private final RestTemplate restTemplate;
    private final RocketSmsPropertiesProvider propertiesProvider;
    private final ObjectMapper objectMapper;

    public DefaultRocketSmsClient(RestTemplate restTemplate, RocketSmsPropertiesProvider propertiesProvider) {
        this.restTemplate = restTemplate;
        this.propertiesProvider = propertiesProvider;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void sendSMS(String phone, String message) throws ServicePlatformException {
        validatePhone(phone);

        UriComponents uri;
        try {
            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(ROCKET_SMS_URN + SEND_SMS_URL);

            Optional.ofNullable(propertiesProvider.getSender())
                    .ifPresent(sender -> uriComponentsBuilder.queryParam("sender", sender));

            uri = uriComponentsBuilder.buildAndExpand(propertiesProvider.getUsername(), getMD5(propertiesProvider.getPassword()),
                    phone, message);
        } catch (NoSuchAlgorithmException e) {
            throw new ServicePlatformException(e, "Internal server error. Please, contact with administrator", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ResponseEntity<String> response;

        try {
            response = restTemplate.postForEntity(uri.toUriString(), null, String.class);
        } catch (RuntimeException e) {
            LOG.warn("Fail to process response from RocketSms service");
            return;
        }

        try {
            validateResponse(response);
        } catch (ServicePlatformException e) {
            throw e;
        } catch (Exception e) {
            LOG.warn("Fail to validate response from RocketSms service");
        }
    }

    private String getMD5(String text) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");

        messageDigest.update(text.getBytes());
        byte[] digest = messageDigest.digest();

        return DatatypeConverter.printHexBinary(digest).toUpperCase();
    }

    private void validatePhone(String phone) {
        Pattern pattern = Pattern.compile(PHONE_REGEXP);

        Matcher matcher = pattern.matcher(phone);

        if (!matcher.matches()) {
            throw new ServicePlatformException("Invalid phone", HttpStatus.BAD_REQUEST);
        }
    }

    private void validateResponse(ResponseEntity<String> response) throws IOException {
        final String error = "error";
        final String errorMessage = "Something went wrong. Please, contact administrator";

        JsonNode body = objectMapper.readValue(response.getBody(), JsonNode.class);

        if (body == null) {
            throw new ServicePlatformException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (!HttpStatus.OK.equals(response.getStatusCode())) {
            if (body.has(error)) {
                throw new ServicePlatformException(body.findValue(error).asText(), response.getStatusCode());
            } else {
                throw new ServicePlatformException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }
}
