package com.example.fileupload;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.ext.multipart.Multipart;



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
			FileUploadFile file, 
			@Multipart(value="file", type=MediaType.APPLICATION_OCTET_STREAM) InputStream istream) {
		System.err.println("create called: "+file+" \n input stream? "+istream);
		return new FileUploadFileResult("success");
	}

}
