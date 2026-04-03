package com.sprms.system.modelMapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.sprms.system.frmbeans.UserFormBean;
import com.sprms.system.hbmbeans.User;


@Mapper(componentModel = "spring")
public interface UserFrmBeanMapper {

	//entity to frmbean
	User toEntity(UserFormBean userFormBean);
	
	//frmbean to entity	
	UserFormBean toFrmBean(User entity);
	
	
    default List<UserFormBean> toFrmBean(List<User> entities) {
        if (entities == null) return null;
        return entities.stream()
                       .map(this::toFrmBean) // calls single-element mapping
                       .toList();
    }

    default List<User> toEntity(List<UserFormBean> frmBeans) {
        if (frmBeans == null) return null;
        return frmBeans.stream()
                       .map(this::toEntity)
                       .toList();
    }
}
