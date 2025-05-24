package org.iam.exception.mapper;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.iam.exception.entity.ExceptionEntity;
import org.iam.exception.entity.MultipleExceptions;
import org.iam.exception.entity.ResponseExceptionEntity;

@Provider
public class MultipleExceptionMapper implements ExceptionMapper<MultipleExceptions> {
    @Inject
    ResponseExceptionEntity responseExceptionEntity;

    @Override
    public Response toResponse(MultipleExceptions multipleExceptions) {

        System.out.println("*&^%&%^&%^%"+multipleExceptions );
        for (Exception exception1 : multipleExceptions.getExceptions()) {
            responseExceptionEntity.getExceptionEntityList()
                    .add(ExceptionEntity.builder()
                            .errorMessage(exception1.getMessage())
                            .errorCode(multipleExceptions.getErrorCode())
                            .build());
        }

        return Response.status(multipleExceptions.getErrorCode())
                .entity(responseExceptionEntity)
                .build();

    }

}
