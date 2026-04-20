package com.sprms.system.frmbeans;

import java.time.LocalDateTime;

public class SupportingFilesDTO {

	private Long id;
	private String fileName;
	private String fileLocation;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	private Long scholarshipRegistrationId;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileLocation() {
		return fileLocation;
	}

	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
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

	public Long getScholarshipRegistrationId() {
		return scholarshipRegistrationId;
	}

	public void setScholarshipRegistrationId(Long scholarshipRegistrationId) {
		this.scholarshipRegistrationId = scholarshipRegistrationId;
	}

	
}
