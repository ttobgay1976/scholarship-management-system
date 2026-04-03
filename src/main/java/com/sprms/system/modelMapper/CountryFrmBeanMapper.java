package com.sprms.system.modelMapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.sprms.system.frmbeans.CountryFrmBean;
import com.sprms.system.hbmbeans.Country;

@Mapper(componentModel = "spring")
public interface CountryFrmBeanMapper {

	
	//form Entity to frmbean
	Country toEntity(CountryFrmBean frmbean);
	
	//from frmbean to entity
	CountryFrmBean toFrmBean(Country entity);
	
    default List<CountryFrmBean> toFrmBean(List<Country> entities) {
        if (entities == null) return null;
        return entities.stream()
                       .map(this::toFrmBean) // calls single-element mapping
                       .toList();
    }

    default List<Country> toEntity(List<CountryFrmBean> frmBeans) {
        if (frmBeans == null) return null;
        return frmBeans.stream()
                       .map(this::toEntity)
                       .toList();
    }
}
