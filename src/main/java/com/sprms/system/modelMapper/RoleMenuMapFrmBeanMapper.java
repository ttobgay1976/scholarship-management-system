package com.sprms.system.modelMapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.sprms.system.frmbeans.RoleMenuMapFrmBean;
import com.sprms.system.hbmbeans.RoleMenuMap;

@Mapper(componentModel = "spring")
public interface RoleMenuMapFrmBeanMapper {

	// Map entity → FormBean
	RoleMenuMapFrmBean toFrmBean(RoleMenuMap entity);

	// Map FormBean → entity
	RoleMenuMap toEntity(RoleMenuMapFrmBean frmBean);

	default List<RoleMenuMapFrmBean> toFrmBean(List<RoleMenuMap> entities) {
		if (entities == null)
			return null;
		return entities.stream().map(this::toFrmBean) // calls single-element mapping
				.toList();
	}

	default List<RoleMenuMap> toEntity(List<RoleMenuMapFrmBean> frmBeans) {
		if (frmBeans == null)
			return null;
		return frmBeans.stream().map(this::toEntity).toList();
	}
}
