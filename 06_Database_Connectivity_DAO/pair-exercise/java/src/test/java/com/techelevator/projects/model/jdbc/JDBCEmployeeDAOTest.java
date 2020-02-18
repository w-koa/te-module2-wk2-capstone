package com.techelevator.projects.model.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Employee;





public class JDBCEmployeeDAOTest {
	
	private static final Long TEST_DEPARTMEMTID = 4L;

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
			         ", gender, hire_date, " +

					 "VALUES (13, ?, 'FirstAsia', 'LastSouthern', '1970-05-16', M, '2019-05-16')"; 
					                 
		
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			jdbcTemplate.update(sqlInsertEmployee, TEST_DEPARTMEMTID);
			dao = new JDBCEmployeeDAO(dataSource);
		}
		
		@After
		public void rollback() throws SQLException {
			dataSource.getConnection().rollback();
		}
		
		@Test
		public void save_new_Employee_and_read_it_back() {
			Employee theEmployee = getEmployee ((long) 13,TEST_DEPARTMEMTID, "Sonthaya", "Deelua",
					LocalDate.of(1970, 05, 16), 'M', LocalDate.of(2019,05,10));
			
			// this is the dao method we are testing - the SAVE (Create)
			dao.createEmployee(theEmployee);
			List <Employee> savedEmployee = dao.getEmployeesByDepartmentId(theEmployee.getDepartmentId()); // this SHOULD return the city object we just
			                                              	    // added to the database
			
			assertNotEquals(null, theEmployee.getId());
			// asserts to make sure each attribute is the same for the cities to be equal
			assertEquals(theEmployee.getId(), savedEmployee.get(0).getId());
			assertEquals(theEmployee.getDepartmentId(), savedEmployee.get(0).getDepartmentId());
			assertEquals(theEmployee.getFirstName(), savedEmployee.get(0).getFirstName());
			assertEquals(theEmployee.getLastName(), savedEmployee.get(0).getLastName());
			assertEquals(theEmployee.getBirthDay(), savedEmployee.get(0).getBirthDay());
			assertEquals(theEmployee.getGender(), savedEmployee.get(0).getGender());
			assertEquals(theEmployee.getHireDate(), savedEmployee.get(0).getHireDate());
		}

//	@Test
//	public void test() {
//		fail("Not yet implemented");
//	}
	// helper method to create a city object for us -- we will reuse this!
	
	private Employee getEmployee(Long employee_id, Long department_id, String first_name, String last_name, LocalDate birth_date,
			char gender, LocalDate hire_date) {

		Employee theEmployee = new Employee();
		theEmployee.setId(employee_id);
		theEmployee.setDepartmentId(TEST_DEPARTMEMTID);
		theEmployee.setFirstName(first_name);
		theEmployee.setLastName(last_name);
		theEmployee.setBirthDay(birth_date);
		theEmployee.setGender(gender);
		theEmployee.setHireDate(hire_date);

		return theEmployee;
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
