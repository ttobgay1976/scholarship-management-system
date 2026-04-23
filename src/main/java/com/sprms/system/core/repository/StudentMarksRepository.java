package com.sprms.system.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sprms.system.hbmbeans.StudentMarks;

@Repository
public interface StudentMarksRepository extends JpaRepository<StudentMarks, Long> {

	//add other interface
	List<StudentMarks> findStudentMarksByIndexNumber(String indexno);
}
