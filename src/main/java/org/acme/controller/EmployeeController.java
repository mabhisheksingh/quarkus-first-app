package org.acme.controller;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Objects;

import org.acme.exception.BadRequestException;
import org.acme.exception.DublicateDataException;
import org.acme.pojo.Employee;
import org.acme.service.EmployeeService;
import org.acme.utils.customAOP.Logged;
import org.hibernate.exception.ConstraintViolationException;

@Path("/v1/api/emp")
@Logged
@Authenticated
public class EmployeeController {

    @Inject
    private EmployeeService employeeService;

    @GET
    @Path("/test")
    @Produces(MediaType.TEXT_PLAIN)
    @PermitAll
    public Response test() {
        return Response.ok("Hello pass public").build();
    }

    @GET
    @Path("/testp")
    @Produces(MediaType.TEXT_PLAIN)

    public Response testP() {
        return Response.ok("Hello pass Auth").build();
    }

    @POST
    @Path("/create")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createEmp(Employee emp) {
        if (Objects.isNull(emp)) {
            throw new NullPointerException("EMPLOYEE Obj is nulll");
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
    public List<Employee> getAllEmp() {

        return employeeService.listAll();

    }
}
