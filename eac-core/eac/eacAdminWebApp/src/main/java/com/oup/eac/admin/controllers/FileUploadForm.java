package com.oup.eac.admin.controllers;

import org.springframework.web.multipart.MultipartFile;
 
public class FileUploadForm {
 
	private MultipartFile myfile;

	public MultipartFile getMyfile() {
		return myfile;
	}

	public void setMyfile(MultipartFile myfile) {
		this.myfile = myfile;
	}
}
