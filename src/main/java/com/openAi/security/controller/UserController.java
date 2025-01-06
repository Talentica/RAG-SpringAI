package com.openAi.security.controller;

import com.openAi.security.entity.Roles;
import com.openAi.security.entity.User;
import com.openAi.security.enums.AppConstant;
import com.openAi.security.enums.EntityStatus;
import com.openAi.security.enums.UserRole;
import com.openAi.security.mapper.UserMapper;
import com.openAi.security.mapper.UserTeamRolesMapper;
import com.openAi.security.model.UserListViewResponse;
import com.openAi.security.model.UserModel;
import com.openAi.security.service.UserService;
import com.openAi.security.service.UserTeamRoleService;
import com.openAi.security.utils.SessionUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    UserTeamRolesMapper userTeamRolesMapper = Mappers.getMapper(UserTeamRolesMapper.class);
    private final UserService userService;
    private final SessionUtils sessionUtils;
    private final UserTeamRoleService userTeamRoleService;

    @GetMapping("/loggedInUser")
    public ResponseEntity<UserModel> getLoggedInUser() {
        return ResponseEntity.status(HttpStatus.OK).body(sessionUtils.getLoggedInUser());
    }

    @GetMapping
    public ResponseEntity<UserModel> getUser(@RequestParam String email) {
        if (StringUtils.isEmpty(email)) {
            log.error("Invalid email address.");
            throw new IllegalArgumentException("Invalid email address");
        }
        UserModel userModel = userService.findUserByEmailOrAttendance(email);
        return ResponseEntity.status(HttpStatus.OK).body(userModel);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Roles>> getRoles() {
        List<Roles> roles = userService.getRoles();
        return ResponseEntity.status(HttpStatus.OK).body(roles);
    }

    @GetMapping("all")
    public ResponseEntity<UserListViewResponse> getAllUser(
            @RequestParam(value = "role", required = false) UserRole role,
            @RequestParam(value = "status", required = false) EntityStatus status,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "teamName", required = false) String teamName,
            @RequestParam(value = "isTeg", required = false) Boolean isTeg,
            @RequestParam(value = "orderBy", defaultValue = "id", required = false) String orderBy,
            @RequestParam(value = "direction", defaultValue = "DESC", required = false)
            Sort.Direction direction,
            @RequestParam(value = "page", defaultValue = AppConstant.DEFAULT_PAGE, required = false)
            int page,
            @RequestParam(value = "size", defaultValue = AppConstant.DEFAULT_PAGE_SIZE, required = false)
            int size,
            @RequestParam(value = "searchField", required = false) String searchField) {
        Sort sort =
                orderBy.equalsIgnoreCase("id")
                        ? Sort.by(direction, orderBy)
                        : Sort.by(direction, orderBy).and(Sort.by(Sort.Direction.ASC, "id"));
        Pageable paging = PageRequest.of(page, size, sort);
        Specification<User> userSpecification = Specification.where(null);

        if (Arrays.asList(UserRole.values()).contains(role)) {
            userSpecification = userSpecification.and(User.roleSpec(role));
        }
        if (Objects.nonNull(status)) {
            userSpecification = userSpecification.and(User.statusSpec(status));
        }
        if (Objects.nonNull(name)) {
            userSpecification = userSpecification.and(User.nameSpec(name));
        }
        if (Objects.nonNull(email)) {
            userSpecification = userSpecification.and(User.emailSpec(email));
        }
        if (Objects.nonNull(teamName)) {
            userSpecification = userSpecification.and(User.teamSpec(teamName));
        }
        if (Objects.nonNull(isTeg)) {
            userSpecification = userSpecification.and(User.isTegSpec(isTeg));
        }
        if (StringUtils.isNotBlank(searchField)) {
            userSpecification = userSpecification.and(User.userSearchFieldSpec(searchField));
        }
        userSpecification = userSpecification.and(User.distinct());
        UserListViewResponse userListViewResponse = userService.findAllUsers(userSpecification, paging);
        return ResponseEntity.status(HttpStatus.OK).body(userListViewResponse);
    }

    @GetMapping("/manager-list")
    public ResponseEntity<Set<String>> getSeniorManagers(@RequestParam(required = true) String role) {
        UserRole userRole = UserRole.valueOf(role);
        return ResponseEntity.ok(userTeamRoleService.getUsersByRole(userRole));
    }
}
