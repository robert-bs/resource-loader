package pl.rkalaska.resourceloader.services.download;

import pl.rkalaska.resourceloader.services.exceptions.TooBigFileException;
import pl.rkalaska.resourceloader.services.exceptions.UnableToAccessResourceException;

import javax.ejb.Local;
import java.io.InputStream;

@Local
public interface IDownloadService {
    InputStream getResourceAsStream(String url);

    byte[] getResource(String url);
    void validateResourceSize(String url) throws TooBigFileException, UnableToAccessResourceException;
}
