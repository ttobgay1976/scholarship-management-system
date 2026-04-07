package com.sprms.system.user.dao;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sprms.system.hbmbeans.Menu;
import com.sprms.system.hbmbeans.Role;
import com.sprms.system.hbmbeans.RoleMenuMap;

@Repository
public interface RoleMenuMapRepository extends JpaRepository<RoleMenuMap, Long> {


	@Query("SELECT rmm FROM RoleMenuMap rmm WHERE rmm.role.roleId = :roleId")
	List<RoleMenuMap> findByRoleId(@Param("roleId") Long roleId);

	//This code is add to fetch menu by RoleID from the RoleMenuMAp table
	//crated on dt 29/03/2026
	//Place: Home
	@Query("SELECT rmm.menu FROM RoleMenuMap rmm WHERE rmm.role.roleId = :roleId")
	List<Menu> findMenusByRoleId(@Param("roleId") Long roleId);

	void deleteByRole(Role role);
	
	List<RoleMenuMap> findByRole(Role role);

}
