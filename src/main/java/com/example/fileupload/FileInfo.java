package com.example.fileupload;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "file")
public class FileInfo {

    private Integer id;
    private String  name;
    private String contentType;
	private String lastModifiedDate;
    private Integer size;
    private String md5;
    private String path;

    public FileInfo() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

    @Override
    public String toString() {
        return String.format("{id=%s, name=%s, last-modified:%s, size:%s, content-type:%s, md5:%s}", 
        		id, name, lastModifiedDate, size, contentType, md5);
    }

	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
