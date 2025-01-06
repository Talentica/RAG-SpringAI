package com.openAi.security.service;

import com.openAi.security.entity.*;
import com.openAi.security.enums.UserRole;
import com.openAi.security.exceptions.EntityNotFoundException;
import com.openAi.security.mapper.UserMapper;
import com.openAi.security.mapper.UserTeamRolesMapper;
import com.openAi.security.model.UserListViewResponse;
import com.openAi.security.model.UserModel;
import com.openAi.security.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static com.openAi.security.enums.EntityStatus.ACTIVE;

/**
 * UserService Implementation.
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    private final String LOGIN = "login";

    @Autowired
    UserTeamRoleRepository userTeamRoleRepository;
    @Autowired
    AttendanceRepository attendanceRepository;
    @Autowired
    CustomerCredentialRepository customerCredentialRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;
    @Value("${customer-login.site-url}")
    private String siteURL;

    @Value("${customer-login.login-endpoint}")
    private String loginEndPoint;
    @Autowired
    private UserLogRepository userLogRepository;

    private final UserTeamRolesMapper userTeamRolesMapper = Mappers.getMapper(UserTeamRolesMapper.class);

    @Override
    public UserModel findUserByEmailOrAttendance(String email) {
        UserModel userModel = null;
        try {
            userModel = findUserByEmail(email);
        } catch (Exception e) {
            List<Attendance> attendanceList = attendanceRepository.findByEmailIdOrderByYearDescMonthDesc(email);
            if (attendanceList == null || attendanceList.isEmpty()) {
                throw new jakarta.persistence.EntityNotFoundException("User not found with email " + email);
            }
            UserRole role = UserRole.valueOf("People Group".equals(attendanceList.get(0).getEmployeeFunction())
                    ? "PEOPLE_GROUP" : attendanceList.get(0).getEmployeeGrade());
            userModel = new UserModel(
                    null, attendanceList.get(0).getEmployeeName(), role, email, new ArrayList<>(), new ArrayList<>(), false, ACTIVE, new ArrayList<>(), false
            );
        }
        return userModel;
    }

    @Override
    public UserModel findUserByEmail(String email) {
        log.debug("Finding user with email: {}", email);
        User user =
                userRepository
                        .findByEmailIgnoreCaseAndStatus(email, ACTIVE)
                        .orElseThrow(() -> EntityNotFoundException.user("User not found with email " + email));
        UserModel userModel = userMapper.entityToModel(user);
        userModel.setCustomers(user.getTeams().stream().map(Team::getCustomer).toList());
        return userModel;
    }

    @Override
    public void saveUserLog(String email) {

        try {
            Optional<User> optionalUser = userRepository.findUserByEmail(email);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                Date date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
                List<UserLog> logs = userLogRepository.findAllByUserIdAndLogInDate(user.getId(), date);
                var userLogBuilder = UserLog.builder();
                if (!logs.isEmpty()) {
                    userLogBuilder.id(logs.get(0).getId());
                }

                UserLog log = userLogBuilder
                        .userId(user.getId())
                        .logInDate(date)
                        .build();
                userLogRepository.save(log);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }


    }

    @Override
    public UserListViewResponse findAllUsers(Specification<User> userSpecification, Pageable paging) {
        Page<User> pagedResult = userRepository.findAll(userSpecification, paging);

        UserListViewResponse userListViewResponse;
        if (pagedResult.hasContent()) {
            userListViewResponse = populateUserListViewResponse(pagedResult);
        } else {
            userListViewResponse = new UserListViewResponse();
            userListViewResponse.setTotalPages(pagedResult.getTotalPages());
            userListViewResponse.setTotalRecords(pagedResult.getTotalElements());
            userListViewResponse.setPage(pagedResult.getNumber());
            userListViewResponse.setSize(pagedResult.getSize());
            userListViewResponse.setHasNext(pagedResult.hasNext());
            userListViewResponse.setHasPrevious(pagedResult.hasPrevious());
            log.warn("No User(s) found");
        }
        return userListViewResponse;
    }

    private UserListViewResponse populateUserListViewResponse(Page<User> pagedResult) {
        UserListViewResponse userListViewResponse =
                new UserListViewResponse()
                        .setTotalPages(pagedResult.getTotalPages())
                        .setTotalRecords(pagedResult.getTotalElements())
                        .setPage(pagedResult.getNumber())
                        .setSize(pagedResult.getSize())
                        .setHasNext(pagedResult.hasNext())
                        .setHasPrevious(pagedResult.hasPrevious());

        List<User> userEntityList = pagedResult.getContent();
        List<UserModel> userList =
                userEntityList.stream()
                        .map(
                                user -> {
                                    var userModel = userMapper.entityToModel(user);
                                    userModel.setTeams(new HashSet<>(userModel.getTeams()).stream().toList());
                                    userModel.setTeamRoles(
                                            user.getUserTeamRoles().stream()
                                                    .map(userTeamRoles -> userTeamRolesMapper.entityToModel(userTeamRoles))
                                                    .toList());
//                                    userModel.setIsCustomerRole(
//                                            userModel.getTeamRoles().stream()
//                                                    .anyMatch(
//                                                            userTeamRolesModel ->
//                                                                    userTeamRolesModel.getUserRole().getRole().equals(UserRole.CUSTOMER)));
                                    return userModel;
                                })
                        .toList();

        userListViewResponse.setUserListViews(userList);
        return userListViewResponse;
    }


    @Override
    public List<Roles> getRoles() {
        return List.of();
    }

}
