package org.iam.idp.impl;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.iam.dto.EmployeeDTO;
import org.iam.idp.KeyclolakIDP.KeycloakIDP;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

@Slf4j
@ApplicationScoped
@Named("keycloakClient")
public class KeycloakClient implements KeycloakIDP {

    @ConfigProperty(name = "quarkus.oidc.auth-server-url")
    String serverUrl;

    @ConfigProperty(name = "quarkus.oidc.client-id")
    String clientId;

    @ConfigProperty(name = "quarkus.oidc.client-secret")
    String clientSecret;

    @ConfigProperty(name = "selected.organization")
    String realm;

    private Keycloak adminClient;

    private final static String GRANT_TYPE = "client_credentials";

    private Keycloak getAdminClient() {
        log.info("serverUrl {}", serverUrl);
        log.info("realm {}", realm);
        log.info("clientId {}", clientId);
        log.info("clientSecret {}", clientSecret);
        try {
            return KeycloakBuilder.builder()
                    .serverUrl(serverUrl)
                    .realm(realm)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .grantType(GRANT_TYPE)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }


    @PostConstruct
    void initAdminKeycloak() {
        serverUrl = serverUrl.substring(0, serverUrl.indexOf("realms"));
        log.info("New serverUrl {}", serverUrl);
        this.adminClient = getAdminClient();
    }


    @Override
    public Boolean isUserNameExistInKeycloak(String userName) {
        List<UserRepresentation> search = this.adminClient.realm(realm).users().search(userName, true);
        return !search.isEmpty();
    }

    @Override
    public String getUserNameFromToken(String token) {
        return "";
    }

    @Override
    public Boolean createUser(EmployeeDTO employeeDTO) {
        log.info("Creating user in keycloak {}", employeeDTO);
        final UserRepresentation userRepresentation = getUserRepresentation(employeeDTO, false, true, true);
        if (isUserNameExistInKeycloak(userRepresentation.getUsername())) {
            return false;
        }
        try (Response response = adminClient.realm(realm).users().create(userRepresentation)) {

            if (response.getStatus() == 201) {
                return true;
            } else if (response.getStatus() == Response.Status.CONFLICT.getStatusCode()) {
                log.error("User already exists");
                return false;
            } else if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode() || response.getStatus() == Response.Status.UNAUTHORIZED.getStatusCode() || response.getStatus() == Response.Status.FORBIDDEN.getStatusCode()) {
                log.error("Bad request/Unauthorized/Forbidden");
                return false;
            }
            return false;

        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    private CredentialRepresentation getCredentialRepresentation(String password, Boolean isTemporaryPassword) {
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setTemporary(isTemporaryPassword);
        credentialRepresentation.setValue(password);
        return credentialRepresentation;
    }

    private UserRepresentation getUserRepresentation(EmployeeDTO employeeDTO, Boolean isTemporaryPassword, Boolean isEnabled, Boolean isEmailVerified) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(employeeDTO.getUserName());
        userRepresentation.setEmail(employeeDTO.getEmail());
        userRepresentation.setFirstName(employeeDTO.getFirstName());
        userRepresentation.setLastName(employeeDTO.getLastName());
        userRepresentation.setEnabled(isEnabled);
        userRepresentation.setEmailVerified(isEmailVerified);

        userRepresentation.setCredentials(List.of(getCredentialRepresentation(employeeDTO.getPassword(), isTemporaryPassword)));

        CredentialRepresentation credentialRepresentation = getCredentialRepresentation(employeeDTO.getPassword(), isTemporaryPassword);
        userRepresentation.setCredentials(List.of(credentialRepresentation));
        return userRepresentation;
    }
}
