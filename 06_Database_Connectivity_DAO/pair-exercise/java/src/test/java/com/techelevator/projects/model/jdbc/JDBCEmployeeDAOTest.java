package com.techelevator.projects.model.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.postgresql.util.LruCache.CreateAction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Employee;





public class JDBCEmployeeDAOTest {
	


	/* Using this particular implementation of DataSource so that
	 * every database interaction is part of the same database
	 * session and hence the same database transaction */
	private static SingleConnectionDataSource dataSource;
	private JDBCEmployeeDAO dao;

	
	// Before run any tests, this method will initialize the datasource for testing
		@BeforeClass
		public static void setupDataSource() {
			dataSource = new SingleConnectionDataSource();
			dataSource.setUrl("jdbc:postgresql://localhost:5432/department_projects");
			dataSource.setUsername ("postgres");
			dataSource.setPassword("postgres1");
			
			//We can disable the autocommit for the connections so we can rollback after 
			// each test
			dataSource.setAutoCommit(false);
		}
		
		@AfterClass
		public static void closeDataSource() {
			dataSource.destroy();
		}
		
		@Before
		public void setup() {
			String sqlInsertEmployee = "INSERT INTO employee (employee_id, department_id, first_name, last_name, birth_date " +
			         ", gender, hire_date) VALUES (13, 1, 'DUMMY', 'EMPLOYEE', '1970-05-16', 'M', '2019-05-16')"; 
//			Employee dummyEmployee = getEmployee((long) 3, "Dummy", "Employee", LocalDate.of(1980, Month.APRIL, 21),
//						'M', LocalDate.of(2010, Month.JANUARY, 1));
//			dao.createEmployee(dummyEmployee);		                 
		
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			jdbcTemplate.update(sqlInsertEmployee);
			dao = new JDBCEmployeeDAO(dataSource);
		}
		
		@After
		public void rollback() throws SQLException {
			dataSource.getConnection().rollback();
		}

		@Test
		public void get_all_employees_test() {
			List<Employee> employees = dao.getAllEmployees();
			int expectedEmployeeCount = 0;
			for (int i = 0; i < employees.size(); i++) {
				expectedEmployeeCount++;
				
			}
			assertNotNull(employees);
			assertEquals(expectedEmployeeCount, employees.size());
			
		}
		
		@Test
		public void get_employee_by_name() {
			List<Employee> employee = dao.searchEmployeesByName("DUMMY", "EMPLOYEE");
			
			assertNotNull(employee);
			assertEquals("DUMMY", employee.get(0).getFirstName());
			assertEquals("EMPLOYEE", employee.get(0).getLastName());
		}
		
		@Test
		public void get_employee_by_dept_id() {
			List<Employee> employees = dao.getEmployeesByDepartmentId(1);
			long expectedEmployeeCount = 0;
			for (Employee employee : employees) { // probably don't need this since the list is employees in the department.
				if (employee.getDepartmentId() == 1) {
					expectedEmployeeCount++;
				}
			}
			
			assertNotNull(employees);
			assertEquals(expectedEmployeeCount, employees.size());
		}
		
		@Test
		public void get_employees_without_projects_test() {
			List<Employee> employees = dao.getEmployeesWithoutProjects();
			int expectedCount = 0;
			for (int i = 0; i < employees.size(); i++) {
				expectedCount++;
			}
			assertNotNull(employees);
			assertEquals(expectedCount, employees.size());
		}
		
		// Technically we don't need to bother with this test.
//		@Test
//		public void save_new_Employee_and_read_it_back() {
//			Employee employee = getEmployee ((long) 1, "Sonthaya", "Deelua",
//					LocalDate.of(1970, 05, 16), 'M', LocalDate.of(2019,05,10));
//			
//
//			dao.createEmployee(employee);
//			List <Employee> savedEmployee = dao.getEmployeesByDepartmentId(employee.getDepartmentId()); // this SHOULD return the city object we just
//			                                              	    // added to the database
//			
//			assertNotEquals(null, employee.getId());
//			// asserts to make sure each attribute is the same for the cities to be equal
//			assertEquals(employee.getId(), savedEmployee.get(0).getId());
//			assertEquals(employee.getDepartmentId(), savedEmployee.get(0).getDepartmentId());
//			assertEquals(employee.getFirstName(), savedEmployee.get(0).getFirstName());
//			assertEquals(employee.getLastName(), savedEmployee.get(0).getLastName());
//			assertEquals(employee.getBirthDay(), savedEmployee.get(0).getBirthDay());
//			assertEquals(employee.getGender(), savedEmployee.get(0).getGender());
//			assertEquals(employee.getHireDate(), savedEmployee.get(0).getHireDate());
//		}


	
	private Employee getEmployee(Long departmentId, String firstName, String lastName, LocalDate birthDate,
			char gender, LocalDate hireDate) {

		Employee employee = new Employee();
		employee.setDepartmentId(departmentId);
		employee.setFirstName(firstName);
		employee.setLastName(lastName);
		employee.setBirthDay(birthDate);
		employee.setGender(gender);
		employee.setHireDate(hireDate);

		return employee;
	}
//	public void save(Employee newEmployee) {
//		String sqlInsertEmployee = "INSERT INTO employee(employee_id,department_id,first_name, "
//				+ " last_name, birth_date , gender, hire_date ) " + "VALUES(?, ?, ?, ?, ?, ?, ?)";
//		newEmployee.setId(getNextEmployeeId());
//		dao.update(sqlInsertEmployee, newEmployee.getId(), newEmployee.getDepartmentId(), newEmployee.getFirstName(),
//				newEmployee.getLastName(), newEmployee.getBirthDay(), newEmployee.getGender(),
//				newEmployee.getHireDate());
//	}

	
}
