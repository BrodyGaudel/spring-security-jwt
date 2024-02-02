package com.brodygaudel.securityservice.service.implementation;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.brodygaudel.securityservice.dto.LoginRequestDTO;
import com.brodygaudel.securityservice.dto.LoginResponseDTO;
import com.brodygaudel.securityservice.entity.Role;
import com.brodygaudel.securityservice.entity.User;
import com.brodygaudel.securityservice.repository.UserRepository;
import com.brodygaudel.securityservice.security.SecurityParameters;
import com.brodygaudel.securityservice.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service implementation for handling user authentication operations.
 * This class provides the implementation for the AuthenticationService interface,
 * performing user login/authentication based on the provided LoginRequestDTO.
 *
 * @since 2024
 * @author Brody Gaudel MOUNANGA BOUKA
 * @version 1.0
 */
@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final SecurityParameters securityParameters;

    /**
     * Constructs a new AuthenticationServiceImpl with the specified dependencies.
     *
     * @param authenticationManager The AuthenticationManager for user authentication.
     * @param userRepository       The UserRepository for accessing user data.
     * @param securityParameters   The SecurityParameters containing security-related configurations.
     */
    public AuthenticationServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository, SecurityParameters securityParameters) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.securityParameters = securityParameters;
    }


    /**
     * Performs user login/authentication based on the provided LoginRequestDTO.
     * This method attempts to authenticate a user using the information provided in the LoginRequestDTO.
     *
     * @param loginRequestDTO The LoginRequestDTO containing user credentials for authentication.
     * @return The result of the login operation encapsulated in a LoginResponseDTO.
     *         If the login is successful, the response may include user details and a JWT token.
     *         If the login fails, appropriate error details may be included in the response.
     */
    @Override
    public LoginResponseDTO login(@NotNull LoginRequestDTO loginRequestDTO) {
        log.info("In login() :");
        //checks if he tries to authenticate with his email instead of his username
        User user = userRepository.findByEmail(loginRequestDTO.username());
        if(user == null){
            //authentication by username
            return authentication(loginRequestDTO.username(), loginRequestDTO.password());
        }
        //authentication by email
        return authentication(user.getUsername(), loginRequestDTO.password());
    }


    /**
     * Performs user authentication based on the provided username and password.
     *
     * @param username The username for authentication.
     * @param password The password for authentication.
     * @return The result of the authentication operation encapsulated in a LoginResponseDTO.
     */
    @Contract("_, _ -> new")
    private @NotNull LoginResponseDTO authentication(String username, String password){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        User user = userRepository.findByUsername(username);
        List<String> roles = new ArrayList<>();
        user.getRoles().forEach(
                role -> roles.add(role.getName())
        );

        String jwt = JWT.create().withSubject(user.getUsername())
                .withArrayClaim("roles", roles.toArray(new String[0]))
                .withExpiresAt(new Date(System.currentTimeMillis()+ securityParameters.getExpiredTime()))
                .sign(Algorithm.HMAC256(securityParameters.getSecret()));
        log.info("user authenticated");
        return new LoginResponseDTO(user.getUsername(), jwt, rolesToStrings(user.getRoles()));
    }

    /**
     * Converts a list of Role objects to a set of their corresponding names.
     *
     * @param roles The list of Role objects to convert.
     * @return A set of strings representing the names of the roles.
     *         Returns an empty set if the input list is null or empty.
     */
    private Set<String> rolesToStrings(List<Role> roles){
        if(roles == null || roles.isEmpty()){
            return new HashSet<>();
        }
        return roles.stream().map(Role::getName).collect(Collectors.toSet());
    }
}
