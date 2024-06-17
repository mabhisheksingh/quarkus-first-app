package org.acme.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.exception.BadRequestException;
import org.acme.exception.DublicateDataException;
import org.acme.pojo.Employee;
import org.acme.service.EmployeeService;
import org.acme.utils.customAOP.Logged;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.hibernate.exception.ConstraintViolationException;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.Objects;

@Path("/v1/api/emp")
@Logged
public class EmployeeController {
    private Logger log = Logger.getLogger(EmployeeController.class);
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
    @RolesAllowed({"admin", "user"})
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    @SecurityRequirement(name = "keycloak-custom")
    public Response createEmp(@Valid Employee emp) {
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
    @RolesAllowed({"admin"})
    @SecurityRequirement(name = "keycloak-custom")
    public List<Employee> getAllEmp() {
        return employeeService.listAll();
    }

    @GET
    @Path("/getEmpByID")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({"user"})
    @SecurityRequirement(name = "keycloak-custom")
    public Response getAllEmpById(@QueryParam("id") Long id) throws BadRequestException {
        System.out.println("ID " + id);
        Employee employee = employeeService.findById(id);
        log.info("Id " + id);
        log.info("Employee " + employee);
        if (Objects.isNull(employee)) {
            throw new BadRequestException("ID not find ");
        }
        return Response.ok(employee).build();

    }
}
