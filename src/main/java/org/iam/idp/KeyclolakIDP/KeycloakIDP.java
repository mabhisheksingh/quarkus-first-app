package org.iam.idp.KeyclolakIDP;

import org.iam.idp.CoreIDPClient;

public interface KeycloakIDP extends CoreIDPClient {

    Boolean isUserNameExistInKeycloak(String userName);
}
