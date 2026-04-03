package com.sprms.system.user.services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sprms.system.hbmbeans.Menu;
import com.sprms.system.hbmbeans.Role;
import com.sprms.system.hbmbeans.RoleMenuMap;
import com.sprms.system.user.dao.RoleMenuMapRepository;

import jakarta.persistence.EntityManager;

@Service
public class RoleMenuMapServices {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(RoleMenuMapServices.class);
	
	//call repo
	private final RoleMenuMapRepository _roleMenuMapRepository;
    
	
	//constructor
	public RoleMenuMapServices(RoleMenuMapRepository roleMenuMapRepository) {
		this._roleMenuMapRepository=roleMenuMapRepository;
	}
	
	 /**
     * Get all Menu entities assigned to a Role
     */
    public Set<Menu> getMenusByRole(Long roleId) {
        // Fetch all role-menu mappings for the role
        List<RoleMenuMap> roleMenus = _roleMenuMapRepository.findByRoleId(roleId);

        // Extract the Menu from each mapping
        return roleMenus.stream()
                .map(RoleMenuMap::getMenu)  // ✅ uses the getter
                .collect(Collectors.toSet());
        
    }

    /**
     * Assign menus to a role
     */
    public void assignMenusToRole(Long roleId, Set<Menu> menus) {
        // Delete existing mappings
        List<RoleMenuMap> existing = _roleMenuMapRepository.findByRoleId(roleId);
        _roleMenuMapRepository.deleteAll(existing);

        // Add new mappings
        Role role = new Role();
        role.setRoleId(roleId);

        for (Menu menu : menus) {
            RoleMenuMap mapping = new RoleMenuMap();
            mapping.setRole(role);
            mapping.setMenu(menu);
            _roleMenuMapRepository.save(mapping);
        }
    }

}
