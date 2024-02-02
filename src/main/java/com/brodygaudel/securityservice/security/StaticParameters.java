package com.brodygaudel.securityservice.security;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.util.List;

/**
 * Utility class containing static parameters used in the application.
 * @since 2024
 * @author Brody Gaudel MOUNANGA BOUKA
 * @version 1.0
 */
public class StaticParameters {
    /**
     * Represents the role for a super admin user.
     */
    public static final String SUPER_ADMIN = "SUPER_ADMIN";

    /**
     * Represents the role for an admin user.
     */
    public static final String ADMIN = "ADMIN";

    /**
     * Represents the role for a regular user.
     */
    public static final String USER = "USER";

    /**
     * Represents the maximum age (in seconds) for certain functionalities.
     */
    public static final Long MAX_AGE = 3600L;

    /**
     * Represents a list of HTTP methods allowed in the application.
     */
    public static final List<String> METHODS = List.of(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PUT.name(), HttpMethod.DELETE.name());

    /**
     * Represents a list of HTTP headers used in the application.
     */
    public static final List<String> HEADERS = List.of(HttpHeaders.CONTENT_TYPE, HttpHeaders.AUTHORIZATION);

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private StaticParameters(){
        super();
    }
}
