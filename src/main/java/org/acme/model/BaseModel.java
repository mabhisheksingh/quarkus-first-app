package org.acme.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

import java.io.Serializable;

@MappedSuperclass
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseModel implements Serializable {
    @Column(name = "name", nullable = false)
    private String name;
}
