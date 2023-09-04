package net.javaguides.springboot.logicCoverage;

import net.javaguides.springboot.model.Employee;

import net.javaguides.springboot.service.EmployeeService;
import net.javaguides.springboot.service.EmployeeServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CanHaveBonusDaysTest {
    private EmployeeService employeeService;


    @BeforeEach
    public  void init(){
        this.employeeService = new EmployeeServiceImpl(null);
    }

    //format a,b,c,P

    // T,T,F,T
    @Test
    public void test2(){
        Employee employee = new Employee();
        employee.setSalary(15000);
        employee.setPartTime(true);
        employee.setDepartment("HR");
        assertTrue(this.employeeService.canHaveBonusDays(employee));
    }

    // T,F,T,T
    @Test
    public void test3(){
        Employee employee = new Employee();
        employee.setSalary(15000);
        employee.setPartTime(false);
        employee.setDepartment("IT");
        assertTrue(this.employeeService.canHaveBonusDays(employee));
    }

    // T,F,F,F
    @Test
    public void test4(){
        Employee employee = new Employee();
        employee.setSalary(15000);
        employee.setPartTime(false);
        employee.setDepartment("HR");
        assertFalse(this.employeeService.canHaveBonusDays(employee));
    }

    // F,T,F,F
    @Test
    public void test6(){
        Employee employee = new Employee();
        employee.setSalary(900);
        employee.setPartTime(true);
        employee.setDepartment("HR");
        assertFalse(this.employeeService.canHaveBonusDays(employee));
    }
}
