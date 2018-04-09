package pl.rkalaska.resourceloader.DAO;


import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import pl.rkalaska.resourceloader.DAO.utils.KMPMatch;
import pl.rkalaska.resourceloader.entity.ResourceDTO;
import pl.rkalaska.resourceloader.services.configuration.ConfigurationService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.io.*;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;


@Stateless
@Transactional
public class ResourceDAOBean implements IResourceDAO{

    private static final Log LOG = LogFactory.getLog(ResourceDAOBean.class);
    private static final String RESOURCE_UNIT="psuResource";

    @Inject
    private
    ConfigurationService configService;

    @PersistenceContext(unitName = RESOURCE_UNIT)
    private EntityManager entityManager;

    @Override
    public Set<ResourceDTO> getAllResources() {
        List<ResourceDTO> resources = entityManager.createQuery("SELECT r FROM ResourceDTO r").getResultList();
        return new HashSet<ResourceDTO>(resources);
    }

    @Override
    public ResourceDTO getResource(Long id) {
       Query q = entityManager.createQuery("SELECT r FROM ResourceDTO  r WHERE r.id = :id");
        q.setParameter("id",id);
        List<ResourceDTO> res = q.getResultList();
        if(res.isEmpty()) {
            return null;
        } else {
            return res.get(0);
        }
    }

    @Override
    public ResourceDTO storeResource(ResourceDTO resource) {

        if(resource.getId() != null) {
            entityManager.merge(resource);
        } else {
            entityManager.persist(resource);
        }
        entityManager.flush();
        return resource;
    }

    @Override
    public Set<ResourceDTO> getResourceByPattern(String pattern) {

        Set<ResourceDTO> resources = getAllResources();
        Set<ResourceDTO> result = new HashSet<ResourceDTO>();
        resources.forEach( r -> {
            InputStream is = null;
            try {
                is = r.getContent().getBinaryStream();

                if(configService.getConfiguration().isSearchForPatternWithStream()) {
                    if(compareAsStreamWithScanner(is, pattern)) {
                        result.add(r);
                    }
                } else {
                    if(compareWithKMPMatch(is, pattern)) {
                        result.add(r);
                    }
                }

                is.close();
            } catch (SQLException | IOException e) {
                LOG.error("Error while streaming through BLOB object");
                e.printStackTrace();
            } finally {
                try {
                    if(is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    LOG.error("Unable to close Input Stream of Blob Object");
                    e.printStackTrace();
                }
            }
        });

        return result;
    }

    private boolean compareWithKMPMatch(InputStream is, String searchString) throws IOException {
        String wholeContent = IOUtils.toString(is, "UTF-8");
        KMPMatch kmpAlg = new KMPMatch(wholeContent, searchString);
        return kmpAlg.match();
    }

    private boolean compareAsStreamWithScanner(InputStream is, String searchString) throws UnsupportedEncodingException {
        Scanner streamScanner = new Scanner(new InputStreamReader(is, "UTF-8"));
        if (streamScanner.findWithinHorizon(searchString, 0) != null) {
            return true;
        }
        return false;
    }





}
