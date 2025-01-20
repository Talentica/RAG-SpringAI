package com.openAi.security;

import com.openAi.security.exceptions.InvalidAuthenticationException;
import com.openAi.security.model.UserModel;
import com.openAi.security.service.UserService;
import com.openAi.security.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.Collections;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import static com.openAi.security.enums.EntityStatus.ACTIVE;

@Slf4j
@Component

public class AuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private HttpServletRequest httpServletRequest;

    @Value("${security.apiKey}")
    private String apiKey;
    private AuthenticationManager authenticationManager;

    public AuthenticationFilter() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Override
    @SneakyThrows
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {

        String path = request.getRequestURI();
        log.info(
                String.format("Requested path: %s at %s by %s", path, new Date(), request.getRemoteHost()));
        if (
                new AntPathMatcher().match("/getToken", path)
                        || HttpMethod.OPTIONS.name().equalsIgnoreCase(request.getMethod())
                        || new AntPathMatcher().match("/", path)
                        && apiKey.equals(request.getHeader("Api-Key"))) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt;
        try {
            jwt = parseJwt();
            jwtUtils.validateJwtToken(jwt);
        } catch (Exception e) {
            log.error(
                    String.format(
                            "%s | AUTHORIZATION Header: %s",
                            e.getMessage(), request.getHeader(HttpHeaders.AUTHORIZATION)));
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        Claims claims = jwtUtils.getAllClaimsFromToken(jwt);
        String email = claims.getSubject();
        UserModel userModel;
        try {
            userModel = userService.findUserByEmailOrAttendance(email);
        } catch (Exception e) {
            log.error("User not found for email {}", email);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        boolean enabled = ACTIVE.equals(userModel.getStatus());
        User user =
                new User(
                        userModel.getEmail(),
                        UUID.randomUUID().toString(),
                        enabled,
                        true,
                        true,
                        true,
                        Collections.singletonList(new SimpleGrantedAuthority(userModel.getRole().name())));

        final UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    public String parseJwt() {

        Cookie[] cookieAuth = httpServletRequest.getCookies();
        if (Objects.nonNull(cookieAuth)) {
            for (Cookie cookie : cookieAuth) {
                if (cookie.getName().equals("auth-token")) {
                    return cookie.getValue();
                }
            }
        }

        throw InvalidAuthenticationException.invalidToken("Failed to parse JWT Token");
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
}


