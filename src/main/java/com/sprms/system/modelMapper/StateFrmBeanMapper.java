package com.sprms.system.modelMapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.sprms.system.frmbeans.StateDTO;
import com.sprms.system.hbmbeans.State;

@Mapper(componentModel = "spring")
public interface StateFrmBeanMapper {

	//Entity to formBean
	@Mapping(target = "country", ignore = true)
	@Mapping(target = "cities", ignore = true)
	State toEntity(StateDTO frmbean);
	
	//frmbean to entity
	@Mapping(target = "countryId", source = "country.id")
	StateDTO toFrmBean(State entity);
}
