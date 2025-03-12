package com.openAi.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper {
    @Mapping(source = "userTeamRoles", target = "teamRoles")
    UserModel entityToModel(User user);

    User modelToEntity(UserModel user);
}
