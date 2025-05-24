package org.iam.idp;

import org.iam.dto.EmployeeDTO;

public interface CoreIDPClient {

    String getUserNameFromToken(String token);
    Boolean createUser(EmployeeDTO employeeDTO);
}
