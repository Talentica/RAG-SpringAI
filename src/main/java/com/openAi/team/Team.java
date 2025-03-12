package com.openAi.team;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.openAi.customers.Customer;
import com.openAi.security.enums.EntityStatus;
import com.openAi.user.User;
import lombok.*;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Team {

    @Id
    @Column(columnDefinition = "INT(11) UNSIGNED")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String teamName;

    private String teamShortName;

    private String teamJiraName;

    private String teamJiraLink;

    @Enumerated(EnumType.STRING)
    private TeamType teamType;

    private Double noOfHoursPerSP;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("teams")
    @JoinTable(
            name = "user_teams",
            joinColumns = @JoinColumn(name = "teams_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private List<User> teamLeads;

    @Column(columnDefinition = "DATE")
    private LocalDate activeSince;

    @Column(columnDefinition = "DATE")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private EntityStatus status = EntityStatus.ACTIVE;

    private Boolean isTeg;
    private Boolean excludeCompliance;

    private Boolean complianceByStory;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    @JsonIgnoreProperties("teams")
    private Customer customer;

    private String doneStatuses;

    private String onTimeIssueTypes;

    public static Specification<Team> idSpec(int id) {
        return (team, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(team.get("id"), id);
    }

    public static Specification<Team> statusSpec(EntityStatus status) {
        return (team, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal((team.get("status")), status);
    }

    public static Specification<Team> teamTypeSpec(TeamType teamType) {
        return (team, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal((team.get("teamType")), teamType);
    }

    public static Specification<Team> teamNameSpec(String teamName) {
        return (team, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(team.get("teamName"), "%" + teamName + "%");
    }

    public static Specification<Team> teamLeadNameSpec(String teamLeadName) {
        return (team, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(
                        team.join("teamLeads").get("name"), "%" + teamLeadName + "%");
    }

    public static Specification<Team> teamLeadEmailSpec(String teamLeadEmail) {
        return (team, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(
                        team.join("teamLeads").get("email"), "%" + teamLeadEmail + "%");
    }

    public static Specification<Team> isTegSpec(Boolean isTeg) {
        return (team, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal((team.get("isTeg")), isTeg);
    }

    public static Specification<Team> distinct() {
        return (root, query, cb) -> {
            query.distinct(true);
            return null;
        };
    }

    public static Specification<Team> teamSearchFieldSpec(String searchField) {
        Specification<Team> teamSpecification = Specification.where(null);
        if (StringUtils.isNumeric(searchField)) {
            int id = Integer.parseInt(searchField);
            teamSpecification = teamSpecification.or(Team.idSpec(id));
        }
        try {
            var entityStatus = EntityStatus.valueOf(searchField.toUpperCase());
            teamSpecification = teamSpecification.or(Team.statusSpec(entityStatus));
        } catch (IllegalArgumentException e) {
            // Not Entity Status
        }
        try {
            var teamType = TeamType.valueOf(searchField.toUpperCase());
            teamSpecification = teamSpecification.or(Team.teamTypeSpec(teamType));
        } catch (IllegalArgumentException e) {
            // Not Team Type
        }
        teamSpecification =
                teamSpecification.or(Team.teamLeadNameSpec(searchField)).or(Team.teamNameSpec(searchField));
        return teamSpecification;
    }
}
