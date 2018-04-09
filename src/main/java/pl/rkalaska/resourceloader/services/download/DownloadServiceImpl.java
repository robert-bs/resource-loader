package pl.rkalaska.resourceloader.services.download;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import pl.rkalaska.resourceloader.services.configuration.ConfigurationService;
import pl.rkalaska.resourceloader.services.exceptions.TooBigFileException;
import pl.rkalaska.resourceloader.services.exceptions.UnableToAccessResourceException;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;

@Stateless
public class DownloadServiceImpl implements IDownloadService {

    private static final Log LOG = LogFactory.getLog(DownloadServiceImpl.class);

    @Inject
    private
    ConfigurationService configService;

    @Override
    public InputStream getResourceAsStream(String url){

        CloseableHttpClient client = prepareClient();
        HttpGet request = new HttpGet(url);
        byte[] content = null;
        try {
            HttpResponse response = client.execute(request);
            return response.getEntity().getContent();
        }catch(IOException e) {
            LOG.error("Unable to get resource from: "+url);
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public byte[] getResource(String url){

        CloseableHttpClient client = prepareClient();
        HttpGet request = new HttpGet(url);
        byte[] content = null;
        try {
            HttpResponse response = client.execute(request);
            content = IOUtils.toByteArray(response.getEntity().getContent());
        }catch(IOException e) {
            LOG.error("Unable to get resource from: "+url);
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content;
    }

    @Override
    public void validateResourceSize(String url) throws TooBigFileException, UnableToAccessResourceException {
        CloseableHttpClient client = prepareClient();
        HttpGet request = new HttpGet(url);
        try {
            HttpResponse response = client.execute(request);
            if(response.getEntity().getContentLength() > configService.getConfiguration().getMaxContentLength()) {
                LOG.warn("Will not download resource cause its too big for current application settings url: "+url+" size: "+response.getEntity().getContentLength()+" current max size: "+configService.getConfiguration().getMaxContentLength());
                throw new TooBigFileException("File is bigger than current set max file size - file content: "+response.getEntity().getContentLength() +" configuration size: "+configService.getConfiguration().getMaxContentLength());
            }
        }catch(IOException e) {
            LOG.error("Unable to get resource from: "+url);
            e.printStackTrace();
            throw new UnableToAccessResourceException(e.getMessage());
        }finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private CloseableHttpClient prepareClient() {
        int timeout =  configService.getConfiguration().getDownloadTimeoutSeconds();

        HttpHost proxyHost = null;
        CredentialsProvider credProv = null;
        if(configService.getConfiguration().getProxy() != null) {
            proxyHost = configService.getConfiguration().getProxy().getProxyHost();
            credProv = configService.getConfiguration().getProxy().getCredProv();
        }

        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeout* 1000)
                .setConnectionRequestTimeout(timeout * 1000)
                .setSocketTimeout(timeout * 1000)
                .build();

        CloseableHttpClient client;

        if(proxyHost != null) {
            if(credProv != null) {
                client = HttpClientBuilder.create().setDefaultRequestConfig(config).setProxy(proxyHost).setDefaultCredentialsProvider(credProv).build();
            } else {
                client = HttpClientBuilder.create().setDefaultRequestConfig(config).setProxy(proxyHost).build();
            }
        } else {
            client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
        }

        return client;
    }
}
