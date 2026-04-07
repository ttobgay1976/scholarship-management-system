package com.sprms.system.frmbeans;

import java.time.LocalDateTime;

public class ScholarshipProgramDTO {
	
	private Long id;
    private String scholarshipProgramName;
    
    // ⚡ FundingAgency info
    private Long fundingAgencyId;       // for saving/updating
    private String fundingAgencyName;   // for displaying in lists

    private Integer scholarshipAvailableSlots;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;

    // ✅ Constructors
    public ScholarshipProgramDTO() {}

    public ScholarshipProgramDTO(Long id, String scholarshipProgramName,
                                 Long fundingAgencyId, String fundingAgencyName,
                                 Integer scholarshipAvailableSlots,
                                 LocalDateTime createdAt, LocalDateTime updateAt) {
        this.id = id;
        this.scholarshipProgramName = scholarshipProgramName;
        this.fundingAgencyId = fundingAgencyId;
        this.fundingAgencyName = fundingAgencyName;
        this.scholarshipAvailableSlots = scholarshipAvailableSlots;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
    }

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

	public Long getFundingAgencyId() {
		return fundingAgencyId;
	}

	public void setFundingAgencyId(Long fundingAgencyId) {
		this.fundingAgencyId = fundingAgencyId;
	}

	public String getFundingAgencyName() {
		return fundingAgencyName;
	}

	public void setFundingAgencyName(String fundingAgencyName) {
		this.fundingAgencyName = fundingAgencyName;
	}

	public Integer getScholarshipAvailableSlots() {
		return scholarshipAvailableSlots;
	}

	public void setScholarshipAvailableSlots(Integer scholarshipAvailableSlots) {
		this.scholarshipAvailableSlots = scholarshipAvailableSlots;
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
