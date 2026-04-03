package com.sprms.system.hbmbeans;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "tbl_gewogm")
public class GewogM {

	// entity
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "gewog_id")
	private Long gewogId;
	@Column(name = "gewog_name")
	private String gewogName;

	// Many Gewogs belong to one Dzongkhag
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="dzongkhag_id", nullable = false) 
	private DzongkhagM dzongkhag;

	public GewogM() {

	}


	public Long getGewogId() {
		return gewogId;
	}



	public void setGewogId(Long gewogId) {
		this.gewogId = gewogId;
	}



	public String getGewogName() {
		return gewogName;
	}

	public void setGewogName(String gewogName) {
		this.gewogName = gewogName;
	}

	public DzongkhagM getDzongkhag() {
		return dzongkhag;
	}

	public void setDzongkhag(DzongkhagM dzongkhag) {
		this.dzongkhag = dzongkhag;
	}

}
