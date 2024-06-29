package org.acme.exception.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import jakarta.enterprise.context.RequestScoped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Builder
@Data
@ToString
@RequestScoped
public class ResponseExceptionEntity {
    @JsonProperty(value = "error")
    private List<ExceptionEntity> exceptionEntityList;
    ResponseExceptionEntity() {
        this.exceptionEntityList = new ArrayList<>();
    }
}
