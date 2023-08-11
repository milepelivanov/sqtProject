package net.javaguides.springboot.ISP;

import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.repository.EmployeeRepository;
import net.javaguides.springboot.service.EmployeeService;
import net.javaguides.springboot.service.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DeleteEmployeeTest {
    // C1: објетот е избришан успешо
    private EmployeeService employeeService;
    private EmployeeRepository employeeRepository;

    @BeforeEach
    public void init() {
        this.employeeRepository = mock(EmployeeRepository.class);
        this.employeeService = new EmployeeServiceImpl(employeeRepository);
    }
    // C1 - T
    @Test
    public void testDeleteEmployeeById1() {
        long employeeIdToDelete = 1L;
        Employee employeeToDelete = new Employee(employeeIdToDelete, "Jovan", "Manchev", "jovan@mail.com");

        when(employeeRepository.findById(employeeIdToDelete)).thenReturn(Optional.of(employeeToDelete));

        Employee deletedEmployee = employeeService.deleteEmployeeById(employeeIdToDelete);

       // verify(employeeRepository, times(0)).deleteById(employeeIdToDelete);

        assertEquals(employeeToDelete, deletedEmployee);
    }
    // C1 - F
    @Test
    public void testDeleteEmployeeById2() {
        long nonExistentEmployeeId = 999L;

        when(employeeRepository.findById(nonExistentEmployeeId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> employeeService.deleteEmployeeById(nonExistentEmployeeId));
        verify(employeeRepository, never()).deleteById(anyLong());
    }

}
