package com.sprms.system.hbmbeans;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

@Entity
@Table(name = "tbl_status")
public class Status {

	@Id
	@Column(name="status_id")
	private long Id;
	@Column(name = "status_name")
	private String statusName;
	
	
	public Status() {
		
	}
	public long getId() {
		return Id;
	}
	public void setId(long id) {
		Id = id;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	
	

}
