package com.sprms.system.frmbeans;

import java.time.LocalDateTime;

import com.sprms.system.hbmbeans.Cities;
import com.sprms.system.hbmbeans.Country;
import com.sprms.system.hbmbeans.State;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class CollegesDTO {
	
	private Long id;
	private String collegeName;
	private String shortName;
	private String email;
	private String phoneNo;
	private String address;
	private String website;
	private Integer establishedYear;
	private Boolean status;
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;

	// Flattened references
	private Long countryId;
	private String countryName;

	private Long stateId;
	private String stateName;

	private Long cityId;
	private String cityName;

	// Constructors
	public CollegesDTO() {}

	public CollegesDTO(Long id, String collegeName, String shortName, String email, String phoneNo, String address,
			String website, Integer establishedYear, Boolean status, LocalDateTime createdDate,
			LocalDateTime updatedDate, Long countryId, String countryName, Long stateId, String stateName, Long cityId,
			String cityName) {
		super();
		this.id = id;
		this.collegeName = collegeName;
		this.shortName = shortName;
		this.email = email;
		this.phoneNo = phoneNo;
		this.address = address;
		this.website = website;
		this.establishedYear = establishedYear;
		this.status = status;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
		this.countryId = countryId;
		this.countryName = countryName;
		this.stateId = stateId;
		this.stateName = stateName;
		this.cityId = cityId;
		this.cityName = cityName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCollegeName() {
		return collegeName;
	}

	public void setCollegeName(String collegeName) {
		this.collegeName = collegeName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public Integer getEstablishedYear() {
		return establishedYear;
	}

	public void setEstablishedYear(Integer establishedYear) {
		this.establishedYear = establishedYear;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(LocalDateTime updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public Long getStateId() {
		return stateId;
	}

	public void setStateId(Long stateId) {
		this.stateId = stateId;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	
}
