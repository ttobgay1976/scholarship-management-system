package com.sprms.system.modelMapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.sprms.system.frmbeans.MenuFrmBean;
import com.sprms.system.hbmbeans.Menu;

@Mapper(componentModel = "spring")
public interface MenuFrmBeanMapper {

	//entity to frmbean
		Menu toEntity(MenuFrmBean frmbean);
		
		//frmbean to entity
	    @Mapping(target = "parentId", source = "parent.id")
	    @Mapping(target = "parentName", source = "parent.menuName")
		MenuFrmBean toFrmBean(Menu entity);
		
	    default List<MenuFrmBean> toFrmBean(List<Menu> entities) {
	        if (entities == null) return null;
	        return entities.stream()
	                       .map(this::toFrmBean) // calls single-element mapping
	                       .toList();
	    }

	    default List<Menu> toEntity(List<MenuFrmBean> frmBeans) {
	        if (frmBeans == null) return null;
	        return frmBeans.stream()
	                       .map(this::toEntity)
	                       .toList();
	    }
}
