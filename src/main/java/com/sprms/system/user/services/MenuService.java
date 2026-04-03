package com.sprms.system.user.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.sprms.system.frmbeans.MenuDTO;
import com.sprms.system.hbmbeans.Menu;
import com.sprms.system.wrapper.ServiceResponse;

public interface MenuService {

	Menu saveMenu(Menu menu);

	ServiceResponse<Menu> saveMenus(Menu menu);

	List<Menu> getAllMenus();

	Menu getMenuById(Long id);

	void deleteMenu(Long id);

	// For dynamic role-based menu
	Set<Menu> getMenusByUser(Long userId);

	List<Menu> getMenusLstByUser(Long userId);

	List<MenuDTO> getMenusByUserId(Long userId);

	List<MenuDTO> buildMenuTree(List<Menu> menus);

	List<Menu> getMenusForUser(Long userId);

	List<MenuDTO> getSidebarMenus(Long userId);

	// menu display
	List<MenuDTO> getAllMenusTree();

	
}
