package com.carfix.serviceplatform.core.exception;

import org.springframework.http.HttpStatus;

public class ServicePlatformException extends RuntimeException {

    private static final String ERROR_MESSAGE = "Failed to get resource. Access denied.";

    private HttpStatus httpStatus;

    public ServicePlatformException() {
        super(ERROR_MESSAGE);
        setHttpStatus(HttpStatus.FORBIDDEN);
    }

    public ServicePlatformException(String message, HttpStatus httpStatus) {
        super(message);
        setHttpStatus(httpStatus);
    }

    public ServicePlatformException(Throwable cause, String message, HttpStatus httpStatus) {
        super(message, cause);
        setHttpStatus(httpStatus);
    }

    public ServicePlatformException(Throwable cause) {
        super(ERROR_MESSAGE, cause);
        setHttpStatus(HttpStatus.FORBIDDEN);
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    private void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
