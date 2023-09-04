package net.javaguides.springboot.mutationCoverage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.repository.EmployeeRepository;
import net.javaguides.springboot.service.EmployeeService;
import net.javaguides.springboot.service.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Sort.Direction;


public class EmployeeControllerMutationTest {


    private EmployeeService employeeService;
    private EmployeeRepository employeeRepository;

    @BeforeEach
    public void init() {
        this.employeeRepository = mock(EmployeeRepository.class);
        this.employeeService = new EmployeeServiceImpl(employeeRepository);
    }

    @Test
    public void testDeleteEmployeeById() {
        Employee employee = new Employee(123, "Mile", "Pelivanov", "mile@test.com");

        when(employeeRepository.findById(123L)).thenReturn(Optional.of(employee));

        EmployeeServiceImpl employeeService = new EmployeeServiceImpl(employeeRepository);

        Employee result = employeeService.deleteEmployeeById(123L);

        assertNotNull(result);
        assertEquals(employee, result);

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        verify(employeeRepository).deleteById(idCaptor.capture());
        assertEquals(123L, idCaptor.getValue().longValue());
    }

    @Test
    public void testDeleteEmployeeByIdNotFound() {

        when(employeeRepository.findById(999L)).thenReturn(Optional.empty());

        EmployeeServiceImpl employeeService = new EmployeeServiceImpl(employeeRepository);

        assertThrows(RuntimeException.class, () -> employeeService.deleteEmployeeById(999L));
    }


    @Test
    public void testGetAllEmployees() {
        List<Employee> mockEmployees = new ArrayList<>();
        mockEmployees.add(new Employee(123L, "Mile", "Pelivanov", "mile@gmail.com"));

        Mockito.when(employeeRepository.findAll()).thenReturn(mockEmployees);

        EmployeeServiceImpl employeeService = new EmployeeServiceImpl(employeeRepository);

        List<Employee> result = employeeService.getAllEmployees();

        assertEquals(mockEmployees, result);
    }

    @Test
    public void testSaveEmployee() {
        Employee expected = new Employee(222L, "Mile", "Pelivanov", "mile@gmail.com");

        when(this.employeeRepository.save(expected)).thenReturn(expected);
        Employee actual = this.employeeService.saveEmployee(expected);
        assertNotNull(actual);
    }

    @Test
    public void testCanHaveBonusDays() {

        Employee employee = mock(Employee.class);
        //TTT
        when(employee.getSalary()).thenReturn(12000.0);
        when(employee.isPartTime()).thenReturn(true);
        when(employee.getDepartment()).thenReturn("IT");
        assertTrue(employeeService.canHaveBonusDays(employee));

        //FTT
        when(employee.getSalary()).thenReturn(10000.0);
        when(employee.isPartTime()).thenReturn(false);
        when(employee.getDepartment()).thenReturn("IT");
        assertTrue(employeeService.canHaveBonusDays(employee));

        //TFT
        when(employee.getSalary()).thenReturn(12000.0);
        when(employee.isPartTime()).thenReturn(false);
        when(employee.getDepartment()).thenReturn("IT");
        assertTrue(employeeService.canHaveBonusDays(employee));

        //FTT
        when(employee.getSalary()).thenReturn(10000.0);
        when(employee.isPartTime()).thenReturn(true);
        when(employee.getDepartment()).thenReturn("IT");
        assertTrue(employeeService.canHaveBonusDays(employee));

        //TTF
        when(employee.getSalary()).thenReturn(12000.0);
        when(employee.isPartTime()).thenReturn(true);
        when(employee.getDepartment()).thenReturn("HR");
        assertTrue(employeeService.canHaveBonusDays(employee));

        //FFF
        when(employee.getSalary()).thenReturn(10000.0);
        when(employee.isPartTime()).thenReturn(false);
        when(employee.getDepartment()).thenReturn("HR");
        assertFalse(employeeService.canHaveBonusDays(employee));

        //TFF
        when(employee.getSalary()).thenReturn(12000.0);
        when(employee.isPartTime()).thenReturn(false);
        when(employee.getDepartment()).thenReturn("HR");
        assertFalse(employeeService.canHaveBonusDays(employee));

        //FTF
        when(employee.getSalary()).thenReturn(10000.0);
        when(employee.isPartTime()).thenReturn(true);
        when(employee.getDepartment()).thenReturn("HR");
        assertFalse(employeeService.canHaveBonusDays(employee));
    }


    @Test
    public void testFindPaginated() {

        EmployeeServiceImpl employeeService = new EmployeeServiceImpl(employeeRepository);

        int pageNo = 2;
        int pageSize = 10;
        String sortField = "lastName";
        String sortDirection = "ASC";

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

        List<Employee> employees = new ArrayList<>();

        employees.add(new Employee(123L, "Mile", "Pelivanov", "mile@gmail.com"));
        employees.add(new Employee(222L, "Jovan", "Manchev", "jovan@gmail.com"));
        employees.add(new Employee(333L, "Riste", "Ristov", "riste@gmail.com"));

        when(employeeRepository.findAll(pageableCaptor.capture())).thenReturn(Page.empty());

        Page<Employee> result = employeeService.findPaginated(pageNo, pageSize, sortField, sortDirection);

        Pageable capturedPageable = pageableCaptor.getValue();
        assertEquals(pageNo - 1, capturedPageable.getPageNumber());
        assertEquals(pageSize, capturedPageable.getPageSize());
        assertEquals(Direction.ASC, capturedPageable.getSort().getOrderFor(sortField).getDirection());

        assertNotNull(result);
    }
}
