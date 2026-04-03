package com.sprms.system.modelMapper;

import org.mapstruct.Mapper;

import com.sprms.system.frmbeans.StateDTO;
import com.sprms.system.hbmbeans.State;

@Mapper(componentModel = "spring")
public interface StateFrmBeanMapper {

	//Entity to formBean
	State toEntity(StateDTO frmbean);
	
	//frmbean to entity
	StateDTO toFrmBean(State entity);
}
