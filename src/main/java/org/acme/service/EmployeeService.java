package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotFoundException;
import org.acme.dto.EmployeeDTO;
import org.acme.exception.entity.DuplicateDataException;
import org.acme.exception.entity.MultipleExceptions;
import org.acme.model.Employee;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class EmployeeService implements BaseService<Employee, Long> {

    private final Logger logger = Logger.getLogger(EmployeeService.class);

    @Transactional
    public Long saveOrUpdate(EmployeeDTO employeeDTO) {
        if (getByEmailOptional(employeeDTO.getEmail()).isPresent()) {
            throw new DuplicateDataException("Email already exist in DB" + employeeDTO.getEmail());
        }
        Employee emp = new Employee();
        emp.setName(employeeDTO.getName());
        emp.setEmail(employeeDTO.getEmail());
        emp.setAge(employeeDTO.getAge());
        emp.setPassword(employeeDTO.getPassword());

        addUser(emp);
        if (isExist(emp)) {
            logger.info("Data Saved successfully" + emp);
            return emp.getId();
        } else {
            logger.error("Facing issue while saving data");
            throw new InternalServerErrorException("Facing error ");
        }
    }

    @Transactional
    public List<EmployeeDTO> saveOrUpdateBulk(List<EmployeeDTO> employeeDTO) {

        Map<String, Long> duplicateEmail = employeeDTO.stream().parallel()
                .collect(Collectors.groupingBy(EmployeeDTO::getEmail, Collectors.counting()));

        List<String> duplicateEmails = duplicateEmail.entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .map(map -> map.getKey())
                .toList();
        if (!duplicateEmails.isEmpty()) {
            List<Exception> exceptions = duplicateEmails.stream()
                    .map(email ->
                            new Exception("EMAIL ID EXIST : " + email)
                    ).toList();
            throw new MultipleExceptions("Multiple exception", exceptions, 400);
        }
        List<Employee> whoSEmailPresent = new ArrayList<>();
        List<Employee> whoSEmailNotPresent = new ArrayList<>();
        for (EmployeeDTO employeedto : employeeDTO) {
            Employee emp = new Employee();
            emp.setName(employeedto.getName());
            emp.setEmail(employeedto.getEmail());
            emp.setAge(employeedto.getAge());
            emp.setPassword(employeedto.getPassword());
            if (getByEmailOptional(employeedto.getEmail()).isPresent()) {
                whoSEmailPresent.add(emp);
            } else {
                whoSEmailNotPresent.add(emp);
            }
        }
        if (whoSEmailPresent.isEmpty()) {
            addBulkUser(whoSEmailNotPresent);
        } else {
            List<Exception> exceptions = whoSEmailPresent.stream()
                    .map(errorEmp ->
                            new Exception("EMAIL ID EXIST : " + errorEmp.getEmail())
                    ).toList();
            throw new MultipleExceptions("Multiple exception", exceptions, 400);

        }
        return employeeDTO;
    }

    @Transactional
    public List<EmployeeDTO> getAllEmpWithOutPaging() {
        return getAll().stream().parallel().map(emp ->
                EmployeeDTO.builder()
                        .age(emp.getAge())
                        .email(emp.getEmail())
                        .name(emp.getName())
                        .id(emp.getId())
                        .build()
        ).toList();
    }

    @Transactional
    public EmployeeDTO getEmpId(Long id) {
        return getByIdOptional(id).map(employee -> EmployeeDTO.builder()
                .id(employee.getId())
                .name(employee.getName())
                .age(employee.getAge())
                .email(employee.getEmail())
                .password("Password will not show in API response due serialization blocked") //for testing purpose
                .build()).orElseThrow(() ->
                new NotFoundException("Emp id not found " + id)
        );
    }

}
