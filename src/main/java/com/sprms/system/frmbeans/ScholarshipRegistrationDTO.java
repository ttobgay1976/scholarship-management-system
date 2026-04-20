package com.sprms.system.frmbeans;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.sprms.system.applicationEnums.ApplicationStatus;

public class ScholarshipRegistrationDTO {

	private Long id;

	private String citizenId;
	private String firstName;
	private String middleName;
	private String lastName;

	private LocalDate dateOfBirth;
	private String gender;

	private String fatherName;
	private String motherName;

	private String permanentAddress;
	private String guardianDetail;

	private Long indexNumber;

	private String emailAddress;
	private String contactNo;

	private Long streamId;

	private String streamName; // for display only

	private String countryOfCompletion;

	private String remarks;

	private ApplicationStatus status;
	
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	private String verifier_remarks;
	private String verified_by;
	private LocalDateTime verified_at;

	private String approval_remarks;
	private String approved_by;
	private LocalDateTime approved_at;

	private List<MultipartFile> files;

	// 🔥 One-to-Many DTO relation
	private List<SupportingFilesDTO> supportingdto;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCitizenId() {
		return citizenId;
	}

	public void setCitizenId(String citizenId) {
		this.citizenId = citizenId;
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

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public String getMotherName() {
		return motherName;
	}

	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}

	public String getPermanentAddress() {
		return permanentAddress;
	}

	public void setPermanentAddress(String permanentAddress) {
		this.permanentAddress = permanentAddress;
	}

	public String getGuardianDetail() {
		return guardianDetail;
	}

	public void setGuardianDetail(String guardianDetail) {
		this.guardianDetail = guardianDetail;
	}

	public Long getIndexNumber() {
		return indexNumber;
	}

	public void setIndexNumber(Long indexNumber) {
		this.indexNumber = indexNumber;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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

	public List<SupportingFilesDTO> getSupportingdto() {
		return supportingdto;
	}

	public void setSupportingdto(List<SupportingFilesDTO> supportingdto) {
		this.supportingdto = supportingdto;
	}

	public String getCountryOfCompletion() {
		return countryOfCompletion;
	}

	public void setCountryOfCompletion(String countryOfCompletion) {
		this.countryOfCompletion = countryOfCompletion;
	}

	public List<MultipartFile> getFiles() {
		return files;
	}

	public void setFiles(List<MultipartFile> files) {
		this.files = files;
	}

	public Long getStreamId() {
		return streamId;
	}

	public void setStreamId(Long streamId) {
		this.streamId = streamId;
	}

	public String getStreamName() {
		return streamName;
	}

	public void setStreamName(String streamName) {
		this.streamName = streamName;
	}

	public String getVerified_by() {
		return verified_by;
	}

	public void setVerified_by(String verified_by) {
		this.verified_by = verified_by;
	}

	public LocalDateTime getVerified_at() {
		return verified_at;
	}

	public void setVerified_at(LocalDateTime verified_at) {
		this.verified_at = verified_at;
	}

	public String getApproved_by() {
		return approved_by;
	}

	public void setApproved_by(String approved_by) {
		this.approved_by = approved_by;
	}

	public LocalDateTime getApproved_at() {
		return approved_at;
	}

	public void setApproved_at(LocalDateTime approved_at) {
		this.approved_at = approved_at;
	}

	public ApplicationStatus getStatus() {
		return status;
	}

	public void setStatus(ApplicationStatus status) {
		this.status = status;
	}

	public String getVerifier_remarks() {
		return verifier_remarks;
	}

	public void setVerifier_remarks(String verifier_remarks) {
		this.verifier_remarks = verifier_remarks;
	}

	public String getApproval_remarks() {
		return approval_remarks;
	}

	public void setApproval_remarks(String approval_remarks) {
		this.approval_remarks = approval_remarks;
	}

	
}
