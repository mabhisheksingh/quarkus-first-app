package org.acme.service;

import jakarta.transaction.Transactional;
import org.acme.dto.EmployeeDTO;
import org.acme.model.Employee;

import java.util.List;
import java.util.Optional;


public interface EmployeeService {

    @Transactional
    public Long saveOrUpdate(EmployeeDTO employeeDTO);

    @Transactional
    public List<EmployeeDTO> saveOrUpdateBulk(List<EmployeeDTO> employeeDTO);

    @Transactional
    public List<EmployeeDTO> getAllEmpWithOutPaging();

    @Transactional
    public EmployeeDTO getEmpById(Long id);

    @Transactional
    public Optional<Employee> getByEmailOptional(String email);
}
