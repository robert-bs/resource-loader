package pl.rkalaska.resourceloader.model;

public class ApplicationConfiguration {

    private Integer downloadTimeoutSeconds=4;
    private Integer downloadRetryMin=2;
    private Integer dowaloadMaxRetry=3;
    private ProxyConfig proxy;
    private Integer maxContentLength=20971520;
    private boolean searchForPatternWithStream = true;

    public ApplicationConfiguration(Integer downloadTimeoutSeconds, Integer downloadRetryMin, Integer dowaloadMaxRetry, ProxyConfig proxy, Integer maxContentLength, Boolean searchForPatternWithStream) {
        this.downloadTimeoutSeconds = downloadTimeoutSeconds;
        this.downloadRetryMin = downloadRetryMin;
        this.dowaloadMaxRetry = dowaloadMaxRetry;
        this.proxy = proxy;
        this.maxContentLength = maxContentLength;
        this.searchForPatternWithStream = searchForPatternWithStream;
    }

    public ApplicationConfiguration() {

    }

    public Integer getDownloadTimeoutSeconds() {
        return downloadTimeoutSeconds;
    }

    public Integer getDownloadRetryMin() {
        return downloadRetryMin;
    }

    public Integer getDowaloadMaxRetry() {
        return dowaloadMaxRetry;
    }

    public ProxyConfig getProxy() {
        return proxy;
    }

    public Integer getMaxContentLength() {
        return maxContentLength;
    }

    public boolean isSearchForPatternWithStream() {
        return searchForPatternWithStream;
    }

    @Override
    public String toString() {
        return "ApplicationConfiguration{" +"\n"+
                "downloadTimeoutSeconds=" + downloadTimeoutSeconds +"\n"+
                "downloadRetryMin=" + downloadRetryMin +"\n"+
                "dowaloadMaxRetry=" + dowaloadMaxRetry +"\n"+
                "maxContent Length = " +maxContentLength+"\n"+
                "search for Pattern with stream = " +searchForPatternWithStream+"\n"+
                "proxy= "+proxy+
                '}';
    }
}
