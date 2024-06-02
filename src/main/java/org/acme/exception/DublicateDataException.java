package org.acme.exception;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@NoArgsConstructor
@ToString
public class DublicateDataException extends RuntimeException {

    public DublicateDataException(String meString) {
        super(meString);
    }
}
