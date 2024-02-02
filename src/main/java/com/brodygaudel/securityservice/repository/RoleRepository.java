package com.brodygaudel.securityservice.repository;

import com.brodygaudel.securityservice.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository interface for managing role entities in the database.
 * This interface extends JpaRepository to provide CRUD operations for the Role entity.
 *
 * @see org.springframework.data.jpa.repository.JpaRepository
 * @since 2024
 * @author Brody Gaudel MOUNANGA BOUKA
 * @version 1.0
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Retrieves a role by its name.
     *
     * @param name The name of the role to retrieve.
     * @return The role with the specified name, or null if not found.
     */
    @Query("select r from Role r where r.name = ?1")
    Role findByName(String name);
}

