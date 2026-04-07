package com.sprms.system.user.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sprms.system.hbmbeans.Menu;
import com.sprms.system.hbmbeans.Role;
import com.sprms.system.hbmbeans.RoleMenuMap;
import com.sprms.system.user.dao.MenuRepository;
import com.sprms.system.user.dao.RoleMenuMapRepository;
import com.sprms.system.user.dao.RoleRepository;
import com.sprms.system.utils.DateUtil;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RoleMenuServiceImpl implements RoleMenuService {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(RoleMenuServiceImpl.class);

	// call Repository
	private final RoleRepository _roleRepository;
	private final MenuRepository _menuRepository;
	private final RoleMenuMapRepository _roleMenuMapRepository;
	
	//constructor
	public RoleMenuServiceImpl(RoleRepository roleRepository, MenuRepository menuRepository, RoleMenuMapRepository roleMenuMapRepository) {
		this._menuRepository=menuRepository;
		this._roleRepository=roleRepository;
		this._roleMenuMapRepository=roleMenuMapRepository;
	}

	// ✅ Assign menus (add)
	@Override
	public Role assignMenusToRole(Long roleId, List<Long> menuIds) {
		
		logger.info("@@@Calling assignMenusToRole proc..................");

		Role role = _roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found"));

		List<Menu> menus = _menuRepository.findAllById(menuIds);

		role.getMenus().addAll(menus); // Set avoids duplicates

		return _roleRepository.save(role);
	}

	//Replace all menus (important for UI checkbox save)
	@Override
	public Role updateRoleMenus(Long roleId, List<Long> menuIds) {
		
		 logger.info("@@@Calling updateRoleMenus proc..................");

		    // 1️⃣ Fetch role
		    
		    Role role = _roleRepository.findById(roleId)
		            .orElseThrow(() -> new RuntimeException("Role not found"));

		    List<RoleMenuMap> existingMappings = _roleMenuMapRepository.findByRole(role);
		    

		    Set<Long> existingMenuIds = existingMappings.stream()
		            .map(rm -> rm.getMenu().getId())
		            .collect(Collectors.toSet());

		    Set<Long> newMenuIds = menuIds != null ? new HashSet<>(menuIds) : new HashSet<>();

		    // 3️⃣ Delete unchecked menus
		    for (RoleMenuMap rm : existingMappings) {
		        if (!newMenuIds.contains(rm.getMenu().getId())) {
		            _roleMenuMapRepository.delete(rm);
		        }
		    }

		    // 4️⃣ Fetch all menus for new checked menuIds
		    List<Menu> menus = _menuRepository.findAllById(newMenuIds);

		    // 5️⃣ Save newly checked menus (if not already existing)
		    for (Menu menu : menus) {
		        if (!existingMenuIds.contains(menu.getId())) {
		            RoleMenuMap rm = new RoleMenuMap();
		            rm.setRole(role);
		            rm.setMenu(menu);
		            _roleMenuMapRepository.save(rm);
		        }
		    }

		    return role;
	}

	// ✅ Remove selected menus
	/*
	 * @Override public Role removeMenusFromRole(Long roleId, List<Long> menuIds) {
	 * 
	 * logger.info("@@@Calling removeMenusFromRole proc..................");
	 * 
	 * Role role = _roleRepository.findById(roleId).orElseThrow(() -> new
	 * RuntimeException("Role not found"));
	 * 
	 * List<Menu> menus = _menuRepository.findAllById(menuIds);
	 * 
	 * role.getMenus().removeAll(menus);
	 * 
	 * return _roleRepository.save(role); }
	 */
	
}
