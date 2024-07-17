package io.github.opendme.server.service.keycloak;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.opendme.server.config.AppConfig;
import io.github.opendme.server.config.KeycloakConfig;
import jakarta.ws.rs.NotFoundException;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
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

        boolean realmExists =
                realms.stream().anyMatch(realm -> realm.getRealm().equals(keycloakConfig.getRealm()));

        if (!realmExists) {
            //TODO proper exception
            throw new RuntimeException("Realm not found. Please import realm from docker/kc_data/realm-export.json. Have a look at our compose file for reference or the keycloak manual.");
        }

        initKeycloak();
    }

    private void initKeycloak() {
        log.info("Enforcing keycloak consistency");
        KeycloakDefaults defaults;
        try (var in = getClass().getClassLoader().getResourceAsStream("keycloak.json")) {
            defaults = new ObjectMapper().readValue(new String(in.readAllBytes()), KeycloakDefaults.class);
        } catch (IOException e) {
            throw new RuntimeException("Could not read keycloak.json from resources", e);
        }
        initKeycloakGroups(defaults);
        initAdmin();
    }

    private void initKeycloakGroups(KeycloakDefaults defaults) {
        for (GroupRepresentation group : defaults.groups) {
            GroupRepresentation createdGroup;
            try {
                createdGroup = realmReq().getGroupByPath("/" + group.getName());
                createdGroup.merge(group);
                createGroup(createdGroup);
            } catch (NotFoundException e) {
                createGroup(group);
                createdGroup = realmReq().getGroupByPath("/" + group.getName());
            }
            updateSubGroups(createdGroup, group.getSubGroups());
        }
    }

    private void updateSubGroups(GroupRepresentation group, List<GroupRepresentation> subGroups) {
        for (GroupRepresentation subGroup : subGroups) {
            GroupRepresentation resolved = realmReq().getGroupByPath("/" + subGroup.getName());
            try (var res = realmReq().groups().group(group.getId()).subGroup(resolved)) {
                if (res.getStatus() >= 200 && res.getStatus() < 300) {
                    log.info("Updated sub groups for group %s:%s".formatted(group.getId(), group.getName()));
                } else {
                    log.error("Could not create or update sub groups for %s".formatted(group.getName()));
                    throw new RuntimeException("Could not create or update sub groups for %s: %s".formatted(group.getName(), res.getStatusInfo().getReasonPhrase()));
                }

            }

        }

    }

    private void createGroup(GroupRepresentation group) {
        log.info("Attempting to create or update group %s:%s".formatted(group.getId(), group.getName()));
        try (var res = realmReq().groups().add(group)) {
            if (res.getStatus() >= 200 && res.getStatus() < 300) {
                log.info("Created or updated group %s:%s".formatted(group.getId(), group.getName()));
            } else {
                log.error("Could not create or update group %s".formatted(group.getName()));
                throw new RuntimeException("Could not create or update group %s: %s".formatted(group.getName(), res.getStatusInfo().getReasonPhrase()));
            }
        }
    }

    public RealmResource realmReq() {
        return keycloak.realm(keycloakConfig.getRealm());
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

    private record KeycloakDefaults(List<GroupRepresentation> groups, List<RoleRepresentation> roles) {
    }
}
