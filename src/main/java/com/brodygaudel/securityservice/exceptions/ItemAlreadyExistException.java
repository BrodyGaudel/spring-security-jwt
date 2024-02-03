package com.brodygaudel.securityservice.exceptions;

/**
 * Exception thrown when an item already exists.
 * This exception is typically used to indicate that an attempt to create or add
 * an item failed because the item already exists in the system.
 *
 * @since 2024
 * @author Brody Gaudel MOUNANGA BOUKA
 * @version 1.0
 */
public class ItemAlreadyExistException extends Exception {

    /**
     * Constructs a new {@code ItemAlreadyExistException} with the specified detail message.
     * The cause is not initialized and may subsequently be initialized by a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public ItemAlreadyExistException(String message) {
        super(message);
    }
}

