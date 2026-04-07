package com.sprms.system.hbmbeans;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="tbl_scholarship_detail")
public class ScholarshipProgram {

	@Id
	private Long id;
	private String scholarshipProgramName;
	/* private Long scholarshipFundingSource; */
	private Integer scholarshipAvailableSlots;
	private LocalDateTime createdat;
	private LocalDateTime updateat;
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scholarship_funding_source_id") // FK column in DB
    private FundingAgency fundingAgency;
    
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getScholarshipProgramName() {
		return scholarshipProgramName;
	}
	public void setScholarshipProgramName(String scholarshipProgramName) {
		this.scholarshipProgramName = scholarshipProgramName;
	}
	public Integer getScholarshipAvailableSlots() {
		return scholarshipAvailableSlots;
	}
	public void setScholarshipAvailableSlots(Integer scholarshipAvailableSlots) {
		this.scholarshipAvailableSlots = scholarshipAvailableSlots;
	}
	public LocalDateTime getCreatedat() {
		return createdat;
	}
	public void setCreatedat(LocalDateTime createdat) {
		this.createdat = createdat;
	}
	public LocalDateTime getUpdateat() {
		return updateat;
	}
	public void setUpdateat(LocalDateTime updateat) {
		this.updateat = updateat;
	}
	public FundingAgency getFundingAgency() {
		return fundingAgency;
	}
	public void setFundingAgency(FundingAgency fundingAgency) {
		this.fundingAgency = fundingAgency;
	}

	
	
	
}
