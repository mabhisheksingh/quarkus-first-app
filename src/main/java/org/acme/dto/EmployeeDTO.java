package org.acme.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = {"password"}, allowSetters = true)
public class EmployeeDTO {

    @Null
    private Long id;

    @NotNull(message = "name should not be null")
    @NotBlank(message = "name should not be blank")
    private String name;
    @NotNull(message = "age should be not null and b/w 0-125")
    @Min(value = 0)
    @Max(value = 120)
    private Integer age;

    @NotNull(message = "Password should not be null")
    @NotBlank(message = "name should not be blank")
    private String password;

    @NotNull
    @Email
    @JsonProperty(value = "email")
    private String email;

}
