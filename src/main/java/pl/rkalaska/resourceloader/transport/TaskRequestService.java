package pl.rkalaska.resourceloader.transport;

import org.apache.commons.validator.UrlValidator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import pl.rkalaska.resourceloader.jms.JmsProducer;
import pl.rkalaska.resourceloader.model.TaskModel;
import pl.rkalaska.resourceloader.services.download.IDownloadService;
import pl.rkalaska.resourceloader.services.exceptions.TooBigFileException;
import pl.rkalaska.resourceloader.services.exceptions.UnableToAccessResourceException;
import pl.rkalaska.resourceloader.transport.exceptions.RestException;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/download")
public class TaskRequestService {

    @Inject
    private IDownloadService downloadService;

    @Inject
    private JmsProducer taskProducer;

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    public Response startTask(Document doc) throws RestException {

        NodeList nodes = doc.getElementsByTagName("url");
        if(nodes.getLength() == 1) {
            Element urlElement = (Element) nodes.item(0);
            String url = urlElement.getFirstChild().getNodeValue();
            UrlValidator urlValidator = new UrlValidator();
            if(urlValidator.isValid(url)) {
                try {
                    downloadService.validateResourceSize(url);
                }catch(TooBigFileException | UnableToAccessResourceException e) {
                    throw new RestException(e.getMessage());
                }
                taskProducer.sendMessage(new TaskModel(url));
                return Response.status(200).entity("Task started correctly").build();
            } else {
                throw new RestException("Invalid URL send");
            }
        }
        throw new RestException("Unable to get URL from request");

    }



}
