package com.openAi.security.service;

import com.openAi.security.repository.AttendanceRepository;
import com.openAi.security.repository.CustomerCredentialRepository;
import com.openAi.security.repository.UserLogRepository;
import com.openAi.security.repository.UserTeamRoleRepository;
import com.openAi.security.entity.Attendance;
import com.openAi.security.enums.UserRole;
import com.openAi.security.mapper.UserMapper;
import com.openAi.security.model.UserModel;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.openAi.security.enums.EntityStatus.ACTIVE;

/**
 * UserService Implementation.
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    private final String LOGIN = "login";

/*    @Autowired
    UserRepository userRepository;*/
    @Autowired
UserTeamRoleRepository userTeamRoleRepository;
    @Autowired
    AttendanceRepository attendanceRepository;
    @Autowired
    CustomerCredentialRepository customerCredentialRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Value("${customer-login.site-url}")
    private String siteURL;

    @Value("${customer-login.login-endpoint}")
    private String loginEndPoint;
    @Autowired
    private UserLogRepository userLogRepository;

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
        /*log.debug("Finding user with email: {}", email);
        User user =
                userRepository
                        .findByEmailIgnoreCaseAndStatus(email, ACTIVE)
                        .orElseThrow(() -> EntityNotFoundException.user("User not found with email " + email));
        UserModel userModel = userMapper.entityToModel(user);
        userModel.setCustomers(user.getTeams().stream().map(Team::getCustomer).toList());
        return userModel;*/
        return null;
    }

    @Override
    public void saveUserLog(String email) {
/*
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
*/

    }

}
