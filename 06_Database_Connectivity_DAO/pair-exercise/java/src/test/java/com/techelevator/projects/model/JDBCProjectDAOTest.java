package com.techelevator.projects.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.projects.model.jdbc.JDBCEmployeeDAO;
import com.techelevator.projects.model.jdbc.JDBCProjectDAO;

public class JDBCProjectDAOTest {
	
	private static SingleConnectionDataSource dataSource;
	private JDBCProjectDAO dao;
	private JDBCEmployeeDAO employeeDAO;
	private static final long TEST_PROJECT_ID = 99;
	private static final String TEST_PROJECT = "TEST PROJECT";
	private static final LocalDate TEST_FROM_DATE = LocalDate.of(2010, Month.JANUARY, 2);
	private static final LocalDate TEST_TO_DATE = LocalDate.of(2030, Month.MAY, 16);
	
	@BeforeClass
	public static void setupDataSource() {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/department_projects");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		
		// Can disable auto-commit so rollback available after each test.
		dataSource.setAutoCommit(false);
	}
	
	@AfterClass
	public static void closeDataSource() {
		dataSource.destroy();
		
	}
	
	@Before
	public void setup() {
		String sqlInsertProject = "INSERT INTO project (project_id, name, from_date, to_date) " +
				"VALUES (?, ?, '1990-02-11', '2020-05-21')";
		String sqlInsertEmployee = "INSERT INTO employee (employee_id, department_id, first_name, last_name, birth_date " +
		         ", gender, hire_date) VALUES (13, 1, 'DUMMY', 'EMPLOYEE', '1970-05-16', 'M', '2019-05-16')"; 
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sqlInsertProject, TEST_PROJECT_ID, TEST_PROJECT);
		jdbcTemplate.update(sqlInsertEmployee);
		dao = new JDBCProjectDAO(dataSource);
		employeeDAO = new JDBCEmployeeDAO(dataSource);
	}
	
	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}
	
	
	@Test
	public void get_active_projects() {
		List<Project> projects = dao.getAllActiveProjects();
		int projectCount = 0;
		for (int i = 0; i < projects.size(); i++) {
			projectCount++;
		}
		
		String expectedProjectName = "TEST PROJECT";
		assertNotNull(projects);
		assertEquals(projectCount, projects.size());
		assertEquals(expectedProjectName, projects.get(projects.size() - 1).getName());
	}
	
	@Test
	public void add_employee_to_project() {
		List<Project> activeProjects = dao.getAllActiveProjects();
		List<Employee> dummyEmployee = employeeDAO.searchEmployeesByName("DUMMY", "EMPLOYEE");
		long projectId = activeProjects.get(activeProjects.size() - 1).getId();
		long employeeId = dummyEmployee.get(dummyEmployee.size() - 1).getId();
		int numberOfEmployeeOnProjects = employeeDAO.getEmployeesByProjectId(projectId).size();
		
		String expectedProjectName = "TEST PROJECT";
		
		dao.addEmployeeToProject(projectId, employeeId);
		assertEquals(numberOfEmployeeOnProjects + 1, employeeDAO.getEmployeesByProjectId(projectId).size());
		assertEquals(expectedProjectName, activeProjects.get(activeProjects.size() - 1).getName());
	}
	
	@Test
	public void remove_employee_to_project() {
		List<Project> activeProjects = dao.getAllActiveProjects();
		List<Employee> dummyEmployee = employeeDAO.searchEmployeesByName("DUMMY", "EMPLOYEE");
		long projectId = activeProjects.get(activeProjects.size() - 1).getId();
		long employeeId = dummyEmployee.get(dummyEmployee.size() - 1).getId();
		int numberOfEmployeeOnProjects = employeeDAO.getEmployeesByProjectId(projectId).size();
		
		dao.addEmployeeToProject(projectId, employeeId);
		dao.removeEmployeeFromProject(projectId, employeeId);
		assertEquals(numberOfEmployeeOnProjects, employeeDAO.getEmployeesByProjectId(projectId).size());
	}
	
	
	private Project getProject(long projectId, String name, LocalDate fromDate, LocalDate toDate) {
		Project project = new Project();
		project.setId(projectId);;
		project.setName(name);
		project.setStartDate(fromDate);
		project.setEndDate(toDate);
		return project;
	}
	
	private Employee getEmployee(long employeeId, long departmentId, String firstName, String lastName, LocalDate birthDate,
			char gender, LocalDate hireDate) {
		Employee employee = new Employee();
		employee.setId(employeeId);
		employee.setDepartmentId(departmentId);
		employee.setFirstName(firstName);
		employee.setLastName(lastName);
		employee.setBirthDay(birthDate);
		employee.setGender(gender);
		employee.setHireDate(hireDate);	
		return employee;
	}
//	private List<Long> getProjectEmployees(long projectId, long employeeId) {
//		List<Long> projectEmployees = new ArrayList<>();
//	}
	
//	@Test
//	public void add_employee_to_project() {
//
//		Employee employee = getEmployee(12, 4, "Gabreila", "Christie", LocalDate.of(1980, Month.MARCH, 17), 
//				'F', LocalDate.of(1999, Month.AUGUST, 1));
//		Project project = getProject(2, TEST_PROJECT, TEST_FROM_DATE, TEST_TO_DATE);
//		List<Long> projectEmployee = new ArrayList<>();
//		projectEmployee.add(project.getId());
//		projectEmployee.add(employee.getId());
//		dao.addEmployeeToProject(project.getId(), employee.getId());
//		
//
//		assertNotNull(employee.getId());
//		assertEquals(projectEmployee.get(0), project.getId());
//		
//	}

}
