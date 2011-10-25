package com.example.fileupload;

import java.util.Collection;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FileInfoCollection {

    private Collection<FileInfo> files;
    
    public FileInfoCollection() {
    }

    public FileInfoCollection(Collection<FileInfo> users) {
        this.files = users;
    }

    @XmlElement(name="file")
    @XmlElementWrapper(name="files")
    public Collection<FileInfo> getFiles() {
        return files;
    }

}
