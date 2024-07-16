package io.github.opendme.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
@ConfigurationProperties(prefix = "keycloak")
public class KeycloakConfig {
    private String baseUrl;
    private String url;
    private String masterRealm;
    private String realm;
    private String clientId;
    private String clientSecret;
    private boolean initializeOnStartup;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMasterRealm() {
        return masterRealm;
    }

    public void setMasterRealm(String masterRealm) {
        this.masterRealm = masterRealm;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public boolean isInitializeOnStartup() {
        return initializeOnStartup;
    }

    public void setInitializeOnStartup(boolean initializeOnStartup) {
        this.initializeOnStartup = initializeOnStartup;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (KeycloakConfig) obj;
        return Objects.equals(this.baseUrl, that.baseUrl) &&
                Objects.equals(this.url, that.url) &&
                Objects.equals(this.masterRealm, that.masterRealm) &&
                Objects.equals(this.realm, that.realm) &&
                Objects.equals(this.clientId, that.clientId) &&
                Objects.equals(this.clientSecret, that.clientSecret) &&
                this.initializeOnStartup == that.initializeOnStartup;
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseUrl, url, masterRealm, realm, clientId, clientSecret, initializeOnStartup);
    }

    @Override
    public String toString() {
        return "KeycloakConfig[" +
                "baseUrl=" + baseUrl + ", " +
                "url=" + url + ", " +
                "masterRealm=" + masterRealm + ", " +
                "realm=" + realm + ", " +
                "clientId=" + clientId + ", " +
                "clientSecret=" + clientSecret + ", " +
                "initializeOnStartup=" + initializeOnStartup + ']';
    }

}
