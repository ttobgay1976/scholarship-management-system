package com.sprms.system.modelMapper;


import java.util.List;

import org.mapstruct.Mapper;

import com.sprms.system.frmbeans.RoleFrmBean;
import com.sprms.system.hbmbeans.Role;


@Mapper(componentModel = "spring")
public interface RolesBeanMapper {

	//entity to frmbean
	Role toEntity(RoleFrmBean rolefrmbean);
	
	//frmbean to entity
	RoleFrmBean toFrmBean(Role entity);
	
	/*
	 * List<RoleFrmBean> toFrmBean(List<Role> entities);
	 * 
	 * List<Role> toEntity(List<RoleFrmBean> frmBeans);
	 */
	
    default List<RoleFrmBean> toFrmBean(List<Role> entities) {
        if (entities == null) return null;
        return entities.stream()
                       .map(this::toFrmBean) // calls single-element mapping
                       .toList();
    }

    default List<Role> toEntity(List<RoleFrmBean> frmBeans) {
        if (frmBeans == null) return null;
        return frmBeans.stream()
                       .map(this::toEntity)
                       .toList();
    }
}
