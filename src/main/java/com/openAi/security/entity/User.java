package com.openAi.security.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.openAi.security.entity.Team;
import com.openAi.security.entity.UserTeamRoles;
import com.openAi.security.enums.EntityStatus;
import com.openAi.security.enums.UserRole;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.*;
import javax.persistence.criteria.JoinType;
import java.util.List;

/**
 * User Entity.
 */
@Entity
@Getter
@Setter
@Accessors(chain = true)
public class User {

  @Id
  @Column(columnDefinition = "INT(11) UNSIGNED")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String name;

  @Enumerated(EnumType.STRING)
  private UserRole role = UserRole.LEAD;

  @JsonIgnoreProperties("user")
  @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private List<UserTeamRoles> userTeamRoles;

  private String email;
  private Boolean isTeg;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
          name = "user_teams",
          joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
          inverseJoinColumns = @JoinColumn(name = "teams_id", referencedColumnName = "id"))
  @JsonIgnoreProperties("teamLeads")
  private List<Team> teams;

  @Enumerated(EnumType.STRING)
  private EntityStatus status = EntityStatus.ACTIVE;


  public static Specification<User> idSpec(int id) {
    return (user, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(user.get("id"), id);
  }

  public static Specification<User> nameSpec(String name) {
    return (user, criteriaQuery, criteriaBuilder) ->
            criteriaBuilder.like(user.get("name"), "%" + name + "%");
  }

  public static Specification<User> emailSpec(String email) {
    return (user, criteriaQuery, criteriaBuilder) ->
            criteriaBuilder.like(user.get("email"), "%" + email + "%");
  }

  public static Specification<User> roleSpec(UserRole role) {
    return (user, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(user.get("role"), role);
  }

  public static Specification<User> teamSpec(String teamName) {
    return (user, criteriaQuery, criteriaBuilder) ->
            criteriaBuilder.like(
                    user.join("teams", JoinType.LEFT).get("teamName"), "%" + teamName + "%");
  }

  public static Specification<User> statusSpec(EntityStatus status) {
    return (user, criteriaQuery, criteriaBuilder) ->
            criteriaBuilder.equal(user.get("status"), status);
  }

  public static Specification<User> isTegSpec(Boolean isTeg) {
    return (user, criteriaQuery, criteriaBuilder) ->
            criteriaBuilder.equal((user.get("isTeg")), isTeg);
  }

  public static Specification<User> distinct() {
    return (root, query, cb) -> {
      query.distinct(true);
      return null;
    };
  }

  public static Specification<User> userSearchFieldSpec(String searchField) {
    Specification<User> userSpecification = Specification.where(null);
    if (StringUtils.isNumeric(searchField)) {
      int id = Integer.parseInt(searchField);
      userSpecification = userSpecification.or(User.idSpec(id));
    }
    try {
      var entityStatus = EntityStatus.valueOf(searchField.toUpperCase());
      userSpecification = userSpecification.or(User.statusSpec(entityStatus));
    } catch (IllegalArgumentException e) {
      // Not Entity Status
    }
    try {
      var userRole = UserRole.valueOf(searchField.toUpperCase());
      userSpecification = userSpecification.or(User.roleSpec(userRole));
    } catch (IllegalArgumentException e) {
      // Not User Role
    }
    userSpecification =
            userSpecification
                    .or(User.nameSpec(searchField).or(User.emailSpec(searchField)))
                    .or(User.teamSpec(searchField));
    return userSpecification;
  }
}
