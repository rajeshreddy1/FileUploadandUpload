package com.bedbath.fileuploadanddownload.model;

public class FileInfo {
	
	private String fileName;
	private long fileSize;
	private String status;
	private byte[] content;
	
	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
 
	public String getFileName() {
		return fileName;
	}
 
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
 
	public long getFileSize() {
		return fileSize;
	}
 
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
}
