package com.sprms.system.frmbeans;

import java.time.LocalDateTime;

public class FundingAgencyDTO {

	private Long id;
	private String fundingAgencyName;
	private String description;
	private LocalDateTime createdAt;
	private LocalDateTime updateAt;

	// Default constructor
	public FundingAgencyDTO() {
	}

	// All-args constructor
	public FundingAgencyDTO(Long id, String fundingAgencyName, String description, LocalDateTime createdAt,
			LocalDateTime updateAt) {
		this.id = id;
		this.fundingAgencyName = fundingAgencyName;
		this.description = description;
		this.createdAt = createdAt;
		this.updateAt = updateAt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFundingAgencyName() {
		return fundingAgencyName;
	}

	public void setFundingAgencyName(String fundingAgencyName) {
		this.fundingAgencyName = fundingAgencyName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(LocalDateTime updateAt) {
		this.updateAt = updateAt;
	}

}
