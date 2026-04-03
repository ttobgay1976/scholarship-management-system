package com.sprms.system.user.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sprms.system.hbmbeans.Role;
import com.sprms.system.user.dao.RoleRepository;
import com.sprms.system.wrapper.ServiceResponse;

@Service
public class RoleServices {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(RoleServices.class);

	private final RoleRepository roleRepository;

	// constructor
	public RoleServices(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	// save role
	@Transactional
	public Role addRole(Role role) {

		logger.info("@@@Calling the save procedure..............");

		return roleRepository.saveAndFlush(role);
	}

	// using the save wrapper class
	@Transactional
	public ServiceResponse<Role> saveRole(Role role) {

		logger.info("@@@Calling the role service save proc----------------");

		try {
			// set the value to ID
			System.out.println("@@@Checking the ID value :" + role.getRoleId());
			System.out.println("@@@Checking the Role value :" + role.getRoleName());

			Role saveRole = roleRepository.saveAndFlush(role);

			return new ServiceResponse<Role>(true, "New Role Saved", saveRole);

		} catch (Exception e) {
			// TODO: handle exception
			return new ServiceResponse<Role>(false, "Failed to save information", null);
		}
	}

	@Transactional(readOnly = true)
	public List<Role> getRoles() {

		logger.info("@@@Calling the get roles procedure..................");

		return roleRepository.findAll();
	}

	// get role by Id
	@Transactional
	public Role getRoleById(Long roleId) {
		return roleRepository.findById(roleId)
				.orElseThrow(() -> new RuntimeException("Role not found with ID: " + roleId));
	}
}
