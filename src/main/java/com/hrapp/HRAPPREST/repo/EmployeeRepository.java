package com.hrapp.HRAPPREST.repo;

import com.hrapp.HRAPPREST.model.Employee;
import org.springframework.data.repository.CrudRepository;

public interface EmployeeRepository extends CrudRepository<Employee, Integer> {
}
