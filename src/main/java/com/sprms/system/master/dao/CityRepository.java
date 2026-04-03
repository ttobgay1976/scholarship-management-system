package com.sprms.system.master.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sprms.system.hbmbeans.Cities;

@Repository
public interface CityRepository extends JpaRepository<Cities, Long> {
	/* List<Cities> findByStateId(Long Id); */
    
    List<Cities> findByStateId(Long stateId);

}
