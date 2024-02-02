package com.brodygaudel.securityservice.security;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration class for defining Spring Security beans.
 * This class provides beans for customizing authentication mechanisms, password encoding, and
 * configuring the authentication manager.
 *
 * @since 2024
 * @author Brody Gaudel MOUNANGA BOUKA
 * @version 1.0
 */
@Configuration
public class SecurityBeansConfiguration {

    private final UserDetailsService userDetailsService;

    /**
     * Constructs a new SecurityBeansConfiguration with the specified UserDetailsService.
     *
     * @param userDetailsService The UserDetailsService used for authentication.
     */
    public SecurityBeansConfiguration(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Provides an AuthenticationProvider bean for custom authentication.
     *
     * @return The configured AuthenticationProvider bean.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    /**
     * Provides a PasswordEncoder bean for password hashing.
     *
     * @return The configured PasswordEncoder bean.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Provides an AuthenticationManager bean using the provided AuthenticationConfiguration.
     *
     * @param config The AuthenticationConfiguration used for configuring the AuthenticationManager.
     * @return The configured AuthenticationManager bean.
     * @throws Exception If an error occurs during the configuration of the AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(@NotNull AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}

