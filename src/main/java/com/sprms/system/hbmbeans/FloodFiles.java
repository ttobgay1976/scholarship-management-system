package com.sprms.system.hbmbeans;


import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="tbl_flood_registration_files")
public class FloodFiles implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id	
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Column(name="reg_id")
	private long Id;
	@Column(name="fld_reg_file_name")
	private String fileName;
	@Column(name="fld_reg_file_location")
	private String fileLocation;
	@Column(name="created_at", updatable = false, insertable = true)
	private LocalDateTime createdAt;
	@Column(name="updated_at", updatable = true, insertable = false)
	private LocalDateTime updatedAt;
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fld_reg_id", nullable = true)
    private FloodManagement flood;
    
	
	public FloodFiles() {

	}
	public long getId() {
		return Id;
	}
	public void setId(long id) {
		Id = id;
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
	public FloodManagement getFlood() {
		return flood;
	}
	public void setFlood(FloodManagement flood) {
		this.flood = flood;
	}

	
	
	
	
}
