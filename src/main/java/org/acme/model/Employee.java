package org.acme.model;

import jakarta.persistence.*;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "employee", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email"}, name = "uniqueEmail")
})
public class Employee extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "age")
    private Integer age;
    @Column(name = "password", nullable = false)
    @Getter(AccessLevel.NONE)
    private String password;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
}
