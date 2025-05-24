package org.iam.service;

import jakarta.transaction.Transactional;
import org.iam.dto.EmployeeDTO;
import org.iam.model.Employee;

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
