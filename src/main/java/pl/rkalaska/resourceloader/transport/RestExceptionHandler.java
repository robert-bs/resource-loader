package pl.rkalaska.resourceloader.transport;

import org.xml.sax.SAXParseException;
import pl.rkalaska.resourceloader.transport.exceptions.RestException;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.NotSupportedException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RestExceptionHandler implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception e) {
        if (e instanceof RestException || e instanceof SAXParseException) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        if (e instanceof NotFoundException) {
            return Response.status(Response.Status.NOT_FOUND).entity("No api provided under current url").build();
        }
        if (e instanceof NotSupportedException) {
            return Response.status(Response.Status.UNSUPPORTED_MEDIA_TYPE).entity(e.getMessage()).build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
    }

}
