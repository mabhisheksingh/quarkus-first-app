package org.acme.exception;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        ExceptionEntity exceptionEntity = ExceptionEntity.builder().status(Response.Status.INTERNAL_SERVER_ERROR)
                .message(exception.getMessage())
                .build();
        if (exception instanceof BadRequestException) {
            exceptionEntity = ExceptionEntity.builder()
                    .status(Response.Status.BAD_REQUEST)
                    .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                    .message(exception.getMessage())
                    .build();
        }
        else if (exception instanceof DublicateDataException) {
            exceptionEntity = ExceptionEntity.builder()
                    .status(Response.Status.CONFLICT)
                    .statusCode(Response.Status.CONFLICT.getStatusCode())
                    .message(exception.getMessage())
                    .build();
        }else if (exception instanceof NotFoundException) {
            exceptionEntity = ExceptionEntity.builder()
                    .status(Response.Status.NOT_FOUND)
                    .statusCode(Response.Status.NOT_FOUND.getStatusCode())
                    .message(exception.getMessage())
                    .build();
        }else if (exception instanceof NullPointerException) {
            exceptionEntity = ExceptionEntity.builder()
                    .status(Response.Status.NOT_MODIFIED)
                    .statusCode(Response.Status.NOT_MODIFIED.getStatusCode())
                    .message(exception.getMessage())
                    .build();
        }else if (exception instanceof RuntimeException) {
            exceptionEntity = ExceptionEntity.builder()
                    .status(Response.Status.BAD_GATEWAY)
                    .statusCode(Response.Status.BAD_GATEWAY.getStatusCode())
                    .message(exception.getMessage())
                    .build();
        }else {
            exceptionEntity = ExceptionEntity.builder()
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .statusCode(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                    .message(exception.getMessage())
                    .build();
        }
        return Response.status(exceptionEntity.getStatus())
                .entity(exceptionEntity)
                .build();

    }

}
