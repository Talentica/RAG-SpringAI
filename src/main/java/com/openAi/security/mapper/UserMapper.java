package com.openAi.security.mapper;


import com.openAi.security.entity.User;
import com.openAi.security.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/** Entity-Model mapper for User. */
@Mapper
public interface UserMapper {
  @Mapping(source = "userTeamRoles", target = "teamRoles")
  UserModel entityToModel(User user);

  User modelToEntity(UserModel user);
}
