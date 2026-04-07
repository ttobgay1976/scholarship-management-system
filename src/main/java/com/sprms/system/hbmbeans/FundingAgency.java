package com.sprms.system.hbmbeans;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="tbl_funding_agency")
public class FundingAgency {

	@Id
	private Long Id;
	private String fundingAgencyName;
	private String description;
	private LocalDateTime createdAt;
	private LocalDateTime updateAt;
	
	public Long getId() {
		return Id;
	}
	public void setId(Long id) {
		Id = id;
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
