package com.sprms.system.modelMapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.sprms.system.frmbeans.CollegeFrmBean;
import com.sprms.system.hbmbeans.College;

@Mapper(componentModel = "spring")
public interface CollegeFrmBeanMapper {

    // Map entity → FormBean
    @Mapping(target = "countryId", source = "country.id")
    @Mapping(target = "stateId", source = "state.id")
    @Mapping(target = "cityId", source = "city.id")
    CollegeFrmBean toFrmBean(College entity);

    // Map FormBean → entity
    @Mapping(target = "country", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "city", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    College toEntity(CollegeFrmBean collegeFrmBean);

	    
		
	    default List<CollegeFrmBean> toFrmBean(List<College> entities) {
	        if (entities == null) return null;
	        return entities.stream()
	                       .map(this::toFrmBean) // calls single-element mapping
	                       .toList();
	    }

	    default List<College> toEntity(List<CollegeFrmBean> frmBeans) {
	        if (frmBeans == null) return null;
	        return frmBeans.stream()
	                       .map(this::toEntity)
	                       .toList();
	    }
}
