package com.brodygaudel.securityservice.dto;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * A data transfer object (DTO) representing a user response.
 * This record encapsulates the information returned when querying user details,
 * including the user's ID, username, email, enabled status, and a set of roles.
 *
 * @since 2024
 * @author Brody Gaudel MOUNANGA BOUKA
 * @version 1.0
 */
public record UserResponseDTO(String id, String username, String email, Boolean enabled, Set<String> roles, LocalDateTime creation, LocalDateTime lastUpdate) {
}
