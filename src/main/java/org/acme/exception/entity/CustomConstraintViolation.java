package org.acme.exception.entity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import jakarta.validation.metadata.ConstraintDescriptor;

public class CustomConstraintViolation implements ConstraintViolation<String> {

    private String errorMessage;
    @Override
    public String getMessage() {
        return errorMessage;
    }

    @Override
    public String getMessageTemplate() {
        return "";
    }

    @Override
    public String getRootBean() {
        return "";
    }

    @Override
    public Class<String> getRootBeanClass() {
        return null;
    }

    @Override
    public Object getLeafBean() {
        return null;
    }

    @Override
    public Object[] getExecutableParameters() {
        return new Object[0];
    }

    @Override
    public Object getExecutableReturnValue() {
        return null;
    }

    @Override
    public Path getPropertyPath() {
        return null;
    }

    @Override
    public Object getInvalidValue() {
        return null;
    }

    @Override
    public ConstraintDescriptor<?> getConstraintDescriptor() {
        return null;
    }

    @Override
    public <U> U unwrap(Class<U> type) {
        return null;
    }
}
