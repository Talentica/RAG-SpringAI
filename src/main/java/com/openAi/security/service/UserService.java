package com.openAi.security.service;

import com.openAi.security.entity.User;
import com.openAi.security.model.UserModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * UserService.
 */
public interface UserService {


    UserModel findUserByEmailOrAttendance(String email);

    UserModel findUserByEmail(String email);

    void saveUserLog(String userEmail);
}
