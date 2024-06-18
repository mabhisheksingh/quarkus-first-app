package org.acme.repositorie;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.model.Employee;

@ApplicationScoped
public class EmployeeRepo implements PanacheRepository<Employee> {
}
