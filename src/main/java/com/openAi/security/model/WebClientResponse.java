package com.openAi.security.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebClientResponse {
  private String displayName;
  private String givenName;
  private String mail;
  private String mobilePhone;
  private String id;
  private String userPrincipalName;
  private String type = "Bearer";
  private String token;
  private Boolean isResetRequired;
  private String reason;
}
