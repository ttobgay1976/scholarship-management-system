package com.sprms.system.master.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sprms.system.hbmbeans.State;

@Repository
public interface StateRepository extends JpaRepository<State, Long> {

	List<State> findByCountryId(Long countryId);
}
