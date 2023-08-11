package net.javaguides.springboot.integration;

import net.javaguides.springboot.controller.EmployeeController;
import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Test
    public void testViewHomePage() throws Exception{
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(new Employee(1L, "Jovan", "Manchev", "jovan@mail.com"));
        Page<Employee> page = new PageImpl<>(employeeList);
        Pageable pageable = PageRequest.of(0, 5, Sort.by("firstName").ascending());
        when(employeeService.findPaginated(1, 5, "firstName", "asc")).thenReturn(page);


        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("index"))
                .andExpect(model().attributeExists("listEmployees"));
    }
    @Test
    public void testShowNewEmployeeForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/showNewEmployeeForm"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("new_employee"))
                .andExpect(model().attributeExists("employee"));
    }
    @Test
    public void testAddEmployee() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/saveEmployee")
                        .param("firstName", "Jovan")
                        .param("lastName", "Manchev")
                        .param("email", "jovan@mail.com"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }
    @Test
    public void testEditForm() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/saveEmployee")
                        .param("firstName", "Jovan")
                        .param("lastName", "Manchev")
                        .param("email", "jovan@mail.com"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());


        Employee employee = new Employee(1L, "Jovan", "Manchev", "jovan@mail.com");
        when(employeeService.getEmployeeById(1L)).thenReturn(employee);

        mockMvc.perform(MockMvcRequestBuilders.get("/showFormForUpdate/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("update_employee"))
                .andExpect(model().attribute("employee", employee));
    }
    @Test
    public void testAddAndEditEmployee() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/saveEmployee")
                        .param("firstName", "Jovan")
                        .param("lastName", "Manchev")
                        .param("email", "jovan@mail.com"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());


        Employee originalEmployee = new Employee(1L, "Jovan", "Manchev", "jovan@mail.com");
        Employee editedEmployee = new Employee(1L, "Edited Jovan", "Edited Manchev", "jovanEdited@mail.com");
        when(employeeService.getEmployeeById(1L)).thenReturn(originalEmployee, editedEmployee);


        mockMvc.perform(MockMvcRequestBuilders.post("/saveEmployee")
                        .param("id", "1")
                        .param("firstName", "Edited Jovan")
                        .param("lastName", "Edited Manchev")
                        .param("email", "jovanEdited@mail.com"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());


        assertEquals("Edited Jovan", editedEmployee.getFirstName());
        assertEquals("Edited Manchev", editedEmployee.getLastName());
        assertEquals("jovanEdited@mail.com", editedEmployee.getEmail());
    }

    @Test
    public void testDeleteEmployee() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/saveEmployee")
                        .param("firstName", "Jovan")
                        .param("lastName", "Manchev")
                        .param("email", "jovan@mail.com"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());

        mockMvc.perform(MockMvcRequestBuilders.post("/saveEmployee")
                        .param("firstName", "Jovan")
                        .param("lastName", "Manchev")
                        .param("email", "jovan@mail.com"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());

        Employee addedEmployee = new Employee(1L, "Jovan", "Manchev", "jovan@mail.com");
        when(employeeService.getEmployeeById(1L)).thenReturn(addedEmployee);


        mockMvc.perform(MockMvcRequestBuilders.get("/deleteEmployee/1"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));


        verify(employeeService, times(1)).deleteEmployeeById(1L);

        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(new Employee(2L, "Jovan", "Manchev", "jovan@mail.com"));
        Page<Employee> page = new PageImpl<>(employeeList);
        when(employeeService.findPaginated(1, 5, "firstName", "asc")).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(model().attribute("listEmployees", hasSize(1)));
    }
    @Test
    public void testFindPaginatedWithDefaultParameters() throws Exception {

        List<Employee> employeeList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            employeeList.add(new Employee(i, "FirstName" + i, "LastName" + i, "email" + i + "@mail.com"));
            mockMvc.perform(MockMvcRequestBuilders.post("/saveEmployee")
                            .param("firstName", employeeList.get(i - 1).getFirstName())
                            .param("lastName", employeeList.get(i - 1).getLastName())
                            .param("email", employeeList.get(i - 1).getEmail()))
                    .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
        }

        Page<Employee> employeePage = new PageImpl<>(employeeList.subList(5, 10));


        when(employeeService.findPaginated(anyInt(), anyInt(), anyString(), anyString())).thenReturn(employeePage);

        mockMvc.perform(MockMvcRequestBuilders.get("/page/2")
                .param("sortField", "firstName") // Provide the sortField parameter
                .param("sortDir", "asc"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("index"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("listEmployees"))
                .andExpect(MockMvcResultMatchers.model().attribute("listEmployees", hasSize(5)))
                .andExpect(MockMvcResultMatchers.model().attribute("listEmployees", employeeList.subList(5,10)));
    }


}
