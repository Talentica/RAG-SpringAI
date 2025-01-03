package com.openAi.security.model;

import lombok.Data;

@Data
public class JwtRequest {

  private String token;
  private String email;
}