package org.acme.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        if (exception instanceof BadRequestException) {
            ExceptionEntity exceptionEntity = ExceptionEntity.builder()
                    .status(Response.Status.BAD_REQUEST)
                    .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                    .message(exception.getMessage())
                    .build();

            return Response.status(Response.Status.BAD_REQUEST)
                    // .entity(exception.getMessage())
                    .entity(exceptionEntity)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(exception.getMessage())
                    .build();
        }

    }

}
