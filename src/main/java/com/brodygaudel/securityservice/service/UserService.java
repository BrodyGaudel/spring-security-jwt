package com.brodygaudel.securityservice.service;

import com.brodygaudel.securityservice.dto.UserRequestDTO;
import com.brodygaudel.securityservice.dto.UserResponseDTO;
import com.brodygaudel.securityservice.dto.UserRoleRequestDTO;
import com.brodygaudel.securityservice.dto.UsersPageResponseDTO;
import com.brodygaudel.securityservice.exceptions.ItemAlreadyExistException;
import com.brodygaudel.securityservice.exceptions.RoleNotFoundException;
import com.brodygaudel.securityservice.exceptions.UserNotFoundException;

import java.util.List;

/**
 * Service interface for managing user-related operations.
 * This interface defines methods for saving, updating, finding, and deleting users.
 *
 * @since 2024
 * @author Brody Gaudel MOUNANGA BOUKA
 * @version 1.0
 */
public interface UserService {


    /**
     * Saves a new user based on the provided {@link UserRequestDTO}.
     *
     * @param userRequestDTO The DTO containing user information to be saved.
     * @return The response DTO containing details of the saved user.
     * @throws ItemAlreadyExistException If an attempt to save the user fails because the user already exists.
     */
    UserResponseDTO save(UserRequestDTO userRequestDTO) throws ItemAlreadyExistException;

    /**
     * Updates an existing user with the specified ID using the information in the {@link UserRequestDTO}.
     *
     * @param id              The ID of the user to be updated.
     * @param userRequestDTO  The DTO containing updated user information.
     * @return The response DTO containing details of the updated user.
     * @throws UserNotFoundException    If the user with the specified ID is not found.
     * @throws ItemAlreadyExistException If an attempt to update the user fails because the updated user already exists.
     */
    UserResponseDTO update(String id, UserRequestDTO userRequestDTO) throws UserNotFoundException, ItemAlreadyExistException;


    /**
     * Finds a user by the specified ID.
     *
     * @param id The ID of the user to be found.
     * @return The response DTO containing details of the found user.
     * @throws UserNotFoundException If the user with the specified ID is not found.
     */
    UserResponseDTO findById(String id) throws UserNotFoundException;

    /**
     * Finds all users in the system.
     *
     * @return A list of response DTOs containing details of all users.
     */
    List<UserResponseDTO> findAll();

    /**
     * Finds a page of users with the specified page number and page size.
     *
     * @param page The page number (zero-based).
     * @param size The size of the page.
     * @return A response DTO containing a page of users.
     */
    UsersPageResponseDTO findAll(int page, int size);

    /**
     * Deletes a user by the specified ID.
     *
     * @param id The ID of the user to be deleted.
     */
    void deleteById(String id);

    /**
     * Deletes all users in the system.
     */
    void deleteAll();

    /**
     * Adds a role to a user based on the provided {@link UserRoleRequestDTO}.
     *
     * @param userRoleRequestDTO The DTO containing user and role information for the association.
     * @return {@code true} if the role is successfully added to the user, {@code false} otherwise.
     * @throws UserNotFoundException If the user specified in the DTO is not found.
     * @throws RoleNotFoundException If the role specified in the DTO is not found.
     */
    Boolean addRoleToUser(UserRoleRequestDTO userRoleRequestDTO) throws UserNotFoundException, RoleNotFoundException;

    /**
     * Removes a role from a user based on the provided {@link UserRoleRequestDTO}.
     *
     * @param userRoleRequestDTO The DTO containing user and role information for the removal.
     * @return {@code true} if the role is successfully removed from the user, {@code false} otherwise.
     * @throws UserNotFoundException If the user specified in the DTO is not found.
     * @throws RoleNotFoundException If the role specified in the DTO is not found.
     */
    Boolean removeRoleToUser(UserRoleRequestDTO userRoleRequestDTO) throws UserNotFoundException, RoleNotFoundException;


}

