package com.sprms.system.core.servicesdao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sprms.system.hbmbeans.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

	// Find student by CID number
	@Query("SELECT s FROM Student s WHERE s.cidNumber = :cidNumber")
	Optional<Student> findByUserCidNo(@Param("cidNumber") String cidNumber);

	// Find student by email address
	Optional<Student> findByEmailAddress(String emailAddress);

	// Find student by student ID number
	Optional<Student> findByStudentIdNumber(String studentIdNumber);

	// Check if CID number exists
	@Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Student s WHERE s.cidNumber = :cidNumber")
	boolean existsByUserCidNo(@Param("cidNumber") String cidNumber);

	// Check if email address exists
	boolean existsByEmailAddress(String emailAddress);

	// Find students by college name
	List<Student> findByCollegeName(String collegeName);

	// Find students by program/course
	List<Student> findByProgramCourse(String programCourse);

	// Find students by funding type
	List<Student> findByFundingType(String fundingType);

	// Find active students
	List<Student> findByIsActiveTrue();

	// Find students by name (first or last name)
	@Query("SELECT s FROM Student s WHERE " +
		   "LOWER(s.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
		   "LOWER(s.lastName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
		   "LOWER(s.fullName) LIKE LOWER(CONCAT('%', :name, '%'))")
	List<Student> findByNameContaining(@Param("name") String name);

	// Find students by college and program
	List<Student> findByCollegeNameAndProgramCourse(String collegeName, String programCourse);

	// Count active students by college
	@Query("SELECT COUNT(s) FROM Student s WHERE s.collegeName = :collegeName AND s.isActive = true")
	Long countActiveStudentsByCollege(@Param("collegeName") String collegeName);

	// Find students by enrollment date range
	@Query("SELECT s FROM Student s WHERE s.enrollmentDate BETWEEN :startDate AND :endDate")
	List<Student> findByEnrollmentDateBetween(@Param("startDate") java.time.LocalDateTime startDate, 
											  @Param("endDate") java.time.LocalDateTime endDate);

	// Search students with multiple criteria
	@Query("SELECT s FROM Student s WHERE " +
		   "(:cid IS NULL OR s.cidNumber = :cid) AND " +
		   "(:college IS NULL OR s.collegeName = :college) AND " +
		   "(:program IS NULL OR s.programCourse = :program) AND " +
		   "(:fundingType IS NULL OR s.fundingType = :fundingType) AND " +
		   "(:isActive IS NULL OR s.isActive = :isActive)")
	List<Student> searchStudents(@Param("cid") String cid, 
								  @Param("college") String college,
								  @Param("program") String program,
								  @Param("fundingType") String fundingType,
								  @Param("isActive") Boolean isActive);

	// Get student statistics
	@Query("SELECT " +
		   "COUNT(s) as totalStudents, " +
		   "COUNT(CASE WHEN s.isActive = true THEN 1 END) as activeStudents, " +
		   "COUNT(CASE WHEN s.fundingType = 'GOVERNMENT_FUNDED' THEN 1 END) as governmentFunded, " +
		   "COUNT(CASE WHEN s.fundingType = 'PRIVATELY_FUNDED' THEN 1 END) as privatelyFunded " +
		   "FROM Student s")
	Object[] getStudentStatistics();

	// Find students with similar names (for duplicate detection)
	@Query("SELECT s FROM Student s WHERE " +
		   "LOWER(s.firstName) = LOWER(:firstName) AND " +
		   "LOWER(s.lastName) = LOWER(:lastName) AND " +
		   "s.studentId != :excludeId")
	List<Student> findSimilarNames(@Param("firstName") String firstName, 
									@Param("lastName") String lastName,
									@Param("excludeId") Long excludeId);
}
