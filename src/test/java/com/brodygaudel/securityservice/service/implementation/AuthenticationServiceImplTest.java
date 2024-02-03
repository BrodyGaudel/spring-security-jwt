package com.brodygaudel.securityservice.service.implementation;

import com.brodygaudel.securityservice.dto.LoginRequestDTO;
import com.brodygaudel.securityservice.dto.LoginResponseDTO;
import com.brodygaudel.securityservice.entity.Role;
import com.brodygaudel.securityservice.entity.User;
import com.brodygaudel.securityservice.repository.UserRepository;
import com.brodygaudel.securityservice.security.SecurityParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Integration tests for the {@link AuthenticationServiceImpl} class.
 * These tests validate the behavior of authentication functionality.
 *
 * @since 2024
 * @author Brody Gaudel MOUNANGA BOUKA
 * @version 1.0
 */
@SpringBootTest
class AuthenticationServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityParameters securityParameters;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @BeforeEach
    void setUp() {
        authenticationService = new AuthenticationServiceImpl(
                authenticationManager,
                userRepository,
                securityParameters
        );
    }

    /**
     * Test the login functionality by providing a username.
     * This test ensures proper authentication and response generation.
     */
    @Test
    void testLoginByUsername() {
        // Arrange
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("username", "password");
        List<Role> roles = List.of(new Role(1L, "ADMIN"), new Role(2L, "USER"));

        User user = User.builder().id("id").password("password").username("username").email("email").enabled(true)
                .roles(roles).lastUpdate(LocalDateTime.now()).creation(LocalDateTime.now())
                .build();

        when(userRepository.findByEmail("username")).thenReturn(null);
        when(userRepository.findByUsername("username")).thenReturn(user);
        when(securityParameters.getExpiredTime()).thenReturn(3600000L);
        when(securityParameters.getSecret()).thenReturn("secret");

        // Act
        LoginResponseDTO response = authenticationService.login(loginRequestDTO);

        // Assert
        assertEquals("username", response.username());
        // Add more assertions based on your expected behavior
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    /**
     * Test the login functionality by providing an email address.
     * This test ensures proper authentication and response generation.
     */
    @Test
    void testLoginByEmail() {
        // Arrange
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("email@example.com", "password");
        List<Role> roles = List.of(new Role(1L, "ADMIN"), new Role(2L, "USER"));

        User user = User.builder().id("id").password("password").username("username").email("email@example.com").enabled(true)
                .roles(roles).lastUpdate(LocalDateTime.now()).creation(LocalDateTime.now())
                .build();

        when(userRepository.findByEmail("email@example.com")).thenReturn(user);
        when(userRepository.findByUsername("username")).thenReturn(user);
        when(securityParameters.getExpiredTime()).thenReturn(3600000L);
        when(securityParameters.getSecret()).thenReturn("secret");

        // Act
        LoginResponseDTO response = authenticationService.login(loginRequestDTO);

        // Assert
        assertEquals("username", response.username());
        // Add more assertions based on your expected behavior
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}