package com.sprms.system.frmbeans;

import jakarta.persistence.Id;

public class CountryFrmBean {
	
	@Id
	private Long Id;
	private String countryName;
	private String countryCode;
	
	public Long getId() {
		return Id;
	}
	public void setId(Long id) {
		Id = id;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	
	

}
