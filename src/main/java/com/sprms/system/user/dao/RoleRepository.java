package com.sprms.system.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sprms.system.hbmbeans.Role;



@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}
