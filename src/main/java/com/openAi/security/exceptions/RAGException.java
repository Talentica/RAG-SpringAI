package com.openAi.security.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;



@Setter
@Getter
public abstract class RAGException extends RuntimeException {

    private  Integer code = ErrorCode.INTERNAL_SERVER_ERROR;

  protected RAGException(String message) {
    super(message);
  }

  protected RAGException(String message, Throwable cause) {
    super(message, cause);
  }

  protected RAGException(Throwable cause) {
    super(cause);
  }

}
