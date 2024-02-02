package com.brodygaudel.securityservice.dto;

/**
 * A data transfer object (DTO) representing a user request.
 * This record encapsulates the information required for creating a new user,
 * including the user's ID, username, email, and password.
 * @since 2024
 * @author Brody Gaudel MOUNANGA BOUKA
 * @version 1.0
 */
public record UserRequestDTO(String id, String username, String email, String password) {
}
