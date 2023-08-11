package net.javaguides.springboot.ISP;

import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.repository.EmployeeRepository;
import net.javaguides.springboot.service.EmployeeService;
import net.javaguides.springboot.service.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
public class FindPaginatedTest {
//    C1: PageSize е поголемо од 0
//    C2: Сите објекти се на една страна
//    C3: Објектите се сортирани во растечки редослед

    private EmployeeService employeeService;
    private EmployeeRepository employeeRepository;

    @BeforeEach
    public void init() {
        this.employeeRepository = mock(EmployeeRepository.class);
        this.employeeService = new EmployeeServiceImpl(employeeRepository);
    }

    // C1 - T, C2 - T, C3 - T
    @Test
    public void findPaginatedTest1(){
        int pageNo = 1;
        int pageSize = 5;
        String sortFild = "id";
        String sortDirection = "ASC";
        List<Employee> expectedEmployees = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, pageSize, Sort.by("id").ascending());
        expectedEmployees.add(new Employee(1L, "Jovan", "Manchev", "jovan@mail.com"));
        expectedEmployees.add(new Employee(2L, "Mile", "Pelivanov", "mile@mail.com"));
        Page<Employee> expectedPage = new PageImpl<>(expectedEmployees, pageable, expectedEmployees.size());

        when(employeeRepository.findAll(pageable)).thenReturn(new PageImpl<>(expectedEmployees, pageable, expectedEmployees.size()));

        Page<Employee> employeePage = this.employeeService.findPaginated(pageNo, pageSize, sortFild, sortDirection);

        assertEquals(expectedPage, employeePage);

    }
    // C1 - F, C2 - T, C3 - T
    @Test
    public void findPaginatedTest2(){
        int pageNo = 1;
        int pageSize = 0;
        String sortFild = "id";
        String sortDirection = "ASC";
        assertThrows(IllegalArgumentException.class, () -> this.employeeService.findPaginated(pageNo, pageSize, sortFild, sortDirection));
    }

    // C1 - T, C2 - F, C3 - T
    @Test
    public void findPaginatedTest3(){
        int pageNo = 2;
        int pageSize = 3;
        String sortFild = "id";
        String sortDirection = "ASC";
        List<Employee> expectedEmployees = new ArrayList<>();
        LongStream
                .rangeClosed(1,5)
                .forEach(number -> expectedEmployees.add(new Employee(number, "Jovan", "Manchev", "jovan@mail.com")));
        Pageable pageable = PageRequest.of(pageNo-1, pageSize, Sort.by("id").ascending());
        Page<Employee> expectedPage = new PageImpl<>(expectedEmployees, pageable, expectedEmployees.size());

        when(employeeRepository.findAll(pageable)).thenReturn(new PageImpl<>(expectedEmployees, pageable, expectedEmployees.size()));

        Page<Employee> employeePage = this.employeeService.findPaginated(pageNo, pageSize, sortFild, sortDirection);

        assertEquals(expectedPage, employeePage);
    }

    // C1 - T, C2 - T, C3 - F
    @Test
    public void findPaginatedTest4(){
        int pageNo = 1;
        int pageSize = 5;
        String sortFild = "id";
        String sortDirection = "DESC";
        List<Employee> expectedEmployees = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, pageSize, Sort.by("id").descending());
        expectedEmployees.add(new Employee(1L, "Jovan", "Manchev", "jovan@mail.com"));
        expectedEmployees.add(new Employee(2L, "Mile", "Pelivanov", "mile@mail.com"));
        Page<Employee> expectedPage = new PageImpl<>(expectedEmployees, pageable, expectedEmployees.size());

        when(employeeRepository.findAll(pageable)).thenReturn(new PageImpl<>(expectedEmployees, pageable, expectedEmployees.size()));

        Page<Employee> employeePage = this.employeeService.findPaginated(pageNo, pageSize, sortFild, sortDirection);

        assertEquals(expectedPage, employeePage);

    }
}
