package com.openAi.security.service;

import com.openAi.security.enums.UserRole;

import java.util.Set;

public interface UserTeamRoleService {

  Set<String> getUsersByRole(UserRole role);
}
