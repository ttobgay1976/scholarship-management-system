package com.sprms.system.hbmbeans;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="tbl_country")
public class Country implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private Long Id;
	
	@Column(name="country_Name")
	private String countryName;
	
	@Column(name="country_code")
	private String countryCode;
	
    @OneToMany(mappedBy="country")
    @JsonManagedReference
    private List<State> states;
    

    @OneToMany(mappedBy="country", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    private List<College> colleges;

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

	public List<State> getStates() {
		return states;
	}

	public void setStates(List<State> states) {
		this.states = states;
	}

	public List<College> getColleges() {
		return colleges;
	}

	public void setColleges(List<College> colleges) {
		this.colleges = colleges;
	}

	
	
}
