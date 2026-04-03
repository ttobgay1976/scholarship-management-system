package com.sprms.system.frmbeans;

import java.util.List;

public class RoleMenuDTO {

	private Long id;
	private Long roleId;
    private List<Long> menuIds;

    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RoleMenuDTO() {
    }

    public RoleMenuDTO(Long roleId, List<Long> menuIds) {
        this.roleId = roleId;
        this.menuIds = menuIds;
    }

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public List<Long> getMenuIds() {
		return menuIds;
	}

	public void setMenuIds(List<Long> menuIds) {
		this.menuIds = menuIds;
	}

    
}
