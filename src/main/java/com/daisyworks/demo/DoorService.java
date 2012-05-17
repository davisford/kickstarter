package com.daisyworks.demo;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

/**
 * This is the service class that handles the web browser requests
 */
@Path("/")
public class DoorService {

	// map of USB ports
    private static Map<Integer, Port> map = new HashMap<Integer, Port>();
    
    private static final Logger LOGGER = Logger.getLogger(DoorService.class);
    
    // current state of door
	private int toggled;
    
	/**
	 * Constructor
	 */
    public DoorService() {
    	// FIXME: remove this auto-generated map to show data in browser
    	map.put(1, new Port(1, "/dev/ttyUSB0"));
    	map.put(2, new Port(2, "/dev/ttyUSB1"));
    	LOGGER.info("---------------- Widget Service CTOR ------------------");
    }

    /**
     * Fetch the list of ports; returns a JSON document with each {@link Port} object rendered as JSON
     * @return a JSON document with each {@link Port} object rendered as JSON
     */
    @GET
    @Path("/door/ports")
    @Produces(MediaType.APPLICATION_JSON)
    public PortCollection getPorts() {
    	LOGGER.debug("Getting all ports");
        return new PortCollection(map.values());
    }
    
    /**
     * Toggle the state of the door on/off
     * @param portId the id of the USB port you want to control
     * @param val the value of the door where <tt>1 => open</tt> and <tt>0 => closed</tt>
     * @return A string that indicates if it was successful or not as "true" or "false"
     */
    @GET
    @Path("/door/toggle/{portId}/{val}")
    @Produces(MediaType.TEXT_PLAIN)
    public String toggle(@PathParam("portId") Integer portId, @PathParam("val") Integer val) {
    	toggled = val;
    	if(map.containsKey(portId)) {
    		Port port = map.get(portId);
    		LOGGER.info("Toggle USB port "+port.getPath()+" =>" + ((toggled == 1) ? "on" : "off"));
    		return "true";
    	} else {
    		LOGGER.error("No port found for that id: "+portId);
    		return "false";
    	}
    }

}
