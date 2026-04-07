package com.sprms.system.user.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sprms.system.hbmbeans.Role;
import com.sprms.system.hbmbeans.User;
import com.sprms.system.hbmbeans.UserRoles;
import com.sprms.system.user.dao.RoleRepository;
import com.sprms.system.user.dao.UserRepository;
import com.sprms.system.user.dao.UserRoleRepository;
import com.sprms.system.utils.DateUtil;
import com.sprms.system.wrapper.ServiceResponse;

@Service
public class UserRolesServices {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(UserRolesServices.class);

	private final UserRoleRepository userRoleRepository;
	private final RoleRepository roleRepository;
	private final UserRepository userRepository;

	public UserRolesServices(UserRoleRepository userRoleRepository, RoleRepository roleRepository,
			UserRepository userRepository) {
		this.userRoleRepository = userRoleRepository;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}

	// save the User Role
	@Transactional
	public ServiceResponse<UserRoles> saveUserRoles(UserRoles userRoles) {

		logger.info("@@@Calling User Role save serive.................");

		try {

			UserRoles saveRoles = userRoleRepository.saveAndFlush(userRoles);

			return new ServiceResponse<UserRoles>(true, "Information save successfully", saveRoles);

		} catch (Exception e) {
			// TODO: handle exception
			return new ServiceResponse<UserRoles>(false, "Information save successfully", null);
		}
	}	
	
	@Transactional
	public ServiceResponse<List<UserRoles>> assignRoles_Old(Long userId, List<Long> roleIds) {
	    try {
	    	User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

	        // 1️⃣ Delete old roles
	        userRoleRepository.deleteById(userId);

	        System.out.println("@@@ User id from form: " + userId);

	        // 2️⃣ Insert new roles
	        List<UserRoles> savedRoles = new ArrayList<>();

	        for (Long roleId : roleIds) {
	            Role role = roleRepository.findById(roleId)
	                                      .orElseThrow(() -> new RuntimeException("Role not found"));

	            UserRoles mapping = new UserRoles();
	            mapping.setUser(user);
	            mapping.setRole(role);
//	            mapping.setId(Long.parseLong(DateUtil.getUniqueID()));

	            UserRoles saved = userRoleRepository.save(mapping);
	            savedRoles.add(saved);

	            System.out.println("@@@ Role id saved: " + role.getRoleId());
	            
	        }

	        return new ServiceResponse<List<UserRoles>>(true, "Information saved successfully", savedRoles);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ServiceResponse<List<UserRoles>>(false, "Failed to save information", null);
	    }
	}
	

	//save one user with multiple Roles
	//created on dt 30/03/2026
	public List<UserRoles> assignRolesToUser(Long userId, List<Long> roleIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<UserRoles> savedRoles = new ArrayList<>();

        for (Long roleId : roleIds) {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new RuntimeException("Role not found: " + roleId));

            // Skip if role already assigned
            if (!userRoleRepository.existsByUserIdAndRole_RoleId(userId, roleId)) {
                UserRoles userRole = new UserRoles();
                userRole.setUser(user);
                userRole.setRole(role);
                savedRoles.add(userRoleRepository.save(userRole));
            }
        }

        return savedRoles;
    }
	
	// get the role by taking the user name /userID
	//New
	public List<Long> getAssignedRoleIds(Long userId) {
		
		logger.info("@@@Calling the getAssignedRoleIds.................");

		// 🔍 Validate user exists (recommended)
		if (!userRepository.existsById(userId)) {
			throw new RuntimeException("User not found with id: " + userId);
		}
		
		// 📥 Fetch assigned role IDs
		List<Long> roleIds = userRoleRepository.findRoleIdsByUserId(userId);

		// 🛡️ Null safety (just in case)
		return roleIds != null ? roleIds : new ArrayList<>();
	}
}
