package pl.rkalaska.resourceloader.DAO;

import pl.rkalaska.resourceloader.entity.ResourceDTO;

import javax.ejb.Local;
import java.util.Set;

@Local
public interface IResourceDAO {
    Set<ResourceDTO>  getAllResources();
    ResourceDTO getResource(Long id);
    ResourceDTO storeResource(ResourceDTO resource);
    Set<ResourceDTO> getResourceByPattern(String pattern);


}
