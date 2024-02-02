package com.brodygaudel.securityservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Filter responsible for processing JWT-based authorization in Spring Security.
 * This filter extracts and verifies JWT tokens from the "Authorization" header, sets up
 * the authentication context, and delegates to the next filter in the chain.
 *
 * @since 2024
 * @author Brody Gaudel MOUNANGA BOUKA
 * @version 1.0
 */
@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private static final String BEARER = "Bearer ";
    private static final String ROLES = "roles";
    private static final String AUTHORIZATION = "Authorization";
    private final SecurityParameters securityParameters;

    /**
     * Constructs a new JWTAuthorizationFilter with the specified security parameters.
     *
     * @param securityParameters The security parameters used for JWT verification.
     */
    public JWTAuthorizationFilter(SecurityParameters securityParameters) {
        this.securityParameters = securityParameters;
    }

    /**
     * Performs the JWT-based authorization filter logic.
     *
     * @param request The HTTP request.
     * @param response The HTTP response.
     * @param filterChain The filter chain for additional filters.
     * @throws ServletException If an error occurs during the filter processing.
     * @throws IOException If an I/O error occurs during the filter processing.
     */
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        String jwt = request.getHeader(AUTHORIZATION);
        if (jwt == null || !jwt.startsWith(BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }

        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(securityParameters.getSecret())).build();
        jwt = jwt.substring(7);
        DecodedJWT decodedJWT = verifier.verify(jwt);

        String username = decodedJWT.getSubject();
        List<String> roles = decodedJWT.getClaims().get(ROLES).asList(String.class);
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        for (String r : roles) {
            grantedAuthorities.add(new SimpleGrantedAuthority(r));
        }

        UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(username, null, grantedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(user);
        filterChain.doFilter(request, response);
    }
}
