package com.openAi.security.model;

import lombok.Data;

@Data
public class UserRequest {
    private String user;
    private String password;
    private String oldPassword;
}
