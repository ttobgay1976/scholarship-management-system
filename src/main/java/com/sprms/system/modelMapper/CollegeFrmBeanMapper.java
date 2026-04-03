package com.sprms.system.modelMapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.sprms.system.frmbeans.CollegeFrmBean;
import com.sprms.system.hbmbeans.College;

@Mapper(componentModel = "spring")
public interface CollegeFrmBeanMapper {


	    // Map entity → FormBean
//	    @Mapping(target = "stateName", source = "state.stateName")
	    CollegeFrmBean toFrmBean(College entity);

	    // Map FormBean → entity
//	    @Mapping(target = "state", ignore = true) // map separately if needed
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
