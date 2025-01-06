package com.openAi.security.utils;

import com.openAi.security.model.UserModel;
import com.openAi.security.service.UserService;
import com.openAi.security.utils.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SessionUtils {

  private final HttpServletRequest request;
  private final UserService userService;
  private final JwtUtils jwtUtils;

  public UserModel getLoggedInUser() {
    Cookie[] cookieAuth = request.getCookies();
    String jwt = "";
    if (Objects.nonNull(cookieAuth)) {
      for (Cookie cookie : cookieAuth) {
        if (cookie.getName().equals("auth-token")) {
          jwt = cookie.getValue();
        }
      }
    }

    String email = jwtUtils.getSubject(jwt);
    return userService.findUserByEmail(email);
  }
}
