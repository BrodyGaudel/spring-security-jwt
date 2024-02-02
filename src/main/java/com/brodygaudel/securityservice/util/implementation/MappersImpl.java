package com.brodygaudel.securityservice.util.implementation;

import com.brodygaudel.securityservice.dto.UserRequestDTO;
import com.brodygaudel.securityservice.dto.UserResponseDTO;
import com.brodygaudel.securityservice.entity.Role;
import com.brodygaudel.securityservice.entity.User;
import com.brodygaudel.securityservice.util.Mappers;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of the Mappers interface providing methods for mapping between DTOs and entities.
 * This class overrides methods to convert between different representations of user data.
 *
 * @since 2024
 * @author Brody Gaudel MOUNANGA BOUKA
 * @version 1.0
 */
@Component
public class MappersImpl implements Mappers {
    /**
     * Converts a UserRequestDTO to a User entity.
     *
     * @param userRequestDTO The UserRequestDTO to convert.
     * @return The corresponding User entity.
     */
    @Override
    public User fromUserRequestDTO(@NotNull UserRequestDTO userRequestDTO) {
        return User.builder()
                .id(userRequestDTO.id())
                .username(userRequestDTO.username())
                .email(userRequestDTO.email())
                .password(userRequestDTO.password())
                .build();
    }

    /**
     * Converts a User entity to a UserResponseDTO.
     *
     * @param user The User entity to convert.
     * @return The corresponding UserResponseDTO.
     */
    @Override
    public UserResponseDTO fromUser(@NotNull User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getEnabled(),
                rolesToStrings(user.getRoles()),
                user.getCreation(),
                user.getLastUpdate()
        );
    }

    /**
     * Converts a list of User entities to a list of UserResponseDTOs.
     *
     * @param users The list of User entities to convert.
     * @return The corresponding list of UserResponseDTOs.
     */
    @Override
    public List<UserResponseDTO> fromListOfUsers(@NotNull List<User> users) {
        return users.stream().map(this::fromUser).toList();
    }

    /**
     * Converts a list of Role objects to a set of their corresponding names.
     *
     * @param roles The list of Role objects to convert.
     * @return A set of strings representing the names of the roles.
     *         Returns an empty set if the input list is null or empty.
     */
    private Set<String> rolesToStrings(List<Role> roles){
        if(roles == null || roles.isEmpty()){
            return new HashSet<>();
        }
        return roles.stream().map(Role::getName).collect(Collectors.toSet());
    }
}
