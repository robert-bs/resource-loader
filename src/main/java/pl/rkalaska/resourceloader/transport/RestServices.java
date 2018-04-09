package pl.rkalaska.resourceloader.transport;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/rest")
public class RestServices extends Application {

    private final Set<Class<?>> classes = new HashSet<Class<?>>();

    public RestServices() {
    classes.add(TaskRequestService.class);
    classes.add(ResourceService.class);

    }

    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }

}

