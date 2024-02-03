package com.brodygaudel.securityservice.restcontroller;

import com.brodygaudel.securityservice.dto.UserRequestDTO;
import com.brodygaudel.securityservice.dto.UserResponseDTO;
import com.brodygaudel.securityservice.dto.UserRoleRequestDTO;
import com.brodygaudel.securityservice.dto.UsersPageResponseDTO;
import com.brodygaudel.securityservice.exceptions.ItemAlreadyExistException;
import com.brodygaudel.securityservice.exceptions.RoleNotFoundException;
import com.brodygaudel.securityservice.exceptions.UserNotFoundException;
import com.brodygaudel.securityservice.service.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for handling user-related operations.
 * Exposes endpoints for creating, updating, deleting, and retrieving user information.
 *
 * @since 2024
 * @author Brody Gaudel MOUNANGA BOUKA
 * @version 1.0
 */
@RestController
@RequestMapping("/users")
public class UserRestController {

    private final UserService userService;

    /**
     * Constructs a new instance of {@code UserRestController}.
     *
     * @param userService The user service to be used for handling user-related operations.
     */
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Endpoint for creating a new user.
     *
     * @param userRequestDTO The request payload containing user information.
     * @return The response DTO containing information about the created user.
     * @throws ItemAlreadyExistException If the user with the specified information already exists.
     */
    @PostMapping("/create")
    public UserResponseDTO save(@RequestBody UserRequestDTO userRequestDTO) throws ItemAlreadyExistException{
        return userService.save(userRequestDTO);
    }

    /**
     * Endpoint for updating an existing user.
     *
     * @param id             The ID of the user to be updated.
     * @param userRequestDTO The request payload containing updated user information.
     * @return The response DTO containing information about the updated user.
     * @throws UserNotFoundException     If the user with the specified ID is not found.
     * @throws ItemAlreadyExistException If the updated user already exists.
     */
    @PutMapping("/update/{id}")
    public UserResponseDTO update(@PathVariable String id, @RequestBody UserRequestDTO userRequestDTO) throws UserNotFoundException, ItemAlreadyExistException{
        return userService.update(id, userRequestDTO);
    }

    /**
     * Endpoint for adding a role to a user.
     *
     * @param userRoleRequestDTO The request payload containing user and role information for the association.
     * @return {@code true} if the role is successfully added to the user, {@code false} otherwise.
     * @throws UserNotFoundException If the user specified in the DTO is not found.
     * @throws RoleNotFoundException If the role specified in the DTO is not found.
     */
    @PutMapping("/add-role")
    public Boolean addRoleToUser(@RequestBody UserRoleRequestDTO userRoleRequestDTO) throws UserNotFoundException, RoleNotFoundException {
        return userService.addRoleToUser(userRoleRequestDTO);
    }

    /**
     * Endpoint for removing a role from a user.
     *
     * @param userRoleRequestDTO The request payload containing user and role information for the removal.
     * @return {@code true} if the role is successfully removed from the user, {@code false} otherwise.
     * @throws UserNotFoundException If the user specified in the DTO is not found.
     * @throws RoleNotFoundException If the role specified in the DTO is not found.
     */
    @PutMapping("/remove-role")
    public Boolean removeRoleToUser(@RequestBody UserRoleRequestDTO userRoleRequestDTO) throws UserNotFoundException, RoleNotFoundException {
        return userService.removeRoleToUser(userRoleRequestDTO);
    }

    /**
     * Endpoint for retrieving a user by the specified ID.
     *
     * @param id The ID of the user to be retrieved.
     * @return The response DTO containing details of the found user.
     * @throws UserNotFoundException If the user with the specified ID is not found.
     */
    @GetMapping("/get/{id}")
    public UserResponseDTO findById(@PathVariable String id) throws UserNotFoundException {
        return userService.findById(id);
    }

    /**
     * Endpoint for retrieving all users in the system.
     *
     * @return A list of response DTOs containing details of all users.
     */
    @GetMapping("/all")
    public List<UserResponseDTO> findAll() {
        return userService.findAll();
    }

    /**
     * Endpoint for retrieving a page of users with the specified page number and size.
     *
     * @param page The page number (zero-based).
     * @param size The size of the page.
     * @return A response DTO containing a page of users.
     */
    @GetMapping("/list/{page}/{size}")
    public UsersPageResponseDTO findAll(@PathVariable(name = "page") int page, @PathVariable(name = "size") int size) {
        return userService.findAll(page, size);
    }

    /**
     * Endpoint for deleting a user by the specified ID.
     *
     * @param id The ID of the user to be deleted.
     */
    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable String id) {
        userService.deleteById(id);
    }

    /**
     * Endpoint for deleting all users in the system.
     */
    @DeleteMapping("/delete-all")
    public void deleteAll() {
        userService.deleteAll();
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
