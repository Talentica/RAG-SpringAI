package com.openAi.security.repository;

import com.openAi.security.entity.UserLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface UserLogRepository extends JpaRepository<UserLog, Integer> {
    List<UserLog> findAllByUserIdAndLogInDate(Integer userId, Date logInDate);
}
