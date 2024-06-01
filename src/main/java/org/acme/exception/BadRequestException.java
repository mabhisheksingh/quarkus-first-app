package org.acme.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BadRequestException extends Exception {
    private Integer statusCode;
    public BadRequestException(String message, Integer statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
