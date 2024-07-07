package org.acme.exception.mapper;

import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.acme.exception.entity.ExceptionEntity;
import org.acme.exception.entity.ResponseExceptionEntity;

import java.util.Set;

@Provider
public class ConstrainValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    @Inject
    ResponseExceptionEntity responseExceptionEntity;

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();

        violations.stream().
                map(violation ->
                        ExceptionEntity.builder()
                                .errorField(violation.getPropertyPath().toString())
                                .errorMessage(violation.getMessage())
                                .errorCode(Response.Status.BAD_REQUEST.getStatusCode())
                                .build()
                ).forEach(
                        exceptionEntity -> responseExceptionEntity.getExceptionEntityList().add(exceptionEntity)
                );
        return Response.status(Response.Status.BAD_REQUEST.getStatusCode())
                .entity(responseExceptionEntity)
                .build();

    }

}
