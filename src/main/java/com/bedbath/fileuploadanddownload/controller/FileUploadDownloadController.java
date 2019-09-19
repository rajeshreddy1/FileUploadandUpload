package com.bedbath.fileuploadanddownload.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bedbath.fileuploadanddownload.model.FileInfo;
import com.bedbath.fileuploadanddownload.service.FileUploadDownloadService;

@RestController
public class FileUploadDownloadController {
	
	private static final Logger logger = Logger.getLogger(FileUploadDownloadController.class);
	@Autowired
	public FileUploadDownloadService fileUploadDownloadService;
	
	@RequestMapping(value = "/api/file/upload", method = RequestMethod.POST, headers = ("content-type=multipart/*"))
	@ResponseBody
	public ResponseEntity<FileInfo> upload(@RequestParam("file") MultipartFile inputFile) {
		FileInfo fileInfo = new FileInfo();
		HttpHeaders headers = new HttpHeaders();
		if (!inputFile.isEmpty()) {
			try {
				String originalFilename = inputFile.getOriginalFilename();
				headers.add("File Uploaded Successfully - ", originalFilename);
				byte[] bytes = inputFile.getBytes();
				long size = bytes.length;
				Boolean result = fileUploadDownloadService.saveFileToRemote(fileInfo, inputFile);
				if (result) {
					fileInfo.setFileName(originalFilename);
					fileInfo.setFileSize(size);
					fileInfo.setStatus("Upload Success");
					return new ResponseEntity<FileInfo>(fileInfo, headers, HttpStatus.OK);
				} else {
					fileInfo.setStatus("Upload Failed");
					return new ResponseEntity<FileInfo>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
			} catch (Exception e) {
				return new ResponseEntity<FileInfo>(HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<FileInfo>(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/api/file/download", method = RequestMethod.GET)
	public ResponseEntity<FileInfo> download(HttpServletRequest request, HttpServletResponse response) {
		
		FileInfo fileInfo = new FileInfo();
		HttpHeaders headers = new HttpHeaders();
		String pdfFileName = request.getParameter("file");
	//	String pdfFilePath = request.getParameter("path");

		try {
			byte[] result = fileUploadDownloadService.download(request, response, pdfFileName);
			
				fileInfo.setFileName(pdfFileName);
				fileInfo.setContent(result);
				fileInfo.setStatus("Dowload Successfull");
				return new ResponseEntity<FileInfo>(fileInfo, headers, HttpStatus.OK);
			
		} catch (Exception e) {
			logger.error("Exception - "+e.getMessage());
			return new ResponseEntity<FileInfo>(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/api/file/checkifexist", method = RequestMethod.GET)
	public ResponseEntity<FileInfo> checkIfFileExist(HttpServletRequest request, HttpServletResponse response) {
		FileInfo fileInfo = new FileInfo();
		HttpHeaders headers = new HttpHeaders();
		String pdfFileName = request.getParameter("file");
	//	String pdfFilePath = request.getParameter("path");
		try {
			Boolean result = fileUploadDownloadService.checkIfFileExist(pdfFileName);
			if (result){
				fileInfo.setFileName(pdfFileName);
				fileInfo.setStatus("File Found");
				return new ResponseEntity<FileInfo>(fileInfo, headers, HttpStatus.OK);
			}else{
				fileInfo.setFileName(pdfFileName);
				fileInfo.setStatus("File Not Found");
				return new ResponseEntity<FileInfo>(fileInfo, headers, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			logger.error("Exception - "+e.getMessage());
			return new ResponseEntity<FileInfo>(HttpStatus.BAD_REQUEST);
		}
	}
	
}
