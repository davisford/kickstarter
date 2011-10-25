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
		
	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response upload(MultipartBody body) {
  		List<Attachment> attachments = body.getAllAttachments();
  		
  		Attachment a = body.getAttachment("file-metadata");

		try {
			DataHandler metafileHandler = attachments.get(0).getDataHandler();
			DataHandler filestreamHandler = attachments.get(1).getDataHandler();
			FileInfo fileInfo = getFileMetadataFromInputStream(metafileHandler.getInputStream());
			fileInfo.setId(sequence++);
			files.put(fileInfo.getId(), fileInfo);
			System.out.println("\t received: "+fileInfo);
			File file = getFileFromInputStream(fileInfo, filestreamHandler.getInputStream());
			System.out.println("\t wrote: "+file.getAbsolutePath());
		} catch(Exception ex) {
			ex.printStackTrace();
			throw new WebApplicationException(500);
		}
		return Response.ok().build();
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
			int read = 0;
			byte[] bytes = new byte[1024];
			while( (read = is.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
		} finally {
			if(out != null) { out.close(); }
		}
		return file;
	}

}
