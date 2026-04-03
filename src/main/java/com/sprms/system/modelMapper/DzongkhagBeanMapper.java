package com.sprms.system.modelMapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.sprms.system.frmbeans.DzongkhagmFrmBean;
import com.sprms.system.hbmbeans.DzongkhagM;



@Mapper(componentModel = "spring")
public interface DzongkhagBeanMapper {

	//form Entity to frmbean
	DzongkhagM toEntity(DzongkhagmFrmBean dzfrmbean);
	
	//from frmbean to entity
	DzongkhagmFrmBean toFrmBean(DzongkhagM entity);
	
    default List<DzongkhagmFrmBean> toFrmBean(List<DzongkhagM> entities) {
        if (entities == null) return null;
        return entities.stream()
                       .map(this::toFrmBean) // calls single-element mapping
                       .toList();
    }

    default List<DzongkhagM> toEntity(List<DzongkhagmFrmBean> frmBeans) {
        if (frmBeans == null) return null;
        return frmBeans.stream()
                       .map(this::toEntity)
                       .toList();
    }
	
	
	
}
