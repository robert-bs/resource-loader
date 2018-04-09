package pl.rkalaska.resourceloader.services.configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import pl.rkalaska.resourceloader.model.ApplicationConfiguration;
import pl.rkalaska.resourceloader.model.ProxyConfig;

import javax.ejb.Singleton;
import javax.ejb.Startup;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Startup
@Singleton
public  class ConfigurationService {

    private static final String FILENAME="application.properties";
    private static final Log LOG = LogFactory.getLog(ConfigurationService.class);

    private static ApplicationConfiguration configInstance = null;

    static {
        InputStream input = null;
        try {
            Properties prop = new Properties();


            input = ConfigurationService.class.getClassLoader().getResourceAsStream(FILENAME);
            if(input==null){
                LOG.error("Unable to get application configuration file! System will use default values");
                createInstance(null,null,null, null, null, null);
            } else {

                prop.load(input);

                createInstance(
                        prop.getProperty("downloadTimeoutSeconds"),
                        prop.getProperty("downloadRetryMin"),
                        prop.getProperty("downloadMaxRetry"),
                        proxyLoad(prop),
                        prop.getProperty("maxContentLength"),
                        prop.getProperty("searchForPatternWithStream")
                );

                LOG.info("Application config loaded properly:"+configInstance.toString());

            }

        } catch (IOException ex) {
            LOG.error("Unable to get application configuration file! System will use default values");
            createInstance(null,null,null, null, null, null);
            ex.printStackTrace();
        } finally{
            if(input!=null){
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private static ProxyConfig proxyLoad(Properties prop) {
        try {
            ProxyConfig proxyConfig = new ProxyConfig();

            if (prop.getProperty("downloadProxyUse") != null) {
                Boolean useProxy = Boolean.valueOf(prop.getProperty("downloadProxyUse"));
                if (useProxy) {
                    String host = prop.getProperty("downloadProxyHost");
                    String port = prop.getProperty("downloadProxyPort");
                    String user = prop.getProperty("downloadProxyUser");
                    String password = prop.getProperty("downloadProxyPassword");

                    if (host == null || port == null) {
                        LOG.error("Proxy set as active but invalid host or port setting");
                        return null;
                    }
                    Integer portNumber;
                    try {
                        portNumber = Integer.valueOf(port);
                    } catch (NumberFormatException e) {
                        LOG.error("Proxy port invalid format");
                        return null;
                    }

                    HttpHost proxy = new HttpHost(host, portNumber);

                    proxyConfig.setProxyHost(proxy);

                    Credentials credentials;
                    AuthScope authScope;
                    CredentialsProvider credsProvider;

                    if (user != null && password != null) {
                        credentials = new UsernamePasswordCredentials(user, password);
                        authScope = new AuthScope(host, portNumber);
                        credsProvider = new BasicCredentialsProvider();
                        credsProvider.setCredentials(authScope, credentials);
                        proxyConfig.setCredProv(credsProvider);
                    }

                    return proxyConfig;

                } else {
                    return null;
                }
            }
            return null;
        }catch(Exception e) {
            LOG.error("Unable to resolve proxy config");
            return null;
        }

    }

    private static void createInstance(String downloadTimeoutSeconds, String downloadRetryMin, String dowaloadMaxRetry, ProxyConfig proxy, String maxContentLength, String searchOptions) {
        if(configInstance != null) {
            throw new IllegalStateException("Application Configuration may be created only once!");
        }
        if(downloadTimeoutSeconds == null|| dowaloadMaxRetry == null || downloadRetryMin == null ) {
            configInstance = new ApplicationConfiguration();
        } else {
            try {
                configInstance = new ApplicationConfiguration(
                        Integer.valueOf(downloadTimeoutSeconds),
                        Integer.valueOf(downloadRetryMin),
                        Integer.valueOf(dowaloadMaxRetry),
                        proxy,
                        Integer.valueOf(maxContentLength),
                        Boolean.valueOf(searchOptions)
                );
            }catch(NumberFormatException e) {
                LOG.error("Unable to properly parse configuration, will use default");
                configInstance = new ApplicationConfiguration();
            }
        }
    }

    public ApplicationConfiguration getConfiguration() {
        return configInstance;
    }

}
