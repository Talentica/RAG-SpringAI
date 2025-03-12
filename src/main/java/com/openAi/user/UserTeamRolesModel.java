package com.openAi.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.openAi.team.Team;
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
