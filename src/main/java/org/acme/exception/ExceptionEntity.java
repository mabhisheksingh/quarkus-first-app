package org.acme.exception;

import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ExceptionEntity {
    private String message;
    private Response.Status status;
    private Integer statusCode;
}
