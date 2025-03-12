package com.openAi.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.openAi.customers.Customer;
import com.openAi.security.enums.EntityStatus;
import com.openAi.team.TeamModel;
import lombok.*;

import java.util.List;

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
    private Boolean isCustomerRole;
    private EntityStatus status;
    private List<Customer> customers;
    private Boolean isTeg;
}
