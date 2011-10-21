package com.example.fileupload;

import java.util.Collection;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FileUploadFileCollection {

    private Collection<FileUploadFile> files;
    
    public FileUploadFileCollection() {
    }

    public FileUploadFileCollection(Collection<FileUploadFile> users) {
        this.files = users;
    }

    @XmlElement(name="file")
    @XmlElementWrapper(name="files")
    public Collection<FileUploadFile> getFiles() {
        return files;
    }

}
