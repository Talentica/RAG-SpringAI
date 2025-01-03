package com.openAi.security.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.openAi.security.entity.Customer;
import com.openAi.security.enums.EntityStatus;
import com.openAi.security.enums.UserRole;
import lombok.*;

import java.util.List;

/**
 * User Model.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {

  private Integer id;
  private String name;
  private UserRole role;
  private String email;
  @JsonIgnoreProperties(value = {"teamLeads", "userTeamRoles"}, allowSetters = true)
  private List<TeamModel> teams;
  private List<UserTeamRolesModel> teamRoles;
  private boolean isCustomerRole;
  private EntityStatus status;
    private List<Customer> customers;
    private boolean isTeg;
}
