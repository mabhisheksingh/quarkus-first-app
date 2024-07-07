package org.acme.exception.mapper;

import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import org.acme.exception.entity.APIException;
import org.acme.exception.entity.ExceptionEntity;
import org.acme.exception.entity.ResponseExceptionEntity;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {
    @Inject
    ResponseExceptionEntity responseExceptionEntity;

    @Override
    public Response toResponse(Exception exception) {
        ExceptionEntity exceptionEntity = ExceptionEntity.builder()
                .errorCode(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                .errorMessage(exception.getMessage())
                .build();
        if (exception instanceof BadRequestException) {
            exceptionEntity = ExceptionEntity.builder()
                    .errorCode(Response.Status.BAD_REQUEST.getStatusCode())
                    .errorMessage(exception.getMessage())
                    .build();
        } else if (exception instanceof APIException apiException) {
            exceptionEntity = ExceptionEntity.builder()
                    .errorCode(apiException.getErrorCode())
                    .errorMessage(apiException.getErrorMessage())
                    .errorField(apiException.getErrorField())
                    .build();
        } else if (exception instanceof NotFoundException) {
            exceptionEntity = ExceptionEntity.builder()
                    .errorCode(Response.Status.NOT_FOUND.getStatusCode())
                    .errorMessage(exception.getMessage())
                    .build();
        } else {
            exceptionEntity = ExceptionEntity.builder()
                    .errorCode(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                    .errorMessage(exception.getMessage())
                    .build();
        }
        responseExceptionEntity.getExceptionEntityList().add(exceptionEntity);
        return Response.status(exceptionEntity.getErrorCode())
                .entity(responseExceptionEntity)
                .build();

    }

}
