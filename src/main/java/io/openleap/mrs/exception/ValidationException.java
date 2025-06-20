package io.openleap.mrs.exception;

import java.io.Serial;

public class ValidationException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * Constructs a new validation exception with the specified detail message.
     *
     */
    public ValidationException() {
        super("Validation failed");
    }
    /**
     * Constructs a new validation exception with the specified detail message.
     *
     * @param message the detail message
     */
    public ValidationException(String message) {
        super(message);
    }
}
