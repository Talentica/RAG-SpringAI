package com.openAi.security.service;


import com.openAi.security.entity.Roles;
import com.openAi.security.entity.User;
import com.openAi.security.entity.UserTeamRoles;
import com.openAi.security.enums.UserRole;
import com.openAi.security.repository.RolesRepository;
import com.openAi.security.repository.UserTeamRoleRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserTeamRoleServiceImpl implements UserTeamRoleService {

  private final UserTeamRoleRepository userTeamRoleRepository;
  private final RolesRepository rolesRepository;

  @Override
  public Set<String> getUsersByRole(UserRole userRole) {

    Roles role = rolesRepository.findByRole(userRole);

    List<UserTeamRoles> userTeamRoles = userTeamRoleRepository.findByUserRole(role);
    return userTeamRoles.stream()
        .map(UserTeamRoles::getUser)
        .map(User::getName)
        .collect(Collectors.toSet());
  }
}
