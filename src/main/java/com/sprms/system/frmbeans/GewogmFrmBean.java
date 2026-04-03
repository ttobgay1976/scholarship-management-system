package com.sprms.system.frmbeans;


import com.sprms.system.hbmbeans.DzongkhagM;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class GewogmFrmBean {

	private Integer gewogId;
	private String gewogName;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dzongkhag_id") // foreign key column in tbl_gewogm
	private DzongkhagM dzongkhag;

	public Integer getGewogId() {
		return gewogId;
	}

	public void setGewogId(Integer gewogId) {
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
