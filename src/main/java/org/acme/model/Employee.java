package org.acme.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "employee", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email"}, name= "uniqueEmail")
})
public class Employee extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "name should not be null")
    private String name;
    @NotNull(message = "age should be not null and b/w 0-125")
    @Min(value = 0)
    @Max(value = 120)
    private Integer age;

    @NotNull
    @Email
    @JsonProperty(value = "email")
    private String email;
}
