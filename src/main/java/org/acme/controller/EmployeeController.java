package org.acme.controller;

import jakarta.annotation.Nonnull;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dto.EmployeeDTO;
import org.acme.model.Employee;
import org.acme.service.EmployeeService;
import org.acme.utils.customAOP.Logged;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.jboss.logging.Logger;

import java.util.List;

import static org.acme.common.BaseAPI.V1_BASE_EMP_API_PATH;

@Path(V1_BASE_EMP_API_PATH)
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
    @SecurityRequirement(name = "keycloak-custom")
    public Response createEmp(@Valid EmployeeDTO employeeDTO) {
        System.out.println("EMP " + employeeDTO);
        if(employeeDTO.getAge()==99){
            throw new NullPointerException("AGE 99");
        }else if( employeeDTO.getAge() == 98 ){
            throw new NotFoundException("NOT FOUNT 98 ");
        }
        Long id = employeeService.saveOrUpdate(employeeDTO);
        return Response.ok(id).build();
    }

    @GET
    @Path("/getAll")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"admin"})
    @SecurityRequirement(name = "keycloak-custom")
    public List<EmployeeDTO> getAllEmp() {
        return employeeService.getAllEmpWithOutPaging();
    }

    @GET
    @Path("/getEmpByID")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"user", "admin"})
    @SecurityRequirement(name = "keycloak-custom")
    public Response getAllEmpById(@Nonnull @QueryParam("id") Long id) throws BadRequestException {
        EmployeeDTO employee = employeeService.getEmpId(id);
        return Response.ok(employee).build();

    }
}
