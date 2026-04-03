package com.sprms.system.frmbeans;

import java.util.List;

import com.sprms.system.hbmbeans.GewogM;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

public class DzongkhagmFrmBean {

	@Id
	@Column(name="dzongkhag_id")
	
	private Long dzongkhagId;
	private String dzongkhagName;
	
	//join table
    // One Dzongkhag has many Gewogs
    @OneToMany(mappedBy = "dzongkhag", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<GewogM> gewogs;
    
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
