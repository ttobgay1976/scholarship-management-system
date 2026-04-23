package com.sprms.system.api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sprms.system.core.services.StudentService;
import com.sprms.system.hbmbeans.Student;
import com.sprms.system.core.servicesdao.StudentRepository;
@RestController
@RequestMapping("/api/students")
public class StudentController {

	private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

	@Autowired
	private StudentService studentService;
	
	@Autowired
	private StudentRepository studentRepository;

	// Search student by CID number
	@GetMapping("/search-by-cid/{cid}")
	public ResponseEntity<Map<String, Object>> searchStudentByCid(@PathVariable String cid) {
		try {
			logger.info("CID lookup request received for CID: {}", cid);
			
			// Validate CID format
			if (cid == null || cid.trim().isEmpty()) {
				logger.warn("CID validation failed: CID is null or empty");
				return ResponseEntity.badRequest().body(createErrorResponse("CID is required"));
			}

			if (cid.length() != 11 || !cid.matches("\\d+")) {
				logger.warn("CID validation failed: CID must be exactly 11 digits, got: {}", cid);
				return ResponseEntity.badRequest().body(createErrorResponse("CID must be exactly 11 digits"));
			}

			logger.info("Calling studentService.getStudentByCid() with CID: {}", cid.trim());
			Optional<Student> studentOpt = studentService.getStudentByCid(cid.trim());
			
			if (studentOpt.isPresent()) {
				Student student = studentOpt.get();
				logger.info("Student found: {} - {}", student.getCidNumber(), student.getFullName());
				Map<String, Object> response = new HashMap<>();
				response.put("success", true);
				response.put("student", createStudentResponse(student));
				return ResponseEntity.ok(response);
			} else {
				logger.warn("Student not found with CID: {}", cid);
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(createErrorResponse("Student not found with CID: " + cid));
			}

		} catch (Exception e) {
			logger.error("Error searching student by CID {}: {}", cid, e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(createErrorResponse("Error searching student: " + e.getMessage()));
		}
	}

	// Get student by ID
	@GetMapping("/{studentId}")
	public ResponseEntity<Map<String, Object>> getStudentById(@PathVariable Long studentId) {
		try {
			Optional<Student> studentOpt = studentService.getStudentById(studentId);
			
			if (studentOpt.isPresent()) {
				Student student = studentOpt.get();
				Map<String, Object> response = new HashMap<>();
				response.put("success", true);
				response.put("student", createStudentResponse(student));
				return ResponseEntity.ok(response);
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(createErrorResponse("Student not found with ID: " + studentId));
			}

		} catch (Exception e) {
			logger.error("Error getting student by ID {}: {}", studentId, e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(createErrorResponse("Error getting student: " + e.getMessage()));
		}
	}

	// Create new student
	@PostMapping
	public ResponseEntity<Map<String, Object>> createStudent(@RequestBody Student student) {
		try {
			// Validate required fields
			if (student.getCidNumber() == null || student.getCidNumber().trim().isEmpty()) {
				return ResponseEntity.badRequest().body(createErrorResponse("CID number is required"));
			}

			if (student.getFirstName() == null || student.getFirstName().trim().isEmpty()) {
				return ResponseEntity.badRequest().body(createErrorResponse("First name is required"));
			}

			if (student.getLastName() == null || student.getLastName().trim().isEmpty()) {
				return ResponseEntity.badRequest().body(createErrorResponse("Last name is required"));
			}

			if (student.getEmailAddress() == null || student.getEmailAddress().trim().isEmpty()) {
				return ResponseEntity.badRequest().body(createErrorResponse("Email address is required"));
			}

			Student createdStudent = studentService.createStudent(student);
			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("message", "Student created successfully");
			response.put("student", createStudentResponse(createdStudent));
			
			logger.info("Created new student with CID: {}", createdStudent.getCidNumber());
			return ResponseEntity.status(HttpStatus.CREATED).body(response);

		} catch (RuntimeException e) {
			logger.error("Error creating student: {}", e.getMessage());
			return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
		} catch (Exception e) {
			logger.error("Unexpected error creating student: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(createErrorResponse("Error creating student: " + e.getMessage()));
		}
	}

	// Update student
	@PostMapping("/{studentId}/update")
	public ResponseEntity<Map<String, Object>> updateStudent(@PathVariable Long studentId, @RequestBody Student studentDetails) {
		try {
			Student updatedStudent = studentService.updateStudent(studentId, studentDetails);
			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("message", "Student updated successfully");
			response.put("student", createStudentResponse(updatedStudent));
			
			logger.info("Updated student with ID: {}", studentId);
			return ResponseEntity.ok(response);

		} catch (RuntimeException e) {
			logger.error("Error updating student {}: {}", studentId, e.getMessage());
			return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
		} catch (Exception e) {
			logger.error("Unexpected error updating student {}: {}", studentId, e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(createErrorResponse("Error updating student: " + e.getMessage()));
		}
	}

	// Get all active students
	@GetMapping("/active")
	public ResponseEntity<Map<String, Object>> getAllActiveStudents() {
		try {
			List<Student> students = studentService.getAllActiveStudents();
			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("students", students.stream().map(this::createStudentResponse).toList());
			response.put("count", students.size());
			
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			logger.error("Error getting active students: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(createErrorResponse("Error getting active students: " + e.getMessage()));
		}
	}

	// Search students by name
	@GetMapping("/search")
	public ResponseEntity<Map<String, Object>> searchStudents(@RequestParam String name) {
		try {
			List<Student> students = studentService.searchStudentsByName(name);
			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("students", students.stream().map(this::createStudentResponse).toList());
			response.put("count", students.size());
			
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			logger.error("Error searching students by name {}: {}", name, e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(createErrorResponse("Error searching students: " + e.getMessage()));
		}
	}

	// Get students by college
	@GetMapping("/college/{collegeName}")
	public ResponseEntity<Map<String, Object>> getStudentsByCollege(@PathVariable String collegeName) {
		try {
			List<Student> students = studentService.getStudentsByCollege(collegeName);
			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("students", students.stream().map(this::createStudentResponse).toList());
			response.put("count", students.size());
			
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			logger.error("Error getting students by college {}: {}", collegeName, e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(createErrorResponse("Error getting students by college: " + e.getMessage()));
		}
	}

	// Get student statistics
	@GetMapping("/statistics")
	public ResponseEntity<Map<String, Object>> getStudentStatistics() {
		try {
			Object[] stats = studentService.getStudentStatistics();
			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("statistics", Map.of(
				"totalStudents", stats[0],
				"activeStudents", stats[1],
				"governmentFunded", stats[2],
				"privatelyFunded", stats[3]
			));
			
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			logger.error("Error getting student statistics: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(createErrorResponse("Error getting statistics: " + e.getMessage()));
		}
	}

	// Helper method to create student response object
	private Map<String, Object> createStudentResponse(Student student) {
		Map<String, Object> studentMap = new HashMap<>();
		studentMap.put("studentId", student.getStudentId());
		studentMap.put("cidNumber", student.getCidNumber());
		studentMap.put("studentIdNumber", student.getStudentIdNumber());
		studentMap.put("firstName", student.getFirstName());
		studentMap.put("middleName", student.getMiddleName());
		studentMap.put("lastName", student.getLastName());
		studentMap.put("fullName", student.getFullName());
		studentMap.put("contactNumber", student.getContactNumber());
		studentMap.put("emailAddress", student.getEmailAddress());
		studentMap.put("programCourse", student.getProgramCourse());
		studentMap.put("semester", student.getSemester());
		studentMap.put("yearOfStudy", student.getYearOfStudy());
		studentMap.put("collegeName", student.getCollegeName());
		studentMap.put("department", student.getDepartment());
		studentMap.put("address", student.getAddress());
		studentMap.put("fundingType", student.getFundingType());
		studentMap.put("scholarshipType", student.getScholarshipType());
		studentMap.put("enrollmentDate", student.getEnrollmentDate());
		studentMap.put("isActive", student.getIsActive());
		studentMap.put("createdAt", student.getCreatedAt());
		studentMap.put("updatedAt", student.getUpdatedAt());
		
		return studentMap;
	}

	// Test endpoint to verify student functionality
	@GetMapping("/test")
	public ResponseEntity<Map<String, Object>> testStudentFunctionality() {
		try {
			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("message", "Student controller is working");
			response.put("database", "Connected");
			response.put("repository", "Available");
			
			// Test repository method
			long studentCount = studentRepository.count();
			response.put("totalStudents", studentCount);
			
			logger.info("Student controller test successful. Total students: {}", studentCount);
			return ResponseEntity.ok(response);
			
		} catch (Exception e) {
			logger.error("Error in student controller test: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(createErrorResponse("Controller test failed: " + e.getMessage()));
		}
	}

	// Helper method to create error response
	private Map<String, Object> createErrorResponse(String message) {
		Map<String, Object> error = new HashMap<>();
		error.put("success", false);
		error.put("error", message);
		return error;
	}
}
