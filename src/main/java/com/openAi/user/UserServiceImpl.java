package com.openAi.user;

import com.openAi.Attendance.Attendance;
import com.openAi.Attendance.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.openAi.team.Team;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static com.openAi.security.enums.EntityStatus.ACTIVE;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;

    @Override
    public UserModel findUserByEmail(String email) {
        log.debug("Finding user with email: {}", email);
        User user =
                userRepository
                        .findByEmailIgnoreCaseAndStatus(email, ACTIVE)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with email " + email));
        UserModel userModel = userMapper.entityToModel(user);
        userModel.setCustomers(user.getTeams().stream().map(Team::getCustomer).toList());
        return userModel;
    }

    @Override
    public UserModel findUserByEmailOrAttendance(String email) {
        UserModel userModel = null;
        try{
            userModel = findUserByEmail(email);
        }
        catch (Exception e){
            List<Attendance> attendanceList = attendanceRepository.findByEmailIdOrderByYearDescMonthDesc(email);
            if (attendanceList == null || attendanceList.isEmpty()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with email " + email);
            }
            UserRole role = UserRole.valueOf("People Group".equals(attendanceList.get(0).getEmployeeFunction())
                    ? "PEOPLE_GROUP" : attendanceList.get(0).getEmployeeGrade());
            userModel = new UserModel(
                    null, attendanceList.get(0).getEmployeeName(), role, email, new ArrayList<>(), new ArrayList<>(),false, ACTIVE, new ArrayList<>(), false
            );
        }
        return userModel;
    }

}
