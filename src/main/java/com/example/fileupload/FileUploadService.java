package com.example.fileupload;

import java.io.InputStream;


public interface FileUploadService {

    FileUploadFileCollection getFiles();

    FileUploadFile getFile(Integer id);

    FileUploadFileResult create(FileUploadFile file, InputStream istream);

}