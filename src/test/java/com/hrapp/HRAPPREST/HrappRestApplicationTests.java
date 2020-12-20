package com.hrapp.HRAPPREST;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrapp.HRAPPREST.controller.EmployeeController;
import com.hrapp.HRAPPREST.model.Employee;
import com.hrapp.HRAPPREST.repo.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@EnableJpaRepositories(basePackages = "com.hrapp.HRAPPREST.repo")
@PropertySource("classpath:application.properties")
@WebMvcTest(EmployeeController.class)
class HrappRestApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	EmployeeRepository employeeRepository;

	@Test
	public void testGetAllEmployees() throws Exception {

		Employee employee1 = new Employee(1, "merve", null, "DE", 1, 1);
		Employee employee2 = new Employee(2, "pinar", null, "TR", 1, 1);

		List<Employee> employeeList = new ArrayList<>();
		employeeList.add(employee1);
		employeeList.add(employee2);

		given(employeeRepository.findAll()).willReturn(employeeList);

		ResultActions request = mockMvc.perform(get("/employees")).andDo(print()).andExpect(status().isOk());

		String result = request.andReturn().getResponse().getContentAsString();
		List<Employee> resultLIst = Arrays.stream(objectMapper.readValue(result, Employee[].class))
				.collect(Collectors.toList());

		for (Employee employee : employeeList)
			Assert.isTrue(resultLIst.stream().anyMatch(
					x -> x.getEmployeeID().equals(employee.getEmployeeID()) && x.getName()
							.equals(employee.getName()) && x.getAddress().equals(employee.getAddress()) && x
							.getPositionID().equals(employee.getPositionID()) && x.getStatusID()
							.equals(employee.getStatusID())), "Some employees are missing after GET request");
	}

	@Test
	public void testGetEmployeeByIdExisting() throws Exception {
		Employee employee = new Employee(1, "merve", null, "DE", 1, 1);

		given(employeeRepository.findById(1)).willReturn(Optional.of(employee));

		this.mockMvc.perform(get("/employees/{id}", 1)).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value(employee.getName()))
				.andExpect(jsonPath("$.statusID").value(employee.getStatusID()))
				.andExpect(jsonPath("$.address").value(employee.getAddress()));
	}

	@Test
	public void testGetEmployeeByIdNotExisting() throws Exception {

		this.mockMvc.perform(get("/employees/{id}", (Integer) 3)).andDo(print()).andExpect(status().isNotFound());
	}

	@Test
	public void testDeleteEmployeeByExistingId() throws Exception {
		Employee employee = new Employee(1, "merve", null, "DE", 1, 1);

		given(employeeRepository.findById(1)).willReturn(Optional.of(employee));

		this.mockMvc.perform(delete("/employees/{id}", 1)).andDo(print()).andExpect(status().isOk());
	}

	@Test
	public void testDeleteEmployeeByNotExistingId() throws Exception {
		Employee employee = new Employee(1, "merve", null, "DE", 1, 1);

		given(employeeRepository.findById(1)).willReturn(Optional.of(employee));

		this.mockMvc.perform(delete("/employees/{id}", 2)).andDo(print()).andExpect(status().isNotFound());
	}

	@Test
	public void testAddNewEmployee() throws Exception {
		Employee employee = new Employee(null, "merve", null, "DE", 1, 1);
		Employee created = new Employee(1, "merve", null, "DE", 1, 1);
		given(employeeRepository.save(employee)).willReturn(created);

		this.mockMvc.perform(post("/employees/").content(asJsonString(employee)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void testUpdateEmployee() throws Exception {
		Employee employee = new Employee(1, "merve", null, "DE", 1, 1);
		Employee updated = new Employee(1, "updated", null, "moved", 2, 1);

		given(employeeRepository.findById(1)).willReturn(Optional.of(employee));
		given(employeeRepository.save(employee)).willReturn(updated);

		this.mockMvc.perform(put("/employees/{id}", employee.getEmployeeID()).content(asJsonString(employee))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("updated"));
	}

	private static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
