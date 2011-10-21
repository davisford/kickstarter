package com.example.fileupload;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;



@Path("/fileupload/")
@Produces("application/xml")
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

    @GET
    @Path("/files/bad")
    @Override
    public Response getBadRequest() {
        return Response.status(Status.BAD_REQUEST).build();
    }

}
