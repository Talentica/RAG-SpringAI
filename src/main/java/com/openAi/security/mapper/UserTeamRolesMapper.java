package com.openAi.security.mapper;

import com.openAi.security.entity.UserTeamRoles;
import com.openAi.security.model.UserTeamRolesModel;
import org.mapstruct.Mapper;

@Mapper
public interface UserTeamRolesMapper {
    UserTeamRolesModel entityToModel(UserTeamRoles userTeamRoles);

    UserTeamRoles modelToEntity(UserTeamRolesModel userTeamRolesModel);
}
