package com.sprms.system.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sprms.system.hbmbeans.SupportingFiles;

@Repository
public interface SupportingFilesRepository extends JpaRepository<SupportingFiles, Long> {

	// add anyother method here

	List<SupportingFiles> findByScholarshipRegistrationId(Long applicationId);

}
