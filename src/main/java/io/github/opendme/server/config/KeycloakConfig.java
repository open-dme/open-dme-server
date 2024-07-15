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

    public void baseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void url(String url) {
        this.url = url;
    }

    public void masterRealm(String masterRealm) {
        this.masterRealm = masterRealm;
    }

    public void realm(String realm) {
        this.realm = realm;
    }

    public void clientId(String clientId) {
        this.clientId = clientId;
    }

    public void clientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public void initializeOnStartup(boolean initializeOnStartup) {
        this.initializeOnStartup = initializeOnStartup;
    }

    public String baseUrl() {
        return baseUrl;
    }

    public String url() {
        return url;
    }

    public String masterRealm() {
        return masterRealm;
    }

    public String realm() {
        return realm;
    }

    public String clientId() {
        return clientId;
    }

    public String clientSecret() {
        return clientSecret;
    }

    public boolean initializeOnStartup() {
        return initializeOnStartup;
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
