package com.sprms.system.core.servicesdao;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sprms.system.applicationEnums.ApplicationStatus;
import com.sprms.system.hbmbeans.ScholarshipRegistration;

//Created 13/04/2026
//place :YK Office

@Repository
public interface ScholarshipRegistrationRepository extends JpaRepository<ScholarshipRegistration, Long> {

	//this is repository for Scholarship Registration
	Page<ScholarshipRegistration> findByStatus(ApplicationStatus status,Pageable pageable);
	
	 List<ScholarshipRegistration> findByCitizenId(String citizenId);
	
}
