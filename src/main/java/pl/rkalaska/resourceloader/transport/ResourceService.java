package pl.rkalaska.resourceloader.transport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import pl.rkalaska.resourceloader.DAO.IResourceDAO;
import pl.rkalaska.resourceloader.entity.ResourceDTO;
import pl.rkalaska.resourceloader.transport.exceptions.RestException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Set;


@Path("/resource")
@RequestScoped
public class ResourceService {


    private static final Log LOG = LogFactory.getLog(ResourceService.class);

    @Inject
    private
    IResourceDAO resourceDAO;

    @GET
    @Path("/all")
    public Response getAllResources() throws RestException {
       Set<ResourceDTO> resources = resourceDAO.getAllResources();
       if(resources.isEmpty()) {
           throw new RestException("No resource in system");
       }
       JSONArray array = new JSONArray();
        for(ResourceDTO r : resources) {
            array.put(new JSONObject().put("id",r.getId())
            .put("url",r.getUrl())
            .put("timestamp",r.getTimestamp()));

        }

        return Response.status(200).entity(array.toString()).build();
    }

    @GET
    @Path("/one/{idString}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getResourceById(@PathParam("idString") String idString) throws RestException {
        Long id;

        try {
            id = Long.valueOf(idString);
        }catch(NumberFormatException e) {
            throw new RestException("Passed ID is not valid number format!");
        }

        ResourceDTO resource = resourceDAO.getResource(id);
        if(resource == null) {
            throw new RestException("No resource for passed ID: "+id);
        }

        try {
            InputStream is = resource.getContent().getBinaryStream();
            Response.ResponseBuilder responseBuilder = Response.ok(is);
            responseBuilder.header("Content-Disposition", "attachment; filename=" + id);
            return responseBuilder.build();
        } catch(SQLException e) {
            LOG.error("Unable to handle blob content for client request");
            e.printStackTrace();
            return Response.status(500).entity("Unable to handle blob content").build();
        }

    }

    @GET
    @Path("/pattern/{patternString}")
    public Response getResourceByPattern(@PathParam("patternString") String pattern) throws RestException {

        Set<ResourceDTO> resources = resourceDAO.getResourceByPattern(pattern);
        if(resources.isEmpty()) {
            throw new RestException("No resource for passed pattern: "+pattern);
        }
        JSONArray array = new JSONArray();
        for(ResourceDTO r : resources) {
            array.put(new JSONObject().put("id",r.getId())
                    .put("url",r.getUrl())
                    .put("timestamp",r.getTimestamp()));

        }

        return Response.status(200).entity(array.toString()).build();

    }
}
