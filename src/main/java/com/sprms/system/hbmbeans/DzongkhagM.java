package com.sprms.system.hbmbeans;



import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="tbl_dzongkhagm")
public class DzongkhagM {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="dzongkhag_id")
	private Long dzongkhagId;
	
	@Column(name="dzongkhag_name")
	private String dzongkhagName;
	
	//join table
    // One Dzongkhag has many Gewogs
    @OneToMany(mappedBy = "dzongkhag", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<GewogM> gewogs;
	

	public DzongkhagM() {
		
	}


	public Long getDzongkhagId() {
		return dzongkhagId;
	}


	public void setDzongkhagId(Long dzongkhagId) {
		this.dzongkhagId = dzongkhagId;
	}


	public String getDzongkhagName() {
		return dzongkhagName;
	}


	public void setDzongkhagName(String dzongkhagName) {
		this.dzongkhagName = dzongkhagName;
	}


	public List<GewogM> getGewogs() {
		return gewogs;
	}


	public void setGewogs(List<GewogM> gewogs) {
		this.gewogs = gewogs;
	}


	
}
