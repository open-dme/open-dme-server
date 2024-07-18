package io.github.opendme.server.service;

import io.github.opendme.server.config.KeycloakConfig;
import io.github.opendme.server.entity.Member;
import io.github.opendme.server.service.keycloak.KeycloakInitializer;
import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.core.Response;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collections;
import java.util.List;

@Service
public class KeycloakService {
    private KeycloakConfig config;
    private Keycloak keycloak;
    private final KeycloakInitializer keycloakInitializer;

    public KeycloakService(KeycloakInitializer keycloakInitializer, Keycloak keycloak, KeycloakConfig config) {
        this.keycloakInitializer = keycloakInitializer;
        this.keycloak = keycloak;
        this.config = config;
    }

    @PostConstruct
    public void init() {
        keycloakInitializer.init(false);
    }

    public List<UserRepresentation> getInstanceAdmins() {
        List<UserRepresentation> admin = realm()
                .groups()
                .group("admin")
                .members();
        return Collections.emptyList();
    }

    public void createGroup(String group) {
        GroupRepresentation rep = new GroupRepresentation();
        rep.setName("admin");
        var add = realm()
                .groups()
                .add(rep);
    }

    private RealmResource realm() {
        return keycloak.realm(config.getRealm());
    }

    public void createUser(Member memberDto) {
        UserRepresentation rep = createKeycloakUser(memberDto);

        try (var res = realm().users().create(rep)) {
            if (res.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
                return;
            }
            // TODO: Check whether this is actually the best way.
            throw new HttpClientErrorException(HttpStatusCode.valueOf(res.getStatus()), res.getStatusInfo().getReasonPhrase());
        }
    }

    private static UserRepresentation createKeycloakUser(Member member) {
        UserRepresentation rep = new UserRepresentation();
        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setType(OAuth2Constants.PASSWORD);
        cred.setValue("abc"); // TODO: Set actual random password
        cred.setTemporary(true);
        rep.setRequiredActions(List.of("UPDATE_PASSWORD"));
        rep.setUsername(member.getName().toLowerCase().replace(" ", "."));
        rep.setEmail(member.getEmail());
        rep.setCredentials(List.of(cred));
        rep.setEnabled(true);
        rep.setGroups(List.of("user"));
        return rep;
    }
}
