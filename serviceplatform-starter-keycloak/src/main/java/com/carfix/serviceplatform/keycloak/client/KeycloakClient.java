package com.carfix.serviceplatform.keycloak.client;

import com.google.common.collect.ImmutableMap;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class KeycloakClient {

    private final Keycloak keycloak;
    private final KeycloakSpringBootProperties keycloakSpringBootProperties;

    public KeycloakClient(Keycloak keycloak, KeycloakSpringBootProperties keycloakSpringBootProperties) {
        this.keycloak = keycloak;
        this.keycloakSpringBootProperties = keycloakSpringBootProperties;
    }

    public String registerUser(UserRepresentation user, CredentialRepresentation credentials, String... roles) {
        List<String> roleList = Arrays.asList(roles);
        user.setAttributes(ImmutableMap.of("serviceplatform_user_role", roleList));
        user.setCredentials(Collections.singletonList(credentials));

        RealmResource realm = getRealm();
        UsersResource usersResource = realm.users();
        Response response = usersResource.create(user);
        String userId = CreatedResponseUtil.getCreatedId(response);

        RolesResource rolesResource = realm.roles();
        List<RoleRepresentation> roleRepresentations = roleList.stream()
                .map(rolesResource::get)
                .map(RoleResource::toRepresentation)
                .collect(Collectors.toList());

        usersResource.get(userId)
                .roles()
                .realmLevel()
                .add(roleRepresentations);

        return userId;
    }

    public void updateUser(String id, Consumer<UserRepresentation> updateFunction) {
        RealmResource realm = getRealm();
        UserResource userResource = realm.users().get(id);
        UserRepresentation userRepresentation = userResource.toRepresentation();
        updateFunction.accept(userRepresentation);
        userResource.update(userRepresentation);
    }

    public void deleteUser(String id) {
        getRealm().users().delete(id);
    }

    public RealmResource getRealm() {
        assert Objects.nonNull(keycloakSpringBootProperties.getRealm());
        return keycloak.realm(keycloakSpringBootProperties.getRealm());
    }
}
