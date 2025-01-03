package com.openAi.security.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.openAi.security.entity.Customer;
import com.openAi.security.entity.User;
import com.openAi.security.enums.EntityStatus;
import com.openAi.security.enums.TeamType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamModel {

  private Integer id;
  private String teamName;
  private TeamType teamType;
  private LocalDate activeSince;
  private LocalDate endDate;
  private String teamShortName;
  private String teamJiraName;
  private Boolean isTeg;
  private Boolean excludeCompliance;
  private Boolean complianceByStory;

  @JsonIgnoreProperties("teams")
  private List<User> teamLeads;

  @JsonIgnoreProperties("team")
  private List<UserTeamRolesModel> userRoles;

  private EntityStatus status;

  @JsonIgnoreProperties(value = {"epics", "customerUserRoles"}, allowSetters = true)
  private Customer customer;
}
