package net.javaguides.springboot.service;

import java.util.List;

import org.springframework.data.domain.Page;

import net.javaguides.springboot.model.Employee;

public interface EmployeeService {
	List<Employee> getAllEmployees();
	Employee saveEmployee(Employee employee);
	Employee getEmployeeById(long id);
	Employee deleteEmployeeById(long id);

	Page<Employee> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);

	boolean canHaveBonusDays(Employee employee);

}
