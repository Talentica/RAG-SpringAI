package com.openAi.security.exceptions;

import static com.openAi.security.exceptions.ErrorCode.INVALID_TOKEN;
import static com.openAi.security.exceptions.ErrorCode.UNAUTHORISED_USER;

public class InvalidAuthenticationException extends RAGException {

  private InvalidAuthenticationException(String message, Integer code) {
    super(message);
    setCode(code);
  }

  public static InvalidAuthenticationException invalidToken(String message) {
    return new InvalidAuthenticationException(message, INVALID_TOKEN);
  }

  public static InvalidAuthenticationException unauthorisedUser(String message) {
    return new InvalidAuthenticationException(message, UNAUTHORISED_USER);
  }
}
