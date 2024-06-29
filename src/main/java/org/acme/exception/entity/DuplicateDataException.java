package org.acme.exception.entity;

import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@ToString
public class DuplicateDataException extends RuntimeException {

    public DuplicateDataException(String meString) {
        super(meString);
    }
}
