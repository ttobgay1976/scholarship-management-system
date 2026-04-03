package com.sprms.system.frmbeans;

import jakarta.persistence.Column;
import jakarta.persistence.Id;

public class RoleMenuMapFrmBean {

	@Id
	private Integer roleId;

	private Integer menuId;

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}
	
	
}
