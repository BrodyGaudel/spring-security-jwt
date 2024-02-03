package com.brodygaudel.securityservice.exceptions;

/**
 * Exception thrown when a role is not found.
 * This exception is typically used to indicate that a role with the specified criteria
 * (such as role name) is not present in the system.
 *
 * @since 2024
 * @author Brody Gaudel MOUNANGA BOUKA
 * @version 1.0
 */
public class RoleNotFoundException extends Exception {

    /**
     * Constructs a new {@code RoleNotFoundException} with the specified detail message.
     * The cause is not initialized and may subsequently be initialized by a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public RoleNotFoundException(String message) {
        super(message);
    }
}

