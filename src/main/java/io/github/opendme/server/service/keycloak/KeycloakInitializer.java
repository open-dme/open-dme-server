package io.github.opendme.server.service.keycloak;

import io.github.opendme.server.config.KeycloakConfig;
import io.github.opendme.server.config.OpenDmeConfig;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class KeycloakInitializer implements InitializingBean {
    private static final Logger log = getLogger(KeycloakInitializer.class);

    private final Keycloak keycloak;
    private final KeycloakConfig keycloakConfig;
    private final OpenDmeConfig openDmeConfig;

    public KeycloakInitializer(Keycloak keycloak,
                               KeycloakConfig keycloakConfig,
                               OpenDmeConfig openDmeConfig) {
        this.keycloak = keycloak;
        this.keycloakConfig = keycloakConfig;
        this.openDmeConfig = openDmeConfig;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (keycloakConfig.isInitializeOnStartup()) {
            init(false);
        }
    }

    public void init(boolean overwrite) {
        log.info("Initializer start");

        List<RealmRepresentation> realms = keycloak.realms().findAll();
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
        }
    }

    private void initAdmin() {
        UserRepresentation user = new UserRepresentation();
        OpenDmeConfig.User owner = openDmeConfig.instanceOwner();
        user.setUsername(owner.name());
        user.setEnabled(true);
        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setType(CredentialRepresentation.PASSWORD);
        cred.setValue(owner.password());
        user.setCredentials(List.of(cred));
        user.setGroups(List.of("admin"));
        try (var res = keycloak.realm(keycloakConfig.getRealm())
                               .users()
                               .create(user)) {
            // TODO
            if (res.getStatus() >= 200 && res.getStatus() < 300) {
                log.info("Created default user with credentials from configuration");
            }

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
