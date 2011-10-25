package com.example.fileupload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;

@Path("/fileupload/")
@Produces(MediaType.APPLICATION_XML)
public class FileUploadServiceImpl {

	/** id sequence */
	private static int sequence = 1;
    private static Map<Integer, FileInfo> files = new HashMap<Integer, FileInfo>();
    
    /* tracks file upload progress */
    private static Map<String, Double> progress = new HashMap<String, Double>();

    public FileUploadServiceImpl() {
    }

    @GET
    @Path("/files")
    public FileInfoCollection getFiles() {
        return new FileInfoCollection(files.values());
    }

    @GET
    @Path("/file/{id}")
    public FileInfo getFile(@PathParam("id") Integer id) {
        return files.get(id);
    }
    
    @GET
    @Path("/upload/progress/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    public String progress(@PathParam("name") String fileName) {
    	if(progress.containsKey(fileName)) {
    		//return Response.ok(progress.get(fileName)).build();
    		return progress.get(fileName).toString();
    	} else {
    		// doesn't exist so progress is 100%
    		//return Response.ok("100").build();
    		return "100";
    	}
    }
		
	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response upload(MultipartBody body) {
  		List<Attachment> attachments = body.getAllAttachments();
		try {
			// parse <file> XML into object
			DataHandler fileInfoHandler = attachments.get(0).getDataHandler();
			FileInfo fileInfo = getFileMetadataFromInputStream(fileInfoHandler.getInputStream());
			// generate a new id
			fileInfo.setId(sequence++);
			// add it to the map
			files.put(fileInfo.getId(), fileInfo);
			// set the progress to zero
			progress.put(fileInfo.getName(), 0.0);
			System.out.println("\t received: "+fileInfo);
			
			// parse the input stream of the file
			DataHandler fileHandler = attachments.get(1).getDataHandler();
			File file = getFileFromInputStream(fileInfo, fileHandler.getInputStream());
			System.out.println("\t wrote: "+file.getAbsolutePath());
		} catch(Exception ex) {
			ex.printStackTrace();
			throw new WebApplicationException(500);
		}
		return Response.ok("success").build();
	}
	
	private FileInfo getFileMetadataFromInputStream(InputStream is) throws Exception {
		JAXBContext c = JAXBContext.newInstance(new Class[]{FileInfo.class});
		Unmarshaller u = c.createUnmarshaller();
		FileInfo file = (FileInfo) u.unmarshal(is);
		return file;
	}
	
	private File getFileFromInputStream(FileInfo fileInfo, InputStream is) throws Exception {
		FileOutputStream out = null;
		File file = new File("./target/"+fileInfo.getName());
		fileInfo.setPath(file.getAbsolutePath());
		try {
			out = new FileOutputStream(file);
			int read, amount = 0;
			// use this to update progress
			int total = fileInfo.getSize();
			
			byte[] bytes = new byte[1024];
			while( (read = is.read(bytes)) != -1) {
				amount += read;
				Double percent = ( (double)amount / (double)total )	* 100;
				progress.put(fileInfo.getName(), percent );
				out.write(bytes, 0, read);
			}
		} finally {
			if(out != null) { out.close(); }
		}
		return file;
	}

}
