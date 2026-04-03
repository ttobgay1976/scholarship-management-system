package com.sprms.system.frmbeans;


public class FilesFrmBean {

	private long Id;
	private long fldRegId;
	private String fileName;
	private String fileLocation;
	
	public long getId() {
		return Id;
	}
	public void setId(long id) {
		Id = id;
	}
	public long getFldRegId() {
		return fldRegId;
	}
	public void setFldRegId(long fldRegId) {
		this.fldRegId = fldRegId;
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
	
	

}
