package com.brodygaudel.securityservice.util.implementation;

import com.brodygaudel.securityservice.dto.UserRequestDTO;
import com.brodygaudel.securityservice.dto.UserResponseDTO;
import com.brodygaudel.securityservice.entity.Role;
import com.brodygaudel.securityservice.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link MappersImpl} class.
 * @since 2024
 * @author Brody Gaudel MOUNANGA BOUKA
 * @version 1.0
 */
@SpringBootTest
class MappersImplTest {

    @Autowired
    private MappersImpl mappers;

    /**
     * Tests the mapping from {@link UserRequestDTO} to {@link User}.
     */
    @Test
    void testFromUserRequestDTO() {
        // Test case setup
        UserRequestDTO userRequestDTO = new UserRequestDTO("id", "username", "email", "password");

        // Perform mapping
        User user = mappers.fromUserRequestDTO(userRequestDTO);

        // Assertions
        assertNotNull(user);
        assertEquals(user.getId(), userRequestDTO.id());
        assertEquals(user.getUsername(), userRequestDTO.username());
        assertEquals(user.getEmail(), userRequestDTO.email());
        assertEquals(user.getPassword(), userRequestDTO.password());
    }

    /**
     * Tests the mapping from {@link User} to {@link UserResponseDTO}.
     */
    @Test
    void testFromUser() {
        // Test case setup
        List<Role> roles = List.of(new Role(1L, "ADMIN"), new Role(2L, "USER"));
        User user = User.builder().id("id").password("password").username("username").email("email").enabled(true)
                .roles(roles).lastUpdate(LocalDateTime.now()).creation(LocalDateTime.now())
                .build();

        // Perform mapping
        UserResponseDTO userResponseDTO = mappers.fromUser(user);

        // Assertions
        assertNotNull(userResponseDTO);
        assertEquals(user.getId(), userResponseDTO.id());
        assertEquals(user.getUsername(), userResponseDTO.username());
        assertEquals(user.getEmail(), userResponseDTO.email());
        assertEquals(user.getEnabled(), userResponseDTO.enabled());
        assertEquals(user.getCreation(), userResponseDTO.creation());
        assertEquals(user.getLastUpdate(), userResponseDTO.lastUpdate());

        // Check roles mapping
        Set<String> roleList = userResponseDTO.roles();
        assertNotNull(roleList);
        assertFalse(roleList.isEmpty());
        assertEquals(2, roleList.size());
    }

    /**
     * Tests the mapping from a list of {@link User} to a list of {@link UserResponseDTO}.
     */
    @Test
    void testFromListOfUsers() {
        // Test case setup
        List<Role> roles = List.of(new Role(1L, "ADMIN"), new Role(2L, "USER"));
        User user1 = User.builder().id("id1").password("1password").username("1username").email("1email").enabled(true)
                .roles(roles).lastUpdate(LocalDateTime.now()).creation(LocalDateTime.now())
                .build();
        User user2 = User.builder().id("id2").password("2password").username("2username").email("2email").enabled(true)
                .roles(roles).lastUpdate(LocalDateTime.now()).creation(LocalDateTime.now())
                .build();

        List<User> users = List.of(user1, user2);

        // Perform mapping
        List<UserResponseDTO> userResponseDTOS = mappers.fromListOfUsers(users);

        // Assertions
        assertNotNull(userResponseDTOS);
        assertFalse(userResponseDTOS.isEmpty());
        assertEquals(2, userResponseDTOS.size());
    }
}
