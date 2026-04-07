package com.sprms.system.master.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sprms.system.hbmbeans.FundingAgency;


@Repository
public interface FundingAgencyRepository extends JpaRepository<FundingAgency, Long> {

	 // You can add custom query methods if needed
}
