package com.sprms.system.hbmbeans;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_students")
public class Student implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "student_id")
	private Long studentId;

	@Column(name = "cid_number", unique = true, nullable = false, length = 11)
	private String cidNumber;

	@Column(name = "student_id_number", length = 20)
	private String studentIdNumber;

	@Column(name = "first_name", nullable = false, length = 100)
	private String firstName;

	@Column(name = "middle_name", length = 100)
	private String middleName;

	@Column(name = "last_name", nullable = false, length = 100)
	private String lastName;

	@Column(name = "full_name", nullable = false, length = 255)
	private String fullName;

	@Column(name = "contact_number", nullable = false, length = 15)
	private String contactNumber;

	@Column(name = "email_address", nullable = false, length = 255)
	private String emailAddress;

	@Column(name = "program_course", length = 255)
	private String programCourse;

	@Column(name = "semester", length = 50)
	private String semester;

	@Column(name = "year_of_study", length = 10)
	private String yearOfStudy;

	@Column(name = "college_name", length = 255)
	private String collegeName;

	@Column(name = "department", length = 100)
	private String department;

	@Column(name = "address", length = 500)
	private String address;

	@Column(name = "funding_type", length = 20)
	private String fundingType;

	@Column(name = "scholarship_type", length = 100)
	private String scholarshipType;

	@Column(name = "enrollment_date")
	private LocalDateTime enrollmentDate;

	@Column(name = "graduation_date")
	private LocalDateTime graduationDate;

	@Column(name = "is_active", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
	private Boolean isActive = true;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@Column(name = "created_by")
	private Long createdBy;

	@Column(name = "updated_by")
	private Long updatedBy;

	// Constructors
	public Student() {
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}

	public Student(String cidNumber, String firstName, String lastName, String contactNumber, String emailAddress) {
		this();
		this.cidNumber = cidNumber;
		this.firstName = firstName;
		this.lastName = lastName;
		this.contactNumber = contactNumber;
		this.emailAddress = emailAddress;
		this.fullName = firstName + " " + (middleName != null ? middleName + " " : "") + lastName;
	}

	// Getters and Setters
	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public String getCidNumber() {
		return cidNumber;
	}

	public void setCidNumber(String cidNumber) {
		this.cidNumber = cidNumber;
	}

	public String getStudentIdNumber() {
		return studentIdNumber;
	}

	public void setStudentIdNumber(String studentIdNumber) {
		this.studentIdNumber = studentIdNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getProgramCourse() {
		return programCourse;
	}

	public void setProgramCourse(String programCourse) {
		this.programCourse = programCourse;
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public String getYearOfStudy() {
		return yearOfStudy;
	}

	public void setYearOfStudy(String yearOfStudy) {
		this.yearOfStudy = yearOfStudy;
	}

	public String getCollegeName() {
		return collegeName;
	}

	public void setCollegeName(String collegeName) {
		this.collegeName = collegeName;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getFundingType() {
		return fundingType;
	}

	public void setFundingType(String fundingType) {
		this.fundingType = fundingType;
	}

	public String getScholarshipType() {
		return scholarshipType;
	}

	public void setScholarshipType(String scholarshipType) {
		this.scholarshipType = scholarshipType;
	}

	public LocalDateTime getEnrollmentDate() {
		return enrollmentDate;
	}

	public void setEnrollmentDate(LocalDateTime enrollmentDate) {
		this.enrollmentDate = enrollmentDate;
	}

	public LocalDateTime getGraduationDate() {
		return graduationDate;
	}

	public void setGraduationDate(LocalDateTime graduationDate) {
		this.graduationDate = graduationDate;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	// Utility methods
	public void updateFullName() {
		this.fullName = firstName + " " + (middleName != null && !middleName.trim().isEmpty() ? middleName + " " : "") + lastName;
	}

	@Override
	public String toString() {
		return "Student{" +
				"studentId=" + studentId +
				", cidNumber='" + cidNumber + '\'' +
				", fullName='" + fullName + '\'' +
				", emailAddress='" + emailAddress + '\'' +
				", programCourse='" + programCourse + '\'' +
				", collegeName='" + collegeName + '\'' +
				", isActive=" + isActive +
				'}';
	}
}
