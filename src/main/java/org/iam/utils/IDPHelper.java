package org.iam.utils;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.iam.idp.CoreIDPClient;
import org.eclipse.microprofile.config.ConfigProvider;

@Getter
@ApplicationScoped
@Slf4j
public class IDPHelper {

    private static final String SELECTED_IDP = ConfigProvider.getConfig()
            .getOptionalValue("selected.idp", String.class).orElse("keycloakClient");
    private final CoreIDPClient coreIDPClient;
    @Inject
    public IDPHelper(Instance<CoreIDPClient> coreIDPClient) {
        try {
        this.coreIDPClient = coreIDPClient.select(new NamedLiteral(SELECTED_IDP)).get();
        } catch (Exception e) {
            log.error("Facing issue while getting coreIDPClient {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
