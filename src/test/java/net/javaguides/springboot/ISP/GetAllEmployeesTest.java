package net.javaguides.springboot.ISP;


import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.repository.EmployeeRepository;
import net.javaguides.springboot.service.EmployeeService;
import net.javaguides.springboot.service.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetAllEmployeesTest {
    // C1 : Има вработени
    private EmployeeService employeeService;
    private EmployeeRepository employeeRepository;
    @BeforeEach
    public void init() {
        this.employeeRepository = mock(EmployeeRepository.class);
        this.employeeService = new EmployeeServiceImpl(employeeRepository);
    }

    // C1 - T
    @Test
    public void testGetAllEmployees1() {

        List<Employee> expectedEmployees = new ArrayList<>();
        expectedEmployees.add(new Employee(1L, "Jovan", "Manchev", "jovan@mail.com"));

        when(employeeRepository.findAll()).thenReturn(expectedEmployees);


        List<Employee> actualEmployees = employeeService.getAllEmployees();


        assertEquals(expectedEmployees.size(), actualEmployees.size());

    }
    // C1 - F
    @Test
    public void testGetAllEmployees2() {

        List<Employee> expectedEmployees = new ArrayList<>();


        when(employeeRepository.findAll()).thenReturn(expectedEmployees);


        List<Employee> actualEmployees = employeeService.getAllEmployees();


        assertEquals(expectedEmployees.size(), actualEmployees.size());

    }
}
