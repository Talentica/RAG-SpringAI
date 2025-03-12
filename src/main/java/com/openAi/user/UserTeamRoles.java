package com.openAi.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.openAi.team.Team;
import lombok.*;
import lombok.experimental.Accessors;

import jakarta.persistence.*;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTeamRoles{

    @Id
    @Column(columnDefinition = "INT(11) UNSIGNED")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Roles userRole;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_id")
    @JsonIgnoreProperties(value = {"teamLeads"}, allowSetters = true)
    private Team team;

}
