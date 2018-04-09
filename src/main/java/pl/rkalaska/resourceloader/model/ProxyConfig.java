package pl.rkalaska.resourceloader.model;

import org.apache.http.HttpHost;
import org.apache.http.client.CredentialsProvider;

import java.util.Objects;

public class ProxyConfig {

        private HttpHost proxy = null;
        private CredentialsProvider credProv = null;

        public HttpHost getProxyHost() {
            return proxy;
        }

        public void setProxyHost(HttpHost proxy) {
            this.proxy = proxy;
        }

        public CredentialsProvider getCredProv() {
            return credProv;
        }

        public void setCredProv(CredentialsProvider credProv) {
            this.credProv = credProv;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ProxyConfig that = (ProxyConfig) o;
            return Objects.equals(proxy, that.proxy) &&
                    Objects.equals(credProv, that.credProv);
        }

        @Override
        public int hashCode() {

            return Objects.hash(proxy, credProv);
        }

        @Override
        public String toString() {
            return "ProxyConfig{" +
                    "proxy=" + proxy +
                    ", credProv=" + credProv +
                    '}';
        }
    }