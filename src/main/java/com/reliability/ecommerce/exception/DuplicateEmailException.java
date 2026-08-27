
package com.reliability.ecommerce.exception;

public class DuplicateEmailException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DuplicateEmailException(String message) {
        super(message);
    }

    public DuplicateEmailException(String message, Throwable cause) {
        super(message, cause);
    }
}

