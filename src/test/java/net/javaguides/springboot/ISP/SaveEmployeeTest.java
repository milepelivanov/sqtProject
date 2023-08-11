package net.javaguides.springboot.ISP;

import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.repository.EmployeeRepository;
import net.javaguides.springboot.service.EmployeeService;
import net.javaguides.springboot.service.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SaveEmployeeTest {
//    C1: employee објектот не е null
//    C2: објектот е зачуван корекно

    private EmployeeService employeeService;
    private EmployeeRepository employeeRepository;
    @BeforeEach
    public void init() {
        this.employeeRepository = mock(EmployeeRepository.class);
        this.employeeService = new EmployeeServiceImpl(employeeRepository);
    }
    // C1 - T, C2 - T
    @Test
    public void testSaveEmployee1(){
        Employee expected = new Employee(1L, "Jovan", "Manchev", "jovan@mail.com");

        when(this.employeeRepository.save(expected)).thenReturn(expected);

        Employee actual = this.employeeService.saveEmployee(expected);
        assertEquals(actual, expected);

    }
    // C1 - T, C2 - F
    @Test
    public void testSaveEmployee2() {
        Employee employeeToSave = new Employee(1L, "Jovan", "Manchev", "jovan@mail.com");

        when(employeeRepository.save(employeeToSave)).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> employeeService.saveEmployee(employeeToSave));
    }
    // C1 - F, C2 - F
    @Test
    public void testSaveEmployee3() {
        Employee employeeToSave = null;

        when(employeeRepository.save(employeeToSave)).thenReturn(null);

        Employee savedEmployee = employeeService.saveEmployee(employeeToSave);

        assertNull(savedEmployee);
    }
}
