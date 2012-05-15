package com.daisyworks.demo;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

@Path("/")
public class WidgetServiceImpl {

    private static Map<Integer, Widget> widgets = new HashMap<Integer, Widget>();
    
    private static final Logger LOGGER = Logger.getLogger(WidgetServiceImpl.class);
    
	private int toggled;
    
    public WidgetServiceImpl() {
    	widgets.put(1, new Widget(1, "foo"));
    	widgets.put(2, new Widget(2, "bar"));
    	LOGGER.info("---------------- Widget Service CTOR ------------------");
    }

    @GET
    @Path("/widgets")
    @Produces(MediaType.APPLICATION_JSON)
    public WidgetCollection getWidgets() {
    	LOGGER.debug("Getting all widgets");
        return new WidgetCollection(widgets.values());
    }

    @GET
    @Path("/widget/{id}")
    public Widget getWidget(@PathParam("id") Integer id) {
    	LOGGER.debug("Getting a widget with id: "+id);
        return widgets.get(id);
    }
    
    @GET
    @Path("/hello/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello(@PathParam("name") String name) {
    	LOGGER.debug("Hello: "+name);
    	return "Hello "+name;
    }

    @POST
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON})
    @Path("/widgets/{id}")
    public void saveWidget(Widget widget) {
    	LOGGER.debug("Received a widget: "+widget);
    	widgets.put(widget.getId(), widget);
    }
    
    @GET
    @Path("/toggle/{val}")
    public void toggle(@PathParam("val") Integer val) {
    	toggled = val;
    	LOGGER.info("Toggle has been switched: "+ ((toggled == 1) ? "on" : "off"));
    }

}
