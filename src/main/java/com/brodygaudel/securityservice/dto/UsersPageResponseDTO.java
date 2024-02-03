package com.brodygaudel.securityservice.dto;

import java.util.List;

/**
 * A data transfer object (DTO) representing a page of users in the system.
 * This record encapsulates information about the total number of pages, the current page,
 * the size of the page, and a list of user response DTOs.
 *
 * @since 2024
 * @author Brody Gaudel MOUNANGA BOUKA
 * @version 1.0
 */
public record UsersPageResponseDTO(int totalPage, int page, int size, List<UserResponseDTO> users) {
}

