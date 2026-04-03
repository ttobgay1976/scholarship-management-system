package com.sprms.system.frmbeans;

public class CitiesDTO {
	
    private Long id;         // City ID (for edit)
    private String cityName; // Name of the city
    
    
    
    public CitiesDTO(Long id, String cityName) {
		this.id = id;
		this.cityName = cityName;
	}
    
	private Long stateId;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public Long getStateId() {
		return stateId;
	}
	public void setStateId(Long stateId) {
		this.stateId = stateId;
	} 

	
}
