package com.sprms.system.master.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sprms.system.hbmbeans.Schools;

@Repository
public interface SchoolsRepository extends JpaRepository<Schools, Long> {

	Optional<Schools> findById(Long Id);
	
	Page<Schools> findAll(Pageable pageable);
}
