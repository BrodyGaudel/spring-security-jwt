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
import com.brodygaudel.securityservice.util.Mappers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Integration tests for the {@link UserServiceImpl} class.
 * These tests validate the behavior of user-related operations.
 *
 * @since 2024
 * @author Brody Gaudel MOUNANGA BOUKA
 * @version 1.0
 */
@SpringBootTest
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private Mappers mappers;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(
                userRepository,
                roleRepository,
                mappers,
                passwordEncoder
        );

    }

    /**
     * Test the save operation for creating a new user.
     * This test ensures proper validation, mapping, and repository interaction.
     *
     * @throws ItemAlreadyExistException If the user already exists.
     */
    @Test
    void save() throws ItemAlreadyExistException {
        UserRequestDTO request = new UserRequestDTO("id", "username", "email@exemple.com", "password");
        List<Role> roles = List.of(new Role(1L, "ADMIN"), new Role(2L, "USER"));
        User user = User.builder().id("id").password("password").username("username").email("email@example.com").enabled(true).roles(roles).creation(LocalDateTime.now()).build();

        when(userRepository.checkIfEmailExists(anyString())).thenReturn(false);
        when(userRepository.checkIfUsernameExists(anyString())).thenReturn(false);
        when(mappers.fromUserRequestDTO(request)).thenReturn(user);
        when(roleRepository.findByName(anyString())).thenReturn(new Role(1L, "USER"));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(
                User.builder().id("id").password("encodedPassword").username("username").email("email@example.com").enabled(true).roles(roles).creation(LocalDateTime.now()).build()
        );
        when(mappers.fromUser(any())).thenReturn(
                new UserResponseDTO("id", "username", "email@exemple.com", true, new HashSet<>(), LocalDateTime.now(), null)
        );

        UserResponseDTO response = userService.save(request);
        assertNotNull(response);
        verify(userRepository, times(1)).save(any());
    }

    /**
     * Test the update operation for modifying an existing user.
     * This test ensures proper validation, mapping, and repository interaction.
     *
     * @throws UserNotFoundException     If the user with the specified ID is not found.
     * @throws ItemAlreadyExistException If the updated user already exists.
     */
    @Test
    void update() throws UserNotFoundException, ItemAlreadyExistException {
        String id = "id";
        UserRequestDTO request = new UserRequestDTO(id, "username", "email@exemple.com", "password");
        List<Role> roles = List.of(new Role(1L, "ADMIN"), new Role(2L, "USER"));
        User user = User.builder().id(id).password("password").username("username").email("email@example.com").enabled(true).roles(roles).creation(LocalDateTime.now()).build();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.checkIfEmailExists(anyString())).thenReturn(false);
        when(userRepository.checkIfUsernameExists(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(
                User.builder().id(id).password("encodedPassword").username("username").email("email@example.com").enabled(true).roles(roles).creation(LocalDateTime.now()).lastUpdate(LocalDateTime.now()).build()
        );
        when(mappers.fromUser(any())).thenReturn(
                new UserResponseDTO(id, "username", "email@exemple.com", true, new HashSet<>(), LocalDateTime.now(), LocalDateTime.now())
        );

        UserResponseDTO response = userService.update(id, request);
        assertNotNull(response);
        assertEquals(id, response.id());
        verify(userRepository, times(1)).save(any());
    }

    /**
     * Test finding a user by ID.
     * This test ensures proper repository interaction and mapping.
     *
     * @throws UserNotFoundException If the user with the specified ID is not found.
     */
    @Test
    void findById() throws UserNotFoundException {
        String id = "id";
        List<Role> roles = List.of(new Role(1L, "ADMIN"), new Role(2L, "USER"));
        User user = User.builder().id("id").password("password").username("username").email("email@example.com").enabled(true).roles(roles).creation(LocalDateTime.now()).build();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(mappers.fromUser(any())).thenReturn(
                new UserResponseDTO(id, "username", "email@exemple.com", true, new HashSet<>(), LocalDateTime.now(), LocalDateTime.now())
        );

        UserResponseDTO response = userService.findById(id);
        assertNotNull(response);
        assertEquals(id, response.id());
        verify(userRepository, times(1)).findById(id);
    }

    /**
     * Test finding all users.
     * This test ensures proper repository interaction and mapping for a list of users.
     */
    @Test
    void findAll() {
        List<Role> roles = List.of(new Role(1L, "ADMIN"), new Role(2L, "USER"));
        User user1 = User.builder().id("id1").password("1password").username("1username").email("1email@example.com").enabled(true).roles(roles).creation(LocalDateTime.now()).build();
        User user2 = User.builder().id("id2").password("2password").username("2username").email("2email@example.com").enabled(true).roles(roles).creation(LocalDateTime.now()).build();
        List<User> users = List.of(user1, user2);

        when(userRepository.findAll()).thenReturn(users);
        when(mappers.fromListOfUsers(anyList())).thenReturn(List.of(
                new UserResponseDTO("id1", "1username", "1email@exemple.com", true, new HashSet<>(), LocalDateTime.now(), LocalDateTime.now()),
                new UserResponseDTO("id2", "2username", "2email@exemple.com", true, new HashSet<>(), LocalDateTime.now(), LocalDateTime.now())
        ));

        List<UserResponseDTO> response = userService.findAll();
        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(2, response.size());
        verify(userRepository, times(1)).findAll();
    }

    /**
     * Test finding all users with pagination.
     * This test ensures proper repository interaction, mapping, and pagination handling.
     */
    @Test
    void testFindAll() {
        int page = 0;
        int size = 2;
        List<Role> roles = List.of(new Role(1L, "ADMIN"), new Role(2L, "USER"));
        User user1 = User.builder().id("id1").password("1password").username("1username").email("1email@example.com").enabled(true).roles(roles).creation(LocalDateTime.now()).build();
        User user2 = User.builder().id("id2").password("2password").username("2username").email("2email@example.com").enabled(true).roles(roles).creation(LocalDateTime.now()).build();
        List<User> users = List.of(user1, user2);
        when(userRepository.findAll(PageRequest.of(page, size))).thenReturn( new PageImpl<>(users, PageRequest.of(page, size), 2));
        when(mappers.fromListOfUsers(anyList())).thenReturn(List.of(
                new UserResponseDTO("id1", "1username", "1email@exemple.com", true, new HashSet<>(), LocalDateTime.now(), LocalDateTime.now()),
                new UserResponseDTO("id2", "2username", "2email@exemple.com", true, new HashSet<>(), LocalDateTime.now(), LocalDateTime.now())
        ));
        UsersPageResponseDTO response = userService.findAll(page, size);
        assertNotNull(response);
        List<UserResponseDTO> userResponseDTOS = response.users();
        assertNotNull(userResponseDTOS);
        assertFalse(userResponseDTOS.isEmpty());
        assertEquals(2, userResponseDTOS.size());
        verify(userRepository, times(1)).findAll(PageRequest.of(page, size));
    }

    /**
     * Test deleting a user by ID.
     * This test ensures proper repository interaction for user deletion.
     */
    @Test
    void deleteById() {
        String id = "id";
        userService.deleteById(id);
        // Assert
        verify(userRepository, times(1)).deleteById(id);
    }

    /**
     * Test deleting all users.
     * This test ensures proper repository interaction for deleting all users.
     */
    @Test
    void deleteAll() {
        // Act
        userService.deleteAll();
        // Assert
        verify(userRepository, times(1)).deleteAll();
    }

    /**
     * Test adding a role to a user.
     * This test ensures proper validation, mapping, and repository interaction.
     *
     * @throws UserNotFoundException If the specified user is not found.
     * @throws RoleNotFoundException If the specified role is not found.
     */
    @Test
    void addRoleToUser() throws UserNotFoundException, RoleNotFoundException {
        UserRoleRequestDTO request = new UserRoleRequestDTO("username", "ADMIN");
        User user = User.builder().id("id").password("password").username("username").email("email@example.com").enabled(true).roles(new ArrayList<>()).creation(LocalDateTime.now()).build();
        when(userRepository.findByUsername(anyString())).thenReturn(user);
        when(roleRepository.findByName(anyString())).thenReturn(new Role(1L, "ADMIN"));
        when(userRepository.save(user)).thenReturn(
                User.builder().id("id").password("password")
                        .username("username").email("email@example.com").enabled(true)
                        .roles(List.of(new Role(1L, "ADMIN")))
                        .creation(LocalDateTime.now()).build()
        );
        Boolean response = userService.addRoleToUser(request);
        assertNotNull(response);
        assertTrue(response);
        verify(userRepository, times(1)).save(user);
    }

    /**
     * Test removing a role from a user.
     * This test ensures proper validation, mapping, and repository interaction.
     *
     * @throws UserNotFoundException If the specified user is not found.
     * @throws RoleNotFoundException If the specified role is not found.
     */
    @Test
    void removeRoleToUser() throws UserNotFoundException, RoleNotFoundException {
        UserRoleRequestDTO request = new UserRoleRequestDTO("username", "ADMIN");
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(new Role(1L, "USER"));
        userRoles.add(new Role(2L, "ADMIN"));

        User user = User.builder().id("id").password("password").username("username").email("email@example.com").enabled(true).roles(userRoles).creation(LocalDateTime.now()).build();
        when(userRepository.findByUsername(anyString())).thenReturn(user);
        when(roleRepository.findByName(anyString())).thenReturn(new Role(2L, "ADMIN"));
        when(userRepository.save(user)).thenReturn(
                User.builder().id("id").password("password")
                        .username("username").email("email@example.com").enabled(true)
                        .roles(List.of(new Role(1L, "USER")))
                        .creation(LocalDateTime.now()).build()
        );
        Boolean response = userService.removeRoleToUser(request);
        assertNotNull(response);
        assertTrue(response);
        verify(userRepository, times(1)).save(user);
    }
}