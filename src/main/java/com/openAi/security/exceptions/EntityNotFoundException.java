package com.openAi.security.exceptions;


import static com.openAi.security.exceptions.ErrorCode.*;

/**
 * EntityNotFoundException.
 */
public class EntityNotFoundException extends RAGException {

  private EntityNotFoundException(String message, Integer code) {
    super(message);
    setCode(code);
  }

  public static EntityNotFoundException user(String message) {
    return new EntityNotFoundException(message, USER_NOT_FOUND);
  }

  public static EntityNotFoundException team(String message) {
    return new EntityNotFoundException(message, TEAM_NOT_FOUND);
  }

  public static EntityNotFoundException cycleConfig(String message) {
    return new EntityNotFoundException(message, CYCLE_CONFIG_NOT_FOUND);
  }

  public static EntityNotFoundException survey(String message) {
    return new EntityNotFoundException(message, SURVEY_NOT_FOUND);
  }

  public static EntityNotFoundException customer(String message) {
    return new EntityNotFoundException(message, CUSTOMER_NOT_FOUND);
  }

  public static EntityNotFoundException section(String message) {
    return new EntityNotFoundException(message, SECTION_NOT_FOUND);
  }

  public static EntityNotFoundException employee(String message) {
    return new EntityNotFoundException(message, EMPLOYEE_NOT_FOUND);
  }

  public static EntityNotFoundException dataForEmployee(String message) {
    return new EntityNotFoundException(message, NO_DATA_FOUND_FOR_EMPLOYEE);
  }

    public static RuntimeException techDigest(String s) {
      return new EntityNotFoundException(s,TECH_DIGEST_NOT_FOUND);
    }
}
