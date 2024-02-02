package com.brodygaudel.securityservice.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Configuration class representing security parameters for authentication.
 * This class is annotated with {@code @Component} to indicate that it is a Spring-managed component.
 * The security parameters include a secret key and an expiration time for authentication tokens.
 *
 * @since 2024
 * @author Brody Gaudel MOUNANGA BOUKA
 * @version 1.0
 */
@NoArgsConstructor
@Getter
@Component
public class SecurityParameters {

    /**
     * The secret key used for generating and validating authentication tokens.
     */
    @Value("${secret}")
    private String secret;

    /**
     * The expiration time (in milliseconds) for authentication tokens.
     */
    @Value("${expired-time}")
    private Long expiredTime;

    /**
     * The allowed origins for Cross-Origin Resource Sharing (CORS) configuration.
     */
    @Value("#{'${allowed-origins}'.split(',')}")
    private List<String> allowedOrigins;
}

