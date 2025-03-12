package com.openAi.user;


public interface UserService {

    UserModel findUserByEmail(String email);

    UserModel findUserByEmailOrAttendance(String email);

}