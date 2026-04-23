package com.sprms.system.core.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sprms.system.core.servicesdao.StudentRepository;
import com.sprms.system.hbmbeans.Student;

@Service
@Transactional
public class StudentService {

	private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

	@Autowired
	private StudentRepository studentRepository;

	// Create new student
	public Student createStudent(Student student) {
		try {
			// Validate CID uniqueness
			if (studentRepository.existsByUserCidNo(student.getCidNumber())) {
				throw new RuntimeException("Student with CID " + student.getCidNumber() + " already exists");
			}

			// Validate email uniqueness
			if (studentRepository.existsByEmailAddress(student.getEmailAddress())) {
				throw new RuntimeException("Student with email " + student.getEmailAddress() + " already exists");
			}

			// Update full name if not set
			if (student.getFullName() == null || student.getFullName().trim().isEmpty()) {
				student.updateFullName();
			}

			// Set timestamps
			student.setCreatedAt(LocalDateTime.now());
			student.setUpdatedAt(LocalDateTime.now());

			Student savedStudent = studentRepository.save(student);
			logger.info("Created new student: {}", savedStudent.getCidNumber());
			return savedStudent;

		} catch (Exception e) {
			logger.error("Error creating student: {}", e.getMessage());
			throw new RuntimeException("Failed to create student: " + e.getMessage());
		}
	}

	// Get student by ID
	public Optional<Student> getStudentById(Long studentId) {
		try {
			return studentRepository.findById(studentId);
		} catch (Exception e) {
			logger.error("Error getting student by ID {}: {}", studentId, e.getMessage());
			return Optional.empty();
		}
	}

	// Get student by CID number
	public Optional<Student> getStudentByCid(String cidNumber) {
		try {
			return studentRepository.findByUserCidNo(cidNumber);
		} catch (Exception e) {
			logger.error("Error getting student by CID {}: {}", cidNumber, e.getMessage());
			return Optional.empty();
		}
	}

	// Get student by email
	public Optional<Student> getStudentByEmail(String emailAddress) {
		try {
			return studentRepository.findByEmailAddress(emailAddress);
		} catch (Exception e) {
			logger.error("Error getting student by email {}: {}", emailAddress, e.getMessage());
			return Optional.empty();
		}
	}

	// Update student information
	public Student updateStudent(Long studentId, Student studentDetails) {
		try {
			Student existingStudent = studentRepository.findById(studentId)
					.orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));

			// Update fields
			existingStudent.setFirstName(studentDetails.getFirstName());
			existingStudent.setMiddleName(studentDetails.getMiddleName());
			existingStudent.setLastName(studentDetails.getLastName());
			existingStudent.setContactNumber(studentDetails.getContactNumber());
			existingStudent.setEmailAddress(studentDetails.getEmailAddress());
			existingStudent.setProgramCourse(studentDetails.getProgramCourse());
			existingStudent.setSemester(studentDetails.getSemester());
			existingStudent.setYearOfStudy(studentDetails.getYearOfStudy());
			existingStudent.setCollegeName(studentDetails.getCollegeName());
			existingStudent.setDepartment(studentDetails.getDepartment());
			existingStudent.setAddress(studentDetails.getAddress());
			existingStudent.setFundingType(studentDetails.getFundingType());
			existingStudent.setScholarshipType(studentDetails.getScholarshipType());
			existingStudent.setUpdatedAt(LocalDateTime.now());

			// Update full name
			existingStudent.updateFullName();

			Student updatedStudent = studentRepository.save(existingStudent);
			logger.info("Updated student: {}", updatedStudent.getCidNumber());
			return updatedStudent;

		} catch (RuntimeException e) {
			logger.error("Error updating student {}: {}", studentId, e.getMessage());
			throw new RuntimeException("Failed to update student: " + e.getMessage());
		} catch (Exception e) {
			logger.error("Error updating student {}: {}", studentId, e.getMessage());
			throw new RuntimeException("Failed to update student: " + e.getMessage());
		}
	}

	// Delete student (soft delete)
	public void deleteStudent(Long studentId) {
		try {
			Student student = studentRepository.findById(studentId)
					.orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));

			student.setIsActive(false);
			student.setUpdatedAt(LocalDateTime.now());
			studentRepository.save(student);

			logger.info("Soft deleted student: {}", student.getCidNumber());

		} catch (Exception e) {
			logger.error("Error deleting student {}: {}", studentId, e.getMessage());
			throw new RuntimeException("Failed to delete student: " + e.getMessage());
		}
	}

	// Get all active students
	public List<Student> getAllActiveStudents() {
		try {
			return studentRepository.findByIsActiveTrue();
		} catch (Exception e) {
			logger.error("Error getting active students: {}", e.getMessage());
			throw new RuntimeException("Failed to get active students: " + e.getMessage());
		}
	}

	// Search students by name
	public List<Student> searchStudentsByName(String name) {
		try {
			return studentRepository.findByNameContaining(name);
		} catch (Exception e) {
			logger.error("Error searching students by name {}: {}", name, e.getMessage());
			throw new RuntimeException("Failed to search students: " + e.getMessage());
		}
	}

	// Get students by college
	public List<Student> getStudentsByCollege(String collegeName) {
		try {
			return studentRepository.findByCollegeName(collegeName);
		} catch (Exception e) {
			logger.error("Error getting students by college {}: {}", collegeName, e.getMessage());
			throw new RuntimeException("Failed to get students by college: " + e.getMessage());
		}
	}

	// Get students by funding type
	public List<Student> getStudentsByFundingType(String fundingType) {
		try {
			return studentRepository.findByFundingType(fundingType);
		} catch (Exception e) {
			logger.error("Error getting students by funding type {}: {}", fundingType, e.getMessage());
			throw new RuntimeException("Failed to get students by funding type: " + e.getMessage());
		}
	}

	// Advanced search with multiple criteria
	public List<Student> advancedSearch(String cid, String college, String program, String fundingType, Boolean isActive) {
		try {
			return studentRepository.searchStudents(cid, college, program, fundingType, isActive);
		} catch (Exception e) {
			logger.error("Error in advanced search: {}", e.getMessage());
			throw new RuntimeException("Failed to perform advanced search: " + e.getMessage());
		}
	}

	// Get student statistics
	public Object[] getStudentStatistics() {
		try {
			return studentRepository.getStudentStatistics();
		} catch (Exception e) {
			logger.error("Error getting student statistics: {}", e.getMessage());
			return new Object[]{0L, 0L, 0L, 0L};
		}
	}

	// Check for duplicate students
	public boolean hasDuplicateStudent(String firstName, String lastName, Long excludeId) {
		try {
			List<Student> duplicates = studentRepository.findSimilarNames(firstName, lastName, excludeId);
			return !duplicates.isEmpty();
		} catch (Exception e) {
			logger.error("Error checking for duplicate students: {}", e.getMessage());
			return false;
		}
	}

	// Reactivate student
	public Student reactivateStudent(Long studentId) {
		try {
			Student student = studentRepository.findById(studentId)
					.orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));

			student.setIsActive(true);
			student.setUpdatedAt(LocalDateTime.now());

			Student reactivatedStudent = studentRepository.save(student);
			logger.info("Reactivated student: {}", reactivatedStudent.getCidNumber());
			return reactivatedStudent;

		} catch (Exception e) {
			logger.error("Error reactivating student {}: {}", studentId, e.getMessage());
			throw new RuntimeException("Failed to reactivate student: " + e.getMessage());
		}
	}

	// Bulk operations
	public void bulkCreateStudents(List<Student> students) {
		try {
			for (Student student : students) {
				createStudent(student);
			}
			logger.info("Bulk created {} students", students.size());
		} catch (Exception e) {
			logger.error("Error in bulk student creation: {}", e.getMessage());
			throw new RuntimeException("Failed to bulk create students: " + e.getMessage());
		}
	}

	// Export students data
	public List<Student> exportStudentsByCollege(String collegeName) {
		try {
			return studentRepository.findByCollegeName(collegeName);
		} catch (Exception e) {
			logger.error("Error exporting students for college {}: {}", collegeName, e.getMessage());
			throw new RuntimeException("Failed to export students: " + e.getMessage());
		}
	}
}
