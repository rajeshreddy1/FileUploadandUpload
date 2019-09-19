package com.bedbath.fileuploadanddownload.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.bedbath.fileuploadanddownload.model.FileInfo;

public interface FileUploadDownloadService {
	
	public Boolean checkIfFileExist(String pdfFileName) throws Exception;
	
	public byte[] download(HttpServletRequest request, HttpServletResponse response, String pdfFileName) throws Exception;
	
	public Boolean saveFileToRemote(FileInfo fileInfo, MultipartFile inputFile) throws Exception;

}
