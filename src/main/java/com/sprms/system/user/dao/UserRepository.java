package com.sprms.system.user.dao;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sprms.system.hbmbeans.Role;
import com.sprms.system.hbmbeans.User;
import com.sprms.system.hbmbeans.UserRoles;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findByUsername(String username);
	
	/* boolean existsByEmail(String email); */
    boolean existsByUsername(String username);
    
	/* Set<Role> getRolesByUsername(String username); */
    
	/* List<UserRoles> getRolesById(Long userId); */
    

}
