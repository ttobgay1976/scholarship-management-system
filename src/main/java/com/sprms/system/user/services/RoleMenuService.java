package com.sprms.system.user.services;

import java.util.List;
import java.util.Set;


import com.sprms.system.hbmbeans.Menu;
import com.sprms.system.hbmbeans.Role;

public interface RoleMenuService {

	Role assignMenusToRole(Long roleId, List<Long> menuIds);

	Role updateRoleMenus(Long roleId, List<Long> menuIds);

	Role removeMenusFromRole(Long roleId, List<Long> menuIds);
}
