package com.openAi.security.repository;

import com.openAi.security.entity.Roles;
import com.openAi.security.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<Roles, Integer> {
  Roles findByRole(UserRole userRole);
}
