package com.sprms.system.master.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sprms.system.hbmbeans.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

}
