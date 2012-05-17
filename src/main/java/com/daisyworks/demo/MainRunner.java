package com.daisyworks.demo;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class MainRunner {

	private static final int DEFAULT_PORT = 8080;

	/**
	 * Launches an embedded Jetty server running the webapp herein
	 * 
	 * Specify arg[0] as port number or else it will try 8080
	 * @param args
	 */
	public static void main(String[] args) {
		int port = DEFAULT_PORT;
		if(args.length > 0) {
			try { 
				port = Integer.parseInt(args[0]); 
			} catch(Exception e) {
				System.err.println("Did you mean to specify a port? Re-run me with something like: `java -jar <filename.jar> 8081` for example to run on port 8081; I am going to try port 8080 which is my default.");
			}
		}
		
		try {
			Server server = new Server(port);
			
			final URL waUrl = MainRunner.class.getClassLoader().getResource("webapp");
			final String waUrlString = waUrl.toExternalForm();
			System.err.println("Starting Jetty server on port: "+port+": "+waUrlString);
			
			WebAppContext ctx = new WebAppContext();
			ctx.setWar(waUrlString);
			ctx.setContextPath("/demo");
			ctx.setResourceBase("/WEB-INF/web.xml");
			ctx.setParentLoaderPriority(true);
			
			server.setHandler(ctx);
			server.start();
			server.join();
		} catch(Exception ex) {
			System.err.println("Failed to launch embedded Jetty server \n"+ getStackTraceAsString(ex));
			System.exit(1);
		}
	}
	
	public static String getStackTraceAsString(Throwable t) {
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		t.printStackTrace(printWriter);
		return result.toString();
	}

}
