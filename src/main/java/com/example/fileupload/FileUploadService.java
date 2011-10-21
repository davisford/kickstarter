package com.example.fileupload;

import javax.ws.rs.core.Response;


public interface FileUploadService {

    FileUploadFileCollection getFiles();

    FileUploadFile getFile(Integer id);

    Response getBadRequest();

}