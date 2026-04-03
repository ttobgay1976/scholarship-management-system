package com.sprms.system.frmbeans;

public class StateDTO {

	private Long Id;
	private String stateName;
	private int countryId;
	
	
	public StateDTO(Long id, String stateName) {
		Id = id;
		this.stateName = stateName;
	}
	
	public Long getId() {
		return Id;
	}
	public void setId(Long id) {
		Id = id;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	public int getCountryId() {
		return countryId;
	}
	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}
	
	
	
}
