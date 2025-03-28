package com.openAi.user;

import com.openAi.security.enums.EntityStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository
        extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

    Optional<User> findByEmailIgnoreCaseAndStatus(String email, EntityStatus status);

}
