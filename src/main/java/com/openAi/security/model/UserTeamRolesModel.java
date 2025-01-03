package com.openAi.security.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.openAi.security.entity.Roles;
import com.openAi.security.entity.Team;
import com.openAi.security.entity.User;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTeamRolesModel {
  private int id;
  private User user;
  private Roles userRole;

  @JsonIgnoreProperties(
      value = {"teamLeads"},
      allowSetters = true)
  private Team team;
}
