package net.javaguides.springboot.model;

import jakarta.persistence.*;

@Entity
@Table(name = "employees")
public class Employee {
	public Employee() {
	}

	public Employee(long id, String firstName, String lastName, String email) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private long id;

	private String firstName;

	private String lastName;
	private String email;

	private double salary;

	private boolean partTime;

	private String department;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public boolean isPartTime() {
		return partTime;
	}

	public void setPartTime(boolean partTime) {
		this.partTime = partTime;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}
}
