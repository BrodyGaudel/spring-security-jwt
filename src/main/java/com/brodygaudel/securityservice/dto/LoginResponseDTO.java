package com.brodygaudel.securityservice.dto;

import java.util.Set;

/**
 * A data transfer object (DTO) representing a login response.
 * This record encapsulates the information returned after a successful login, including
 * the username, JWT (JSON Web Token), and a set of roles associated with the user.
 * @since 2024
 * @author Brody Gaudel MOUNANGA BOUKA
 * @version 1.0
 */
public record LoginResponseDTO(String username, String jwt, Set<String> roles) {
}
