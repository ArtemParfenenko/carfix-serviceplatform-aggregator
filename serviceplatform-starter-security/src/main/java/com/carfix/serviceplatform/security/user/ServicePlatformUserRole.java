package com.carfix.serviceplatform.security.user;

public interface ServicePlatformUserRole {

    /**
     * ADMIN is a role of a user who is able to perform all of the operations in the system and it's rights can't be limited by @AllowFor annotation
     */
    String ADMIN = "ADMIN";

    /**
     * INTERNAL is a role which can be used for communication between ServicePlatform microservices.
     * Every authenticated user can be switched to this role using
     * @see com.carfix.serviceplatform.security.util.RoleSwitcher
     */
    String INTERNAL = "INTERNAL";
}
