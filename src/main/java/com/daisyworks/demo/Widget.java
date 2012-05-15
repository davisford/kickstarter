package com.daisyworks.demo;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * A generic widget object that can be serialized; modify to suit your needs.
 */
@XmlRootElement(name = "widget")
public class Widget {

    private String  name;
    private Integer id;

    public Widget() { }
    
    public Widget(int id, String name) { this.id = id; this.name = name; }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
	public Integer getId() {
		return id;
	}

	public void setId(Integer size) {
		this.id = size;
	}
    
    @Override
    public String toString() {
        return String.format("{name=%s, id:%s}", 
        		name, id);
    }
}
