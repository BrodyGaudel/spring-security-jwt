package com.brodygaudel.securityservice.dto;

/**
 * A data transfer object (DTO) representing a request to assign a role to a user.
 * This record encapsulates the information required to associate a role with a specific user,
 * including the user's username and the name of the role to be assigned.
 *
 * @since 2024
 * @author Brody Gaudel MOUNANGA BOUKA
 * @version 1.0
 */
public record UserRoleRequestDTO(String username, String roleName) {
}
