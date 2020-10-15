package com.rest.api.advice.exception;

public class EmailSignInFailedException extends RuntimeException {

    public EmailSignInFailedException() {
        super();
    }

    public EmailSignInFailedException(String message) {
        super(message);
    }

    public EmailSignInFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
