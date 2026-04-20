package com.sprms.system.hbmbeans;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.sprms.system.applicationEnums.ApplicationStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_sch_registration")
public class ScholarshipRegistration {

	@Id
	private Long id;

	@Column(name = "citizen_id")
	private String citizenId;
	@Column(name = "first_name")
	private String firstName;
	@Column(name = "middle_name")
	private String middleName;
	@Column(name = "last_name")
	private String lastName;
	private LocalDate dateOfBirth;
	private String gender;
	@Column(name = "father_name")
	private String fatherName;
	@Column(name = "mother_name")
	private String motherName;
	/*
	 * private Long dzongkhagId; private Long gewogId; private String villageName;
	 */
	@Column(name = "permanent_address")
	private String permanentAddress;
	@Column(name = "guardian_details")
	private String guardianDetail;
	@Column(name = "index_number")
	private Long indexNumber;
	@Column(name = "email_id")
	private String emailAddress;
	@Column(name = "contact_no")
	private String contactNo;
	@Column(name = "country_of_completion")
	private String countryOfCompletion;
	
	private String remarks;
	
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

	@Column(name = "created_at")
	private LocalDateTime createdAt;
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
	private String verifier_remarks;
	private String verified_by;
	private LocalDateTime verified_at;

	private String approval_remarks;
    private String approved_by;
    private LocalDateTime approved_at;
    

	// One Flood -> Many Files
	@OneToMany(mappedBy = "scholarshipRegistration", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<SupportingFiles> supportingDocs = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stream_id")
	private Stream stream;

	public void addFile(SupportingFiles file) {
		supportingDocs.add(file);
		file.setScholarshipRegistration(this);
	}

	public void removeFile(SupportingFiles file) {
		supportingDocs.remove(file);
		file.setScholarshipRegistration(null);
	}

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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public List<SupportingFiles> getSupportingDocs() {
		return supportingDocs;
	}

	public void setSupportingDocs(List<SupportingFiles> supportingDocs) {
		this.supportingDocs = supportingDocs;
	}

	public String getCountryOfCompletion() {
		return countryOfCompletion;
	}

	public void setCountryOfCompletion(String countryOfCompletion) {
		this.countryOfCompletion = countryOfCompletion;
	}

	public Stream getStream() {
		return stream;
	}

	public void setStream(Stream stream) {
		this.stream = stream;
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
