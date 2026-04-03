package com.sprms.system.hbmbeans;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_flood_registration")
public class FloodManagement implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Column(name = "fld_reg_id")
	private Long Id;
	@Column(name = "fld_type_id")
	private String fld_Type;
	@Column(name = "fld_severity")
	private Integer fldSeverity;
	@Column(name = "fld_location")
	private String location;
	private LocalDate fld_date_occurance;
	private String fld_reported_By;
	private String fld_remarks;
	private int status;
	@Column(name = "created_at", updatable = false, insertable = true)
	private LocalDateTime created_at;
	@Column(name = "updated_at", updatable = true, insertable = false)
	private LocalDateTime updated_at;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fld_dzongkhag_id", referencedColumnName = "dzongkhag_id")
	private DzongkhagM dzongkhag;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fld_gewog_id", referencedColumnName = "gewog_id")
	private GewogM gewog;

	// One Flood -> Many Files
	@OneToMany(mappedBy = "flood", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<FloodFiles> mgFiles = new ArrayList<>();
	

	public void addFile(FloodFiles file) {
		mgFiles.add(file);
		file.setFlood(this);
	}

	public void removeFile(FloodFiles file) {
		mgFiles.remove(file);
		file.setFlood(null);
	}

	public FloodManagement() {

	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getFld_Type() {
		return fld_Type;
	}

	public void setFld_Type(String fld_Type) {
		this.fld_Type = fld_Type;
	}

	public Integer getFldSeverity() {
		return fldSeverity;
	}

	public void setFldSeverity(Integer fldSeverity) {
		this.fldSeverity = fldSeverity;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public LocalDate getFld_date_occurance() {
		return fld_date_occurance;
	}

	public void setFld_date_occurance(LocalDate fld_date_occurance) {
		this.fld_date_occurance = fld_date_occurance;
	}

	public String getFld_reported_By() {
		return fld_reported_By;
	}

	public void setFld_reported_By(String fld_reported_By) {
		this.fld_reported_By = fld_reported_By;
	}

	public String getFld_remarks() {
		return fld_remarks;
	}

	public void setFld_remarks(String fld_remarks) {
		this.fld_remarks = fld_remarks;
	}

	public LocalDateTime getCreated_at() {
		return created_at;
	}

	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}

	public LocalDateTime getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(LocalDateTime updated_at) {
		this.updated_at = updated_at;
	}

	public DzongkhagM getDzongkhag() {
		return dzongkhag;
	}

	public void setDzongkhag(DzongkhagM dzongkhag) {
		this.dzongkhag = dzongkhag;
	}

	public GewogM getGewog() {
		return gewog;
	}

	public void setGewog(GewogM gewog) {
		this.gewog = gewog;
	}

	public List<FloodFiles> getMgFiles() {
		return mgFiles;
	}

	public void setMgFiles(List<FloodFiles> mgFiles) {
		this.mgFiles = mgFiles;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
