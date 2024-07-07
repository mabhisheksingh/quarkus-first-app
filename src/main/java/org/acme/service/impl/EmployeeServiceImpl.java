package org.acme.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.acme.dto.EmployeeDTO;
import org.acme.exception.entity.APIException;
import org.acme.exception.entity.MultipleExceptions;
import org.acme.model.Employee;
import org.acme.repository.EmployeeRepository;
import org.acme.service.EmployeeService;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class EmployeeServiceImpl implements EmployeeService {
    @Inject
    EmployeeRepository employeeRepository;

    private final Logger logger = Logger.getLogger(EmployeeService.class);

    @Override
    @Transactional
    public Long saveOrUpdate(EmployeeDTO employeeDTO) {
        if (getByEmailOptional(employeeDTO.getEmail()).isPresent()) {
            throw new APIException("Email already exist in DB" + employeeDTO.getEmail());
        }
        Employee emp = new Employee();
        emp.setName(employeeDTO.getName());
        emp.setEmail(employeeDTO.getEmail());
        emp.setAge(employeeDTO.getAge());
        emp.setPassword(employeeDTO.getPassword());

        employeeRepository.persist(emp);
        if (employeeRepository.isPersistent(emp)) {
            logger.info("Data Saved successfully" + emp);
            return emp.getId();
        } else {
            logger.error("Facing issue while saving data");
            throw new InternalServerErrorException("Facing error ");
        }
    }

    @Override
    public List<EmployeeDTO> saveOrUpdateBulk(List<EmployeeDTO> employeeDTO) {

        Map<String, Long> duplicateEmail = employeeDTO.stream().parallel()
                .collect(Collectors.groupingBy(EmployeeDTO::getEmail, Collectors.counting()));

        List<String> duplicateEmails = duplicateEmail.entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .map(Map.Entry::getKey)
                .toList();
        if (!duplicateEmails.isEmpty()) {
            List<Exception> exceptions = duplicateEmails.stream()
                    .map(email -> new Exception("EMAIL ID EXIST : " + email)).toList();
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
            employeeRepository.persist(whoSEmailNotPresent);
        } else {
            List<Exception> exceptions = whoSEmailPresent.stream()
                    .map(errorEmp -> new Exception("EMAIL ID EXIST : " + errorEmp.getEmail())).toList();
            throw new MultipleExceptions("Multiple exception", exceptions, 400);

        }
        return employeeDTO;
    }

    @Override
    public List<EmployeeDTO> getAllEmpWithOutPaging() {
        return employeeRepository.listAll().stream().parallel().map(emp -> EmployeeDTO.builder()
                .age(emp.getAge())
                .email(emp.getEmail())
                .name(emp.getName())
                .id(emp.getId())
                .build()).toList();
    }

    @Override
    public EmployeeDTO getEmpById(Long id) {
        System.out.println("id " + id);
        System.out.println(employeeRepository.findByIdOptional(id).isPresent());

        return employeeRepository.findByIdOptional(id).map(employee -> EmployeeDTO.builder()
                .id(employee.getId())
                .name(employee.getName())
                .age(employee.getAge())
                .email(employee.getEmail())
                .password("Password will not show in API response due serialization blocked") // for testing purpose
                .build()).orElseThrow(() -> new NotFoundException("Emp id not found " + id));
    }



    @Override
    public Optional<Employee> getByEmailOptional(String email) {
        return employeeRepository.find("email", email).firstResultOptional();
    }

}
