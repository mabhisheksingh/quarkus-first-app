package org.acme.service;

import org.acme.pojo.Employee;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EmployeeService implements PanacheRepository<Employee> {

}
