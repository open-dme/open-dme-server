package io.github.opendme.server.service.keycloak;

import io.github.opendme.server.config.AppConfig;
import io.github.opendme.server.config.KeycloakConfig;
import jakarta.ws.rs.NotFoundException;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class KeycloakInitializer {
    private static final Logger log = getLogger(KeycloakInitializer.class);

    private final AppConfig appConfig;
    private final KeycloakConfig keycloakConfig;
    private final Keycloak keycloak;

    public KeycloakInitializer(AppConfig appConfig,
                               KeycloakConfig keycloakConfig,
                               Keycloak keycloak) {
        this.appConfig = appConfig;
        this.keycloakConfig = keycloakConfig;
        this.keycloak = keycloak;
    }

    public void init(boolean overwrite) {
        log.info("Initializer start");

        List<RealmRepresentation> realms;
        try {
            realms = keycloak.realms().findAll();
        } catch (RuntimeException e) {
            log.error("Could not retrieve available realms.", e);
            throw new RuntimeException(e);
        }
        boolean isAlreadyInitialized =
                realms.stream().anyMatch(realm -> realm.getId().equals(keycloakConfig.getRealm()));

        if (isAlreadyInitialized && overwrite) {
            reset();
        }

        if (!isAlreadyInitialized || overwrite) {
            initKeycloak();
            log.info("Keycloak initialized successfully");
        } else {
            log.warn("Keycloak initialization cancelled: realm already exist");
        }
    }

    private void initKeycloak() {
        initKeycloakRealm();
        initKeycloakGroups();
        initAdmin();
    }

    private void initKeycloakRealm() {
        RealmRepresentation realmRepresentation = new RealmRepresentation();
        realmRepresentation.setRealm(keycloakConfig.getRealm());
        realmRepresentation.setId(keycloakConfig.getRealm());

        keycloak.realms().create(realmRepresentation);
    }

    private void initKeycloakGroups() {
        GroupRepresentation group = new GroupRepresentation();
        group.setName("admin");
        group.setId("admin");
        try (var res = keycloak.realm(keycloakConfig.getRealm())
                               .groups()
                               .add(group)) {
            // TODO
            if (res.getStatus() >= 200 && res.getStatus() < 300) {
                log.info("Created default admin group");
            }
        } catch (RuntimeException e) {
            log.error("Could not create admin group", e);
        }
    }

    private void initAdmin() {
        UserRepresentation user = new UserRepresentation();
        AppConfig.User owner = appConfig.getInstanceOwner();
        user.setUsername(owner.getName());
        user.setEnabled(true);
        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setType(CredentialRepresentation.PASSWORD);
        cred.setValue(owner.getName());
        user.setCredentials(List.of(cred));
        user.setGroups(List.of("admin"));
        try (var res = keycloak.realm(keycloakConfig.getRealm())
                               .users()
                               .create(user)) {
            // TODO
            if (res.getStatus() >= 200 && res.getStatus() < 300) {
                log.info("Created default user with credentials from configuration");
            }
        } catch (RuntimeException e) {
            log.error("Could not create admin user", e);
        }

    }

    public void reset() {
        try {
            keycloak.realm(keycloakConfig.getRealm()).remove();
        } catch (NotFoundException e) {
            log.error("Failed to reset Keycloak", e);
        }
    }
}
