package com.sprms.system.user.dao;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sprms.system.frmbeans.MenuDTO;
import com.sprms.system.hbmbeans.Menu;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

	@Query("""
			    SELECT DISTINCT m
			    FROM UserRoles ur
			    JOIN ur.role r
			    JOIN r.menus m
			    WHERE ur.user.id = :userId
			    ORDER BY m.displayOrder ASC
			""")
	List<Menu> findMenusByUserId(@Param("userId") Long userId);

}
