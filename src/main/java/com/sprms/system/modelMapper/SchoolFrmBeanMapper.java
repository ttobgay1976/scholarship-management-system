package com.sprms.system.modelMapper;

import java.util.List;

import org.mapstruct.Mapper;
import com.sprms.system.frmbeans.SchoolFrmBean;
import com.sprms.system.hbmbeans.Schools;

@Mapper(componentModel = "spring")
public interface SchoolFrmBeanMapper {


    // Map entity → FormBean
//    @Mapping(target = "stateName", source = "state.stateName")
    SchoolFrmBean toFrmBean(Schools entity);

    // Map FormBean → entity
//    @Mapping(target = "state", ignore = true) // map separately if needed
    Schools toEntity(SchoolFrmBean FrmBean);

    
	
    default List<SchoolFrmBean> toFrmBean(List<Schools> entities) {
        if (entities == null) return null;
        return entities.stream()
                       .map(this::toFrmBean) // calls single-element mapping
                       .toList();
    }

    default List<Schools> toEntity(List<SchoolFrmBean> frmBeans) {
        if (frmBeans == null) return null;
        return frmBeans.stream()
                       .map(this::toEntity)
                       .toList();
    }
}
