package org.acme.controller;


import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.Objects;

import org.acme.exception.BadRequestException;
import org.acme.pojo.Employee;
import org.acme.utils.customAOP.Logged;


@Path("/v1/api/emp")
@Logged
public class EmployeeController {

    @POST
    @Path("/create")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createEmp(Employee emp) throws BadRequestException {
        System.out.println("ABHII " + emp);
        if(Objects.isNull(emp))
        {
            throw new NullPointerException("EMPLOYEE Obj is nulll");
        }else if( emp.getAge() >= 20){
            throw new BadRequestException("Age should be less 20",400);
        }
        return "Employee created " + emp.toString() + " ";
    }

}
