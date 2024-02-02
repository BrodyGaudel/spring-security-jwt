package com.brodygaudel.securityservice.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * A data transfer object (DTO) representing a login request.
 * This record encapsulates the information required for user authentication.
 * @since 2024
 * @author Brody Gaudel MOUNANGA BOUKA
 * @version 1.0
 */
public record LoginRequestDTO(@NotBlank String username,@NotBlank String password) {
}
