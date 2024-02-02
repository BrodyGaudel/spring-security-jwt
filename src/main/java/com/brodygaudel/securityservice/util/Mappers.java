package com.brodygaudel.securityservice.util;

import com.brodygaudel.securityservice.dto.UserRequestDTO;
import com.brodygaudel.securityservice.dto.UserResponseDTO;
import com.brodygaudel.securityservice.entity.User;

import java.util.List;

/**
 * Interface defining methods for mapping between DTOs and entities.
 * This interface provides methods for converting between different representations of user data.
 *
 * @since 2024
 * @author Brody Gaudel MOUNANGA BOUKA
 * @version 1.0
 */
public interface Mappers {

    /**
     * Converts a UserRequestDTO to a User entity.
     *
     * @param userRequestDTO The UserRequestDTO to convert.
     * @return The corresponding User entity.
     */
    User fromUserRequestDTO(UserRequestDTO userRequestDTO);

    /**
     * Converts a User entity to a UserResponseDTO.
     *
     * @param user The User entity to convert.
     * @return The corresponding UserResponseDTO.
     */
    UserResponseDTO fromUser(User user);

    /**
     * Converts a list of User entities to a list of UserResponseDTOs.
     *
     * @param users The list of User entities to convert.
     * @return The corresponding list of UserResponseDTOs.
     */
    List<UserResponseDTO> fromListOfUsers(List<User> users);
}

