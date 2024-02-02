package com.brodygaudel.securityservice.security;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

/**
 * Configuration class for defining Spring Security settings.
 * This class enables web security, configures CORS (Cross-Origin Resource Sharing),
 * sets up session management, and defines access rules for different endpoints.
 *
 * @since 2024
 * @author Brody Gaudel MOUNANGA BOUKA
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final JWTAuthorizationFilter authorizationFilter;
    private final SecurityParameters securityParameters;

    /**
     * Constructs a new SecurityConfiguration with the specified JWTAuthorizationFilter and SecurityParameters.
     *
     * @param authorizationFilter The JWTAuthorizationFilter used for JWT-based authorization.
     * @param securityParameters  The SecurityParameters containing security-related configurations.
     */
    public SecurityConfiguration(JWTAuthorizationFilter authorizationFilter, SecurityParameters securityParameters) {
        this.authorizationFilter = authorizationFilter;
        this.securityParameters = securityParameters;
    }

    /**
     * Configures the default SecurityFilterChain for the application.
     *
     * @param http The HttpSecurity to configure.
     * @return The configured SecurityFilterChain.
     * @throws Exception If an error occurs during the configuration.
     */
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(@NotNull HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> cors.configurationSource(request -> corsConfiguration()))
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/list/**").hasAnyAuthority(StaticParameters.SUPER_ADMIN, StaticParameters.ADMIN, StaticParameters.USER)
                        .requestMatchers("/get/**").hasAnyAuthority(StaticParameters.SUPER_ADMIN, StaticParameters.ADMIN, StaticParameters.USER)
                        .requestMatchers("/find/**").hasAnyAuthority(StaticParameters.SUPER_ADMIN, StaticParameters.ADMIN, StaticParameters.USER)
                        .requestMatchers("/all").hasAnyAuthority(StaticParameters.SUPER_ADMIN, StaticParameters.ADMIN, StaticParameters.USER)
                        .requestMatchers("/save/**").hasAnyAuthority(StaticParameters.SUPER_ADMIN, StaticParameters.ADMIN)
                        .requestMatchers("/update/**").hasAnyAuthority(StaticParameters.SUPER_ADMIN, StaticParameters.ADMIN)
                        .requestMatchers("/add-role/**").hasAuthority(StaticParameters.SUPER_ADMIN)
                        .requestMatchers("/remove-role/**").hasAuthority(StaticParameters.SUPER_ADMIN)
                        .requestMatchers("/delete/**").hasAnyAuthority(StaticParameters.SUPER_ADMIN)
                        .requestMatchers("/clear/**").hasAuthority(StaticParameters.SUPER_ADMIN)
                        .requestMatchers("/login").permitAll())
                .addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Builds and returns a CorsConfiguration based on the security parameters.
     *
     * @return The configured CorsConfiguration.
     */
    private @NotNull CorsConfiguration corsConfiguration(){
        CorsConfiguration cors = new CorsConfiguration();
        cors.setAllowedOrigins(securityParameters.getAllowedOrigins());
        cors.setAllowedMethods(StaticParameters.METHODS);
        cors.setAllowedHeaders(StaticParameters.HEADERS);
        cors.setExposedHeaders(StaticParameters.HEADERS);
        cors.setMaxAge(StaticParameters.MAX_AGE);
        return cors;
    }
}
