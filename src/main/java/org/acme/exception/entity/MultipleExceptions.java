package org.acme.exception.entity;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
public class MultipleExceptions extends RuntimeException {
    private final List<Exception> exceptions;
    private final Integer errorCode;


    public MultipleExceptions(String message, List<Exception> exceptions, Integer errorCode) {
        super(message);
        this.exceptions = exceptions;
        this.errorCode = errorCode;
    }

    public List<Exception> getExceptions() {
        return exceptions;
    }

    public Integer getErrorCode() {
        return errorCode;
    }
}
