package com.brodygaudel.securityservice.service;

import com.brodygaudel.securityservice.dto.LoginRequestDTO;
import com.brodygaudel.securityservice.dto.LoginResponseDTO;

/**
 * Service interface for handling user authentication operations.
 * This interface defines methods related to user login/authentication.
 *
 * @since 2024
 * @author Brody Gaudel MOUNANGA BOUKA
 * @version 1.0
 */
public interface AuthenticationService {

    /**
     * Performs user login/authentication based on the provided LoginRequestDTO.
     * This method attempts to authenticate a user using the information provided in the LoginRequestDTO.
     *
     * @param loginRequestDTO The LoginRequestDTO containing user credentials for authentication.
     * @return The result of the login operation encapsulated in a LoginResponseDTO.
     *         If the login is successful, the response may include user details and a JWT token.
     *         If the login fails, appropriate error details may be included in the response.
     */
    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);

}

