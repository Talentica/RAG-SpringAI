package com.openAi.security.repository;

import com.openAi.security.entity.Roles;
import com.openAi.security.entity.UserTeamRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Repository
public interface UserTeamRoleRepository extends JpaRepository<UserTeamRoles, Integer> {
  List<UserTeamRoles> findByUserRole(Roles userRoles);

  UserTeamRoles findByUserIdAndTeamId(Integer user, Integer team);

  List<UserTeamRoles> findAllByTeam_Id(Integer team);

  @Modifying
  @Transactional
  @Query(value = "delete from playbook.user_team_roles where team_id = :team", nativeQuery = true)
  void deleteByTeam_Id(Integer team);

  @Modifying
  @Transactional
  @Query(value = "delete from playbook.user_team_roles where user_id = :user", nativeQuery = true)
  void deleteByUser_Id(Integer user);
}
