package com.daisyworks.demo;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * A generic widget object that can be serialized; modify to suit your needs.
 */
@XmlRootElement(name = "port")
public class Port {

    private String  path;
    private Integer id;

    public Port() { }
    
    public Port(int id, String path) { this.id = id; this.path = path; }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
    
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
    
    @Override
    public String toString() {
        return String.format("{id=%s, path:%s}", 
        		id, path);
    }
}
