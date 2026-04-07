package com.sprms.system.user.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sprms.system.hbmbeans.UserRoles;

public interface UserRoleRepository extends JpaRepository<UserRoles, Long> {

	List<UserRoles> findByUserId(Long Id);

	void deleteByUserId(Long Id);

	// Optional: check if mapping exists
	boolean existsByUserIdAndRole_RoleId(Long userId, Long roleId);
	
    @Query("SELECT ur.role.id FROM UserRoles ur WHERE ur.user.id = :userId")
    List<Long> findRoleIdsByUserId(@Param("userId") Long userId);
    

}
