package com.bedbath.fileuploadanddownload.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bedbath.fileuploadanddownload.model.FileInfo;

@Service
public class FileUploadDownloadServiceImpl implements FileUploadDownloadService {

	@Value("${ftp.remote.directory:/}")
	private String ftpRemoteDirectory;

	public Boolean checkIfFileExist(String pdfFileName) {
		Boolean fileCheckResult = false;
		File file = new File(ftpRemoteDirectory + File.separator + pdfFileName);
		if (file.exists()) {
			fileCheckResult = true;
			return fileCheckResult;
		} else {
			return fileCheckResult;
		}
	}

	public byte[] download(HttpServletRequest request, HttpServletResponse response, String pdfFileName)
			throws Exception {
		Boolean fileDownloadResult = false;
		File file = new File(ftpRemoteDirectory + File.separator + pdfFileName);

	//	if (file.exists()) {

	//		response.setContentType("application/pdf");
	//		response.addHeader("Content-Disposition", "inline; filename=" + pdfFileName);
	//		response.setContentLength((int) file.length());

			OutputStream os = response.getOutputStream();
			FileInputStream fis = new FileInputStream(file);
			int available = fis.available();
			byte[] buffer = new byte[available];
			
			for (int i = 0; i < available; i += available)
		     {
		         fis.read(buffer);
		     }

			 fis.close();
	//		int b = -1;

	//		while ((b = fis.read(buffer)) != -1) {
	//			os.write(buffer, 0, b);
	//		}

		/*	fis.close();
			os.close();
			fileDownloadResult = true;*/
			return buffer;
	//	}
		
	}

	public Boolean saveFileToRemote(FileInfo fileInfo, MultipartFile inputFile) {

		Boolean fileSaveResult = false;
		try {
			byte[] bytes = inputFile.getBytes();

			File dir = new File(ftpRemoteDirectory);
			if (!dir.exists())
				dir.mkdirs();

			// Create the file on server
			File serverFile = new File(dir.getAbsolutePath() + File.separator + inputFile.getOriginalFilename());
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
			stream.write(bytes);
			stream.close();
			fileSaveResult = true;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return fileSaveResult;
	}
}
