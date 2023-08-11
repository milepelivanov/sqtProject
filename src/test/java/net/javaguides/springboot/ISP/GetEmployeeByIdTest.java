package net.javaguides.springboot.ISP;

import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.repository.EmployeeRepository;
import net.javaguides.springboot.service.EmployeeService;
import net.javaguides.springboot.service.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetEmployeeByIdTest {
 //C1: објектот постои во база
    private EmployeeService employeeService;
    private EmployeeRepository employeeRepository;
    @BeforeEach
    public void init() {
        this.employeeRepository = mock(EmployeeRepository.class);
        this.employeeService = new EmployeeServiceImpl(employeeRepository);
    }
    // C1 - T
    @Test
    public void getEmployeeByIdTest1() {


        Employee employee = new Employee(1L, "Jovan", "Manchev", "jovan@mail.com");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Employee actual = this.employeeService.getEmployeeById(1L);


        assertEquals(employee, actual);

    }
    // C1 - F
    @Test
    public void getEmployeeByIdTest2() {

        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> employeeService.getEmployeeById(1L));

    }
}
