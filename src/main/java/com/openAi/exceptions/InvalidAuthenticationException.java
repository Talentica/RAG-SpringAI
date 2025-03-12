package com.openAi.exceptions;

import org.springframework.security.core.AuthenticationException;

import static com.openAi.exceptions.ErrorCode.INVALID_TOKEN;
import static com.openAi.exceptions.ErrorCode.UNAUTHORISED_USER;

public class InvalidAuthenticationException extends AuthenticationException {

    private final Integer code;

    public InvalidAuthenticationException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public static InvalidAuthenticationException invalidToken(String message) {
        return new InvalidAuthenticationException(message, INVALID_TOKEN); // Custom error code
    }

    public static InvalidAuthenticationException unauthorisedUser(String message) {
        return new InvalidAuthenticationException(message, UNAUTHORISED_USER);
    }
}
