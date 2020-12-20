package com.hrapp.HRAPPREST.controller;

import com.hrapp.HRAPPREST.exception.EmployeeNotFoundException;
import com.hrapp.HRAPPREST.model.Employee;
import com.hrapp.HRAPPREST.repo.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class EmployeeController {

	@Autowired
	private EmployeeRepository employeeRepository;

	@GetMapping(path="/employees")
	public @ResponseBody
	Iterable<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}

	@GetMapping(path="/employees/{id}")
	public @ResponseBody
	Employee getEmployeeById(@PathVariable Integer id) {
		Optional<Employee> employee = employeeRepository.findById(id);
		if (employee.isPresent())
			return employee.get();
		else
			throw new EmployeeNotFoundException();
	}

	@DeleteMapping(path="/employees/{id}")
	public Employee deleteEmployeeById(@PathVariable Integer id) {
		Optional<Employee> employee = employeeRepository.findById(id);
		if (employee.isPresent()){
			employeeRepository.deleteById(id);
			return employee.get();
		}
		else
			throw new EmployeeNotFoundException();
	}

	@PostMapping(path="/employees", consumes = "application/json")
	public ResponseEntity createNewEmployee(@RequestBody Employee employee) {
		Employee created = employeeRepository.save(employee);
		return ResponseEntity.ok(created);
	}

	@PutMapping(path="/employees/{id}", consumes = "application/json")
	public ResponseEntity updateEmployeeById(@PathVariable Integer id, @RequestBody Employee employeeDetails) {
		Optional<Employee> updatedEmployee = employeeRepository.findById(id);
		if (updatedEmployee.isPresent()){
			updatedEmployee.get().setName(employeeDetails.getName());
			updatedEmployee.get().setAddress(employeeDetails.getAddress());
			updatedEmployee.get().setBirthday(employeeDetails.getBirthday());
			updatedEmployee.get().setPositionID(employeeDetails.getPositionID());
			updatedEmployee.get().setStatusID(employeeDetails.getStatusID());
			Employee updated = employeeRepository.save(updatedEmployee.get());

			return ResponseEntity.ok(updated);
		}
		else
			throw new EmployeeNotFoundException();
	}
}
