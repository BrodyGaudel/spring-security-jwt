package com.brodygaudel.securityservice.repository;

import com.brodygaudel.securityservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository interface for managing user entities in the database.
 * This interface extends JpaRepository to provide CRUD operations for the User entity.
 *
 * @see JpaRepository
 * @since 2024
 * @author Brody Gaudel MOUNANGA BOUKA
 * @version 1.0
 */
public interface UserRepository extends JpaRepository<User, String> {

    /**
     * Retrieves a user by their username.
     *
     * @param username The username of the user to retrieve.
     * @return The user with the specified username, or null if not found.
     */
    @Query("select u from User u where u.username = ?1")
    User findByUsername(String username);

    /**
     * Retrieves a user by their email address.
     *
     * @param email The email address of the user to retrieve.
     * @return The user with the specified email address, or null if not found.
     */
    @Query("select u from User u where u.email = ?1")
    User findByEmail(String email);

    /**
     * Checks if a user with the given username already exists.
     *
     * @param username The username to check for existence.
     * @return True if a user with the specified username exists, false otherwise.
     */
    @Query("select case when count(u)>0 then true else false END from User u where u.username = ?1")
    Boolean checkIfUsernameExists(String username);

    /**
     * Checks if a user with the given email address already exists.
     *
     * @param email The email address to check for existence.
     * @return True if a user with the specified email address exists, false otherwise.
     */
    @Query("select case when count(u)>0 then true else false END from User u where u.email = ?1")
    Boolean checkIfEmailExists(String email);
}

