package com.openAi.security.repository;

import com.openAi.security.entity.User;
import com.openAi.security.enums.EntityStatus;
import com.openAi.security.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/** User Repository. */
@Repository
public interface UserRepository
        extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

    Optional<User> findByEmailIgnoreCaseAndStatus(String email, EntityStatus status);

    Optional<User> findByEmailIgnoreCase(String email);

    Page<User> findByStatusInAndRoleIn(
            List<EntityStatus> statusList,
            List<UserRole> role,
            Specification<User> specification,
            Pageable paging);

    Optional<User> findUserByEmail(String email);

    Optional<User> findByNameIgnoreCase(String name);

    List<User> findByStatusAndRoleNotIn(EntityStatus status, List<UserRole> roles);
}
