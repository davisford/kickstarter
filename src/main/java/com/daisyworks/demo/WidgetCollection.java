package com.daisyworks.demo;

import java.util.Collection;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="widgets")
public class WidgetCollection {

    private Collection<Widget> widgets;
    
    public WidgetCollection() {
    }

    public WidgetCollection(Collection<Widget> widgets) {
        this.widgets = widgets;
    }

    @XmlElementWrapper(name="widgets")
    public Collection<Widget> getWidgets() {
        return widgets;
    }

}
