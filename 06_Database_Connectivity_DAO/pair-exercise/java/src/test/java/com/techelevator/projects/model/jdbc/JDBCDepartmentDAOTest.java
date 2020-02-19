package com.techelevator.projects.model.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

import com.techelevator.projects.model.Department;
import com.techelevator.projects.model.Employee;

public class JDBCDepartmentDAOTest {
	private static SingleConnectionDataSource dataSource;
	private JDBCDepartmentDAO deptDao;
	
	@BeforeClass
	public static void setupDataSource() {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/department_projects");
		dataSource.setUsername ("postgres");
		dataSource.setPassword("postgres1");
		dataSource.setAutoCommit(false);
	}
	
	@AfterClass
	public static void closeDataSource() {
		dataSource.destroy();
	}
	
	@Before
	public void setup() {
		String sqlInsertDepartment = "INSERT INTO department (department_id, name)"
				+ " VALUES (99, 'TEST DEPARTMENT')"; 	                 
	
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sqlInsertDepartment);
		deptDao = new JDBCDepartmentDAO(dataSource);
	}
	
	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}
	
	@Test
	public void test_get_all_deparments() {
		
		List<Department> departments = deptDao.getAllDepartments();
		int deptCount = 0;
		for (int i = 0; i < departments.size(); i++) {
			deptCount++;
		}
//		long expectedDeptId = departments.get(departments.size() - 1).getId();
		String expectedDeptName = "TEST DEPARTMENT";
		assertNotNull(departments);
		assertEquals(deptCount, departments.size());
//		assertEquals(expectedDeptId, departments.get(departments.size() - 1).getId());
		assertEquals(expectedDeptName, departments.get(departments.size() -1).getName());
	}
	
	@Test
	public void search_dept_by_name() {
		
		List<Department> departments = deptDao.searchDepartmentsByName("TEST DEPARTMENT");
		String expectedDeptName = "TEST DEPARTMENT";
		
		assertNotNull(departments);
		assertEquals(expectedDeptName, departments.get(departments.size() -1).getName());		
	}

	@Test
	public void test_save_dept() {
		List<Department> departments = deptDao.searchDepartmentsByName("TEST DEPARTMENT");
		departments.get(0).setName("TEST TEST DEPARTMENT");
		deptDao.saveDepartment(departments.get(0));
		String expectedDeptName = "TEST TEST DEPARTMENT";
		assertNotNull(departments);
		assertEquals(expectedDeptName, departments.get(departments.size() -1).getName());	
	}
	
	@Test
	public void test_create_dept() {
		
		deptDao.createDepartment(getDepartment((long) 100, "TEST"));
		List<Department> departments = deptDao.searchDepartmentsByName("TEST");
		String expectedDeptName = "TEST";
		
		assertNotNull(departments);
		assertEquals(expectedDeptName, departments.get(departments.size() -1).getName());
	}
	
	@Test
	public void test_get_dept_by_id() {
		
		Department department = deptDao.getDepartmentById((long) 99) ;
		String expectedDeptName = "TEST DEPARTMENT";
		assertNotNull(department);
		assertEquals(expectedDeptName, department.getName());
	}
	
	private Department getDepartment(Long departmentId, String name) {

		Department department = new Department();
		department.setId(departmentId);
		department.setName(name);

		return department;
	}
}
