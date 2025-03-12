package com.openAi.exceptions;

public class ErrorCode {

    public static final Integer INTERNAL_SERVER_ERROR = 500;
    public static final Integer NULL_POINTER_EXCEPTION = 500;
    public static final Integer INVALID_ACCESS = 401;

    // Authentication and Authorization
    public static final Integer INVALID_TOKEN = 1;
    public static final Integer UNAUTHORISED_USER = 2;

    // User
    public static final Integer USER_NOT_FOUND = 11;

    // Team
    public static final Integer TEAM_NOT_FOUND = 21;

    // ConfigCycle
    public static final Integer CYCLE_EXPIRED = 31;
    public static final Integer CYCLE_CONFIG_NOT_FOUND = 32;

    // Survey
    public static final Integer SURVEY_NOT_FOUND = 41;
    public static final Integer NOT_ENOUGH_SURVEY = 42;

    // File
    public static final Integer INVALID_FILE_EXTENSION = 51;
    public static final Integer INVALID_FILE_FORMAT = 52;

    // Csat
    public static final Integer NOT_ENOUGH_SURVEYS = 61;
    public static final Integer CATEGORY_NOT_FOUND = 62;
    public static final Integer QUESTION_NOT_FOUND = 63;
    public static final Integer SUB_QUESTION_NOT_FOUND = 64;
    public static final Integer SURVEY_ALREADY_EXISTS = 65;
    public static final Integer PLACEHOLDER_NOT_FOUND = 66;

    // Customer
    public static final Integer CUSTOMER_NOT_FOUND = 71;

    // Product Roadmap
    public static final Integer EMPTY_STORY_NAME = 81;
    public static final Integer INVALID_END_DATE = 82;
    public static final Integer EMPTY_START_DATE = 83;
    public static final Integer INCORRECT_DATA = 84;
    public static final Integer NO_DATA_FOUND = 85;

    // Http Connection
    public static final Integer HTTP_CONNECTION_FAILED = 91;

    // Section
    public static final Integer SECTION_NOT_FOUND = 101;

    // TouchPoint
    public static final Integer INVALID_FILE = 1101;

    // Teams
    public static final Integer EMPLOYEE_NOT_FOUND = 1201;
    public static final Integer NO_DATA_FOUND_FOR_EMPLOYEE = 1202;

    public static final Integer TECH_DIGEST_NOT_FOUND = 1301;
    private ErrorCode() {
    }
}
