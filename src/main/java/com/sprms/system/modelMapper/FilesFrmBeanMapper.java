package com.sprms.system.modelMapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.sprms.system.frmbeans.FilesFrmBean;
import com.sprms.system.hbmbeans.FloodFiles;



@Mapper(componentModel = "spring")
public interface FilesFrmBeanMapper {

	FloodFiles toEntity(FilesFrmBean frmbean);
	
	FilesFrmBean toFrmBean(FloodFiles entity);
	
	
    default List<FilesFrmBean> toFrmBean(List<FloodFiles> entities) {
        if (entities == null) return null;
        return entities.stream()
                       .map(this::toFrmBean) // calls single-element mapping
                       .toList();
    }

    default List<FloodFiles> toEntity(List<FilesFrmBean> frmBeans) {
        if (frmBeans == null) return null;
        return frmBeans.stream()
                       .map(this::toEntity)
                       .toList();
    }
}
