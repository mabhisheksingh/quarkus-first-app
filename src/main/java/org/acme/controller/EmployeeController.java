package org.acme.controller;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.exception.DublicateDataException;
import org.acme.pojo.Employee;
import org.acme.service.EmployeeService;
import org.acme.utils.customAOP.Logged;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirements;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;
import java.util.Objects;

@Path("/v1/api/emp")
@Logged
//@Authenticated
public class EmployeeController {

    @Inject
    private EmployeeService employeeService;

    @GET
    @Path("/public/test")
    @Produces(MediaType.TEXT_PLAIN)
    public Response test() {
        return Response.ok("Hello pass public").build();
    }

    @POST
    @Path("/create")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createEmp(Employee emp) {
        if (Objects.isNull(emp)) {
            throw new NullPointerException("EMPLOYEE Obj is null");
        }
        try {
            employeeService.persist(emp);
        } catch (ConstraintViolationException exception) {
            throw new DublicateDataException(exception.getSQLException().getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return Response.ok(emp).build();
    }

    @GET
    @Path("/getAll")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @SecurityRequirement(name = "keycloak-custom")
    public List<Employee> getAllEmp() {
        return employeeService.listAll();
    }
}
