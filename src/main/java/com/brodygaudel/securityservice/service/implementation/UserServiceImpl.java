package com.brodygaudel.securityservice.service.implementation;

import com.brodygaudel.securityservice.dto.UserRequestDTO;
import com.brodygaudel.securityservice.dto.UserResponseDTO;
import com.brodygaudel.securityservice.dto.UserRoleRequestDTO;
import com.brodygaudel.securityservice.dto.UsersPageResponseDTO;
import com.brodygaudel.securityservice.entity.Role;
import com.brodygaudel.securityservice.entity.User;
import com.brodygaudel.securityservice.exceptions.ItemAlreadyExistException;
import com.brodygaudel.securityservice.exceptions.RoleNotFoundException;
import com.brodygaudel.securityservice.exceptions.UserNotFoundException;
import com.brodygaudel.securityservice.repository.RoleRepository;
import com.brodygaudel.securityservice.repository.UserRepository;
import com.brodygaudel.securityservice.service.UserService;
import com.brodygaudel.securityservice.util.Mappers;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Service implementation for managing user-related operations.
 *
 * @since 2024
 * @author Brody Gaudel MOUNANGA BOUKA
 * @version 1.0
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final Mappers mappers;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs a new UserServiceImpl with the specified dependencies.
     *
     * @param userRepository    The UserRepository for accessing user data.
     * @param roleRepository    The RoleRepository for accessing role data.
     * @param mappers           The Mappers utility for mapping between DTOs and entities.
     * @param passwordEncoder   The PasswordEncoder for encoding user passwords.
     */
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, Mappers mappers, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.mappers = mappers;
        this.passwordEncoder = passwordEncoder;
    }


    /**
     * Saves a new user based on the provided {@link UserRequestDTO}.
     *
     * @param userRequestDTO The DTO containing user information to be saved.
     * @return The response DTO containing details of the saved user.
     * @throws ItemAlreadyExistException If an attempt to save the user fails because the user already exists.
     */
    @Override
    public UserResponseDTO save(@NotNull UserRequestDTO userRequestDTO) throws ItemAlreadyExistException {
        log.info("In save()");
        checkIfUsernameOrEmailAlreadyExists(userRequestDTO.username(), userRequestDTO.email());
        User user = mappers.fromUserRequestDTO(userRequestDTO);
        user.setEnabled(true);
        user.setRoles(Collections.singletonList(getUserRole()));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreation(LocalDateTime.now());
        user.setLastUpdate(null);

        User userSaved = userRepository.save(user);
        log.info("user saved");
        return mappers.fromUser(userSaved);
    }

    /**
     * Updates an existing user with the specified ID using the information in the {@link UserRequestDTO}.
     *
     * @param id             The ID of the user to be updated.
     * @param userRequestDTO The DTO containing updated user information.
     * @return The response DTO containing details of the updated user.
     * @throws UserNotFoundException     If the user with the specified ID is not found.
     * @throws ItemAlreadyExistException If an attempt to update the user fails because the updated user already exists.
     */
    @Override
    public UserResponseDTO update(String id, @NotNull UserRequestDTO userRequestDTO) throws UserNotFoundException, ItemAlreadyExistException {
        log.info("In update()");
        User user = userRepository.findById(id).orElseThrow( () -> new UserNotFoundException("user with id '"+id+"' not found"));
        checkingBeforeUpdate(user, userRequestDTO);
        user.setPassword(passwordEncoder.encode(userRequestDTO.password()));
        user.setEmail(userRequestDTO.email());
        user.setUsername(userRequestDTO.username());
        user.setLastUpdate(LocalDateTime.now());

        User userUpdated = userRepository.save(user);
        log.info("user updated");
        return mappers.fromUser(userUpdated);
    }

    /**
     * Finds a user by the specified ID.
     *
     * @param id The ID of the user to be found.
     * @return The response DTO containing details of the found user.
     * @throws UserNotFoundException If the user with the specified ID is not found.
     */
    @Override
    public UserResponseDTO findById(String id) throws UserNotFoundException {
        log.info("In findById()");
        User user = userRepository.findById(id)
                .orElseThrow( () -> new UserNotFoundException("user with id '"+id+"' not found"));
        log.info("user found");
        return mappers.fromUser(user);
    }

    /**
     * Finds all users in the system.
     *
     * @return A list of response DTOs containing details of all users.
     */
    @Override
    public List<UserResponseDTO> findAll() {
        log.info("In findAll()");
        List<User> users = userRepository.findAll();
        log.info(users.size()+" found(s)");
        return mappers.fromListOfUsers(users);
    }

    /**
     * Finds a page of users with the specified page number and page size.
     *
     * @param page The page number (zero-based).
     * @param size The size of the page.
     * @return A response DTO containing a page of users.
     */
    @Override
    public UsersPageResponseDTO findAll(int page, int size) {
        log.info("In findAll()");
        Page<User> userPage = userRepository.findAll(PageRequest.of(page, size));
        List<User> users = userPage.getContent();
        log.info(users.size()+" found(s)");
        return new UsersPageResponseDTO(
              userPage.getTotalPages(), page, size, mappers.fromListOfUsers(users)
        );
    }

    /**
     * Deletes a user by the specified ID.
     *
     * @param id The ID of the user to be deleted.
     */
    @Override
    public void deleteById(String id) {
        log.info("In deleteById()");
        userRepository.deleteById(id);
        log.info("user deleted");
    }

    /**
     * Deletes all users in the system.
     */
    @Override
    public void deleteAll() {
        log.info("In deleteAll()");
        userRepository.deleteAll();
        log.info("users deleted");
    }

    /**
     * Adds a role to a user based on the provided {@link UserRoleRequestDTO}.
     *
     * @param userRoleRequestDTO The DTO containing user and role information for the association.
     * @return {@code true} if the role is successfully added to the user, {@code false} otherwise.
     * @throws UserNotFoundException If the user specified in the DTO is not found.
     * @throws RoleNotFoundException If the role specified in the DTO is not found.
     */
    @Transactional
    @Override
    public Boolean addRoleToUser(@NotNull UserRoleRequestDTO userRoleRequestDTO) throws UserNotFoundException, RoleNotFoundException {
        log.info("In addRoleToUser()");
        User user = checkingIfUserExist(userRoleRequestDTO.username());
        Role role = checkingIfRoleExist(userRoleRequestDTO.roleName());
        try{
            user.getRoles().add(role);
            userRepository.save(user);
            log.info("role added");
            return true;
        }catch (Exception e){
            log.error("role not added : "+e.getMessage());
            return false;
        }
    }

    /**
     * Removes a role from a user based on the provided {@link UserRoleRequestDTO}.
     *
     * @param userRoleRequestDTO The DTO containing user and role information for the removal.
     * @return {@code true} if the role is successfully removed from the user, {@code false} otherwise.
     * @throws UserNotFoundException If the user specified in the DTO is not found.
     * @throws RoleNotFoundException If the role specified in the DTO is not found.
     */
    @Transactional
    @Override
    public Boolean removeRoleToUser(@NotNull UserRoleRequestDTO userRoleRequestDTO) throws UserNotFoundException, RoleNotFoundException {
        User user = checkingIfUserExist(userRoleRequestDTO.username());
        Role role = checkingIfRoleExist(userRoleRequestDTO.roleName());
        try{
            user.getRoles().remove(role);
            userRepository.save(user);
            log.info("role removed");
            return true;
        }catch (Exception e){
            log.error("role not removed : "+e.getMessage());
            return false;
        }

    }


    /**
     * Retrieves the "USER" role from the role repository.
     * If the role is not found, it creates and saves a new "USER" role in the repository.
     *
     * @return The "USER" role from the repository.
     */
    private @NotNull Role getUserRole() {
        Role role = roleRepository.findByName("USER");
        // If the "USER" role doesn't exist, create and save a new one.
        return Objects.requireNonNullElseGet(role, () -> roleRepository.save(new Role(null, "USER")));
    }

    /**
     * Checks if the provided username or email already exists in the user repository.
     *
     * @param username The username to check for existence.
     * @param email    The email to check for existence.
     * @throws ItemAlreadyExistException If the username or email already exists in the repository.
     */
    private void checkIfUsernameOrEmailAlreadyExists(String username, String email) throws ItemAlreadyExistException {
        if (Boolean.TRUE.equals(userRepository.checkIfUsernameExists(username))) {
            throw new ItemAlreadyExistException("Username already exists");
        }
        if (Boolean.TRUE.equals(userRepository.checkIfEmailExists(email))) {
            throw new ItemAlreadyExistException("Email already exists");
        }
    }

    /**
     * Checks conditions before updating a user to ensure that the new username and email do not conflict with existing ones.
     *
     * @param user The existing user to be updated.
     * @param dto  The DTO containing the updated user information.
     * @throws ItemAlreadyExistException If the updated username or email already exists in the repository.
     */
    private void checkingBeforeUpdate(@NotNull User user, @NotNull UserRequestDTO dto) throws ItemAlreadyExistException {
        if (!user.getUsername().equals(dto.username()) && (Boolean.TRUE.equals(userRepository.checkIfUsernameExists(dto.username())))) {
            throw new ItemAlreadyExistException("Username already exists");
        }
        if (!user.getEmail().equals(dto.email()) && (Boolean.TRUE.equals(userRepository.checkIfEmailExists(dto.email())))) {
            throw new ItemAlreadyExistException("Email already exists");
        }
    }

    /**
     * Checks if a role with the specified name exists in the role repository.
     *
     * @param roleName The name of the role to check for existence.
     * @return The existing role with the specified name.
     * @throws RoleNotFoundException If the role with the specified name is not found.
     */
    private @NotNull Role checkingIfRoleExist(String roleName) throws RoleNotFoundException {
        Role role = roleRepository.findByName(roleName);
        if (role == null) {
            throw new RoleNotFoundException("Role not found");
        }
        return role;
    }

    /**
     * Checks if a user with the specified username exists in the user repository.
     *
     * @param username The username of the user to check for existence.
     * @return The existing user with the specified username.
     * @throws UserNotFoundException If the user with the specified username is not found.
     */
    private @NotNull User checkingIfUserExist(String username) throws UserNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        return user;
    }
}
