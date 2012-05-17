package com.daisyworks.demo;

import java.util.Collection;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Wrapper class for a collection of {@link Port} objects for JAXB purposes
 */
@XmlRootElement(name="ports")
public class PortCollection {

    private Collection<Port> ports;
    
    public PortCollection() { }

    public PortCollection(Collection<Port> ports) {
        this.ports = ports;
    }

    @XmlElementWrapper(name="ports")
    public Collection<Port> getPorts() {
        return ports;
    }

}
