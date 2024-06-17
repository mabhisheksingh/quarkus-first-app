package org.acme.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;

@Builder
@AllArgsConstructor
@ToString
public class BadRequestException extends Exception {
    public BadRequestException(String message) {
        super(message);
    }
}
