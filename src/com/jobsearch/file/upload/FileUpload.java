package com.jobsearch.file.upload;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public final class FileUpload {

	private FileUpload(){};

	public static boolean validateFile(MultipartFile file){

		if(!file.isEmpty()){
			return true;
		}

		return false;
	}

	public static void saveFile(MultipartFile file, int userId) throws IOException{
		byte[] bytes = file.getBytes();

		// Creating the directory to store file
		String rootPath = System.getProperty("user.home");
		File dir = new File(rootPath + File.separator + userId);
		if (!dir.exists())
			dir.mkdirs();

		// Create the file on server
		File serverFile = new File(dir.getAbsolutePath()
				+ File.separator + file.getOriginalFilename());
		BufferedOutputStream stream = new BufferedOutputStream(
				new FileOutputStream(serverFile));
		stream.write(bytes);
		stream.close();
	}

}
