package com.example.fileupload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;




@Path("/fileupload/")
@Produces(MediaType.APPLICATION_XML)
public class FileUploadServiceImpl implements FileUploadService {

    private static Map<Integer, FileUploadFile> files = new HashMap<Integer, FileUploadFile>();

    static {
        files.put(1, new FileUploadFile(1, "foo"));
        files.put(2, new FileUploadFile(2, "bar"));
        files.put(3, new FileUploadFile(3, "baz"));
    }

    public FileUploadServiceImpl() {
    }

    @GET
    @Path("/files")
    @Override
    public FileUploadFileCollection getFiles() {
        return new FileUploadFileCollection(files.values());
    }

    @GET
    @Path("/file/{id}")
    @Override
    public FileUploadFile getFile(@PathParam("id") Integer id) {
        return files.get(id);
    }

	@Override
	@POST
	@Path("/")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public FileUploadFileResult create(
			@Multipart(value="file-metadata", type=MediaType.APPLICATION_XML) FileUploadFile file, 
			@Multipart(value="file-content", type=MediaType.APPLICATION_OCTET_STREAM) InputStream istream) {
		System.err.println("create called: "+file+" \n input stream? "+istream);
		return new FileUploadFileResult("success");
	}
	
	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public FileUploadFileResult upload(MultipartBody body) {
		List<Attachment> attachments = body.getAllAttachments();
		Attachment att = body.getRootAttachment();
		
		DataHandler dataHandler = attachments.get(0).getDataHandler();
		File file = new File("the-file.txt");
		FileOutputStream out = null;
		
		int read = 0;
		byte[] bytes = new byte[1024];
		
		try {
			InputStream in = dataHandler.getInputStream();
			out = new FileOutputStream(file);
			while( (read = in.read(bytes)) != -1 ) {
				out.write(bytes, 0, read);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		System.out.println("\t WROTE THE FILE: "+file.getAbsolutePath());
		return new FileUploadFileResult("success");
	}

}
