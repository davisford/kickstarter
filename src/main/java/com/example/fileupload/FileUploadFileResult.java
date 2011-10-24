package com.example.fileupload;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "fileUploadResult")
public class FileUploadFileResult {
	
	public String msg;
	
	public FileUploadFileResult() {
		
	}
	
	public FileUploadFileResult(String msg) {
		this.msg = msg;
	}
	
	public String getMessage() { return msg; }
	
	public void setMessage(String val) { msg = val; }

}
