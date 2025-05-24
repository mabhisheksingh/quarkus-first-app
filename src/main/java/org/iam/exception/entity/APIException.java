package org.iam.exception.entity;

import jakarta.ws.rs.core.Response;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class APIException extends RuntimeException {
    String errorMessage;
    Integer errorCode = Response.Status.BAD_REQUEST.getStatusCode();
    String errorField;

    public APIException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    public APIException(String errorMessage, Integer errorCode) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public APIException(String errorMessage, Integer errorCode, String errorField) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
        this.errorField = errorField;
    }

}
