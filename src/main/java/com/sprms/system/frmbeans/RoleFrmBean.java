package com.sprms.system.frmbeans;

import java.util.HashSet;
import java.util.Set;

import com.sprms.system.hbmbeans.Menu;
import com.sprms.system.hbmbeans.UserRoles;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

public class RoleFrmBean {
	

	private Long roleId;
	private String roleName; // ROLE_USER, ROLE_ADMIN

	// Bidirectional relationship to UserRoles
	@OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
	private Set<UserRoles> userRoles = new HashSet<>();

	// Link to menus
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "tbl_role_menu_map", joinColumns = @JoinColumn(name = "ROLE_ID"), inverseJoinColumns = @JoinColumn(name = "id"))
	private Set<Menu> menus = new HashSet<>();

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Set<UserRoles> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(Set<UserRoles> userRoles) {
		this.userRoles = userRoles;
	}

	public Set<Menu> getMenus() {
		return menus;
	}

	public void setMenus(Set<Menu> menus) {
		this.menus = menus;
	}

        

}
