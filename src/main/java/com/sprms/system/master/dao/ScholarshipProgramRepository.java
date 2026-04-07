package com.sprms.system.master.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sprms.system.hbmbeans.ScholarshipProgram;

@Repository
public interface ScholarshipProgramRepository extends JpaRepository<ScholarshipProgram, Long> {

}
