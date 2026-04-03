package com.sprms.system.modelMapper;

import org.mapstruct.Mapper;

import com.sprms.system.frmbeans.GewogmFrmBean;
import com.sprms.system.hbmbeans.GewogM;


@Mapper(componentModel = "spring")
public interface GewogBeanMapper {

	//Entity to formBean
	GewogM toEntity(GewogmFrmBean gwfrmbean);
	
	//frmbean to entity
	GewogmFrmBean toFrmBean(GewogM entity);
	
	
}
