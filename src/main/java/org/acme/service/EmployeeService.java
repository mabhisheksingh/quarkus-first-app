package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.exception.BadRequestException;
import org.acme.exception.DublicateDataException;
import org.acme.model.Employee;
import org.acme.repositorie.EmployeeRepo;
import org.acme.utils.customAOP.Logged;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class EmployeeService {
    EmployeeRepo employeeRepo;

    EmployeeService(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    @Logged
    public Long saveOrUpdate(Employee emp) {
        if (Objects.isNull(emp)) {
            throw new NullPointerException("EMPLOYEE Obj is null");
        }
        try {
            employeeRepo.persist(emp);
        } catch (ConstraintViolationException exception) {
            throw new DublicateDataException(exception.getSQLException().getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return emp.getId();
    }

    @Logged
    public List getAllEmp() {
        List list;
        try {
            list = employeeRepo.listAll();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return list;
    }

    @Logged
    public Employee getEmpId(Long id) throws BadRequestException {
        if (Objects.isNull(id)) {
            throw new NullPointerException("EMPLOYEE ID is null");
        }
        Employee employee = employeeRepo.findById(id);
        if (Objects.isNull(employee)) {
            throw new BadRequestException("ID not find ");
        }
        return employee;
    }

}
