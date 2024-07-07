package org.acme.resource;

import jakarta.annotation.Nonnull;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dto.EmployeeDTO;
import org.acme.exception.entity.APIException;
import org.acme.service.EmployeeService;
import org.acme.utils.customAOP.Logged;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.jboss.logging.Logger;

import java.util.List;

import static org.acme.common.BaseAPI.V1_BASE_EMP_API_PATH;

@Path(V1_BASE_EMP_API_PATH)
@Logged
public class EmployeeResource {
        private Logger log = Logger.getLogger(EmployeeResource.class);
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
        @RolesAllowed({ "admin", "user" })
        @SecurityRequirement(name = "keycloak-custom")
        @Operation(summary = "Use this APi to create Single Emp at a time", description = "Create Emp API")
        @APIResponses(value = {
                        @APIResponse(responseCode = "200", description = "If data Save Successfully"),
                        @APIResponse(responseCode = "409", description = "If find duplicate Email ID")
        })
        public Response createEmp(@Valid EmployeeDTO employeeDTO) {
                System.out.println("EMP " + employeeDTO);
                if (employeeDTO.getAge() == 99) {
                        throw new APIException("AGE 99");
                } else if (employeeDTO.getAge() == 98) {
                        throw new NotFoundException("NOT FOUNT 98 ");
                }
                Long id = employeeService.saveOrUpdate(employeeDTO);
                return Response.ok(id).build();
        }

        @GET
        @Path("/getAll")
        @Produces(MediaType.APPLICATION_JSON)
        @RolesAllowed({ "admin" })
        @SecurityRequirement(name = "keycloak-custom")
        @Operation(summary = "Use to get All emp with out pagination")
        @APIResponses(value = {
                        @APIResponse(responseCode = "200", description = "If data fetch Successfully"),

        })
        public List<EmployeeDTO> getAllEmp() {
                return employeeService.getAllEmpWithOutPaging();
        }

        @GET
        @Path("/getEmpByID")
        @Produces(MediaType.APPLICATION_JSON)
        @RolesAllowed({ "user", "admin" })
        @SecurityRequirement(name = "keycloak-custom")
        @Operation(summary = "Use to get All emp with its ID")
        @APIResponses(value = {
                        @APIResponse(responseCode = "200", description = "If data fetch Successfully"),
                        @APIResponse(responseCode = "404", description = "If data not find")

        })
        public Response getAllEmpById(@Nonnull @QueryParam("id") Long id) {
                EmployeeDTO employee = employeeService.getEmpById(id);
                return Response.ok(employee).build();
        }

        @GET
        @Path("/getAllWithPage")
        @Produces(MediaType.APPLICATION_JSON)
        @RolesAllowed({ "admin" })
        @SecurityRequirement(name = "keycloak-custom")
        @Operation(summary = "Use to get All emp with pagination")
        @APIResponses(value = {
                        @APIResponse(responseCode = "200", description = "If data fetch Successfully"),

        })
        public List<EmployeeDTO> getAllEmpWithPagination() {
                return employeeService.getAllEmpWithOutPaging();
        }

        @POST
        @Path("/createBulk")
        @RolesAllowed({ "admin", "user" })
        @SecurityRequirement(name = "keycloak-custom")
        @Operation(summary = "Use this APi to create Bulk Emp at a time", description = "Create Emp API")
        @APIResponses(value = {
                        @APIResponse(responseCode = "200", description = "If data Save Successfully"),
                        @APIResponse(responseCode = "409", description = "If find duplicate Email ID")
        })
        public Response createBulkEmp(@Valid List<EmployeeDTO> employeeDTO) {
                System.out.println("EMP " + employeeDTO);

                List<EmployeeDTO> employeeDTOS = employeeService.saveOrUpdateBulk(employeeDTO);
                return Response.ok(employeeDTOS).build();
        }
}
