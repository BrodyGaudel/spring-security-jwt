package com.brodygaudel.securityservice.restcontroller;

import com.brodygaudel.securityservice.dto.LoginRequestDTO;
import com.brodygaudel.securityservice.dto.LoginResponseDTO;
import com.brodygaudel.securityservice.service.AuthenticationService;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Rest controller for handling authentication-related HTTP requests.
 * This controller exposes endpoints for authentication operations, such as user login.
 *
 * @since 2024
 * @author Brody Gaudel MOUNANGA BOUKA
 * @version 1.0
 */
@RestController
@RequestMapping("/authentication")
public class AuthenticationRestController {

    private final AuthenticationService authenticationService;

    /**
     * Constructs a new AuthenticationRestController with the specified AuthenticationService dependency.
     *
     * @param authenticationService The AuthenticationService used for handling authentication operations.
     */
    public AuthenticationRestController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Handles HTTP POST requests for user login/authentication.
     *
     * @param loginRequestDTO The LoginRequestDTO containing user credentials for authentication.
     * @return The result of the login operation encapsulated in a LoginResponseDTO.
     *         If the login is successful, the response may include user details and a JWT token.
     *         If the login fails, appropriate error details may be included in the response.
     */
    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO loginRequestDTO){
        return authenticationService.login(loginRequestDTO);
    }

    /**
     * Exception handler for handling exceptions thrown within this controller.
     *
     * @param exception The exception to handle.
     * @return A ResponseEntity with an error message and HTTP status code.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exceptionHandler(@NotNull Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

