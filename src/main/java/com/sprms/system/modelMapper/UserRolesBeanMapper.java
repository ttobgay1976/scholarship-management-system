package com.sprms.system.modelMapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.sprms.system.frmbeans.UserRolesFrmBean;
import com.sprms.system.hbmbeans.UserRoles;


@Mapper(componentModel = "spring")
public interface UserRolesBeanMapper {

	//entity to frmbean
	UserRoles toEntity(UserRolesFrmBean userRolesfrmbean);
	
	//frmbean to entity
	UserRolesFrmBean toFrmBean(UserRoles entity);
	
	/*
	 * List<RoleFrmBean> toFrmBean(List<Role> entities);
	 * 
	 * List<Role> toEntity(List<RoleFrmBean> frmBeans);
	 */
	
    default List<UserRolesFrmBean> toFrmBean(List<UserRoles> entities) {
        if (entities == null) return null;
        return entities.stream()
                       .map(this::toFrmBean) // calls single-element mapping
                       .toList();
    }

    default List<UserRoles> toEntity(List<UserRolesFrmBean> frmBeans) {
        if (frmBeans == null) return null;
        return frmBeans.stream()
                       .map(this::toEntity)
                       .toList();
    }
}
