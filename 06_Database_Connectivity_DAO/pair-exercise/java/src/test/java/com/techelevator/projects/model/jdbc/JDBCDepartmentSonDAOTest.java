package com.techelevator.projects.model.jdbc;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.projects.model.Department;

public class JDBCDepartmentSonDAOTest {

	private static SingleConnectionDataSource dataSource;
	private JDBCDepartmentSonDAO deptSonDao;

	@BeforeClass
	public static void setUpDataSource() {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/department_projects");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		dataSource.setAutoCommit(false);
	}

	@AfterClass
	public static void closeDataSource() {
		dataSource.destroy();
	}

	@Before
	public void setup() {
		String sqlInsertDepartment = "INSERT INTO department (department_id, name)" + " VALUES (5, 'TEST DEPARTMENT')";

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sqlInsertDepartment);
		deptSonDao = new JDBCDepartmentSonDAO(dataSource);
	}

	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}

	@Test
	public void test_to_get_all_department() {
		List<Department> allDept = deptSonDao.getAllDepartments();
		int countDept = 0;
		for (int i = 0; i < allDept.size(); i++) {
			countDept++;
		}
		String expectedName = "TEST DEPARTMENT";
		Long expectedId = 5L;
		int expectedCount = 5;
		assertNotNull(allDept);
		assertEquals(expectedName, allDept.get(allDept.size() - 1).getName());
		assertEquals(expectedId, allDept.get(allDept.size() - 1).getId());
		assertEquals(expectedCount, allDept.size());
	}

	@Test
	public void search_dept_by_name() {
		List<Department> theDept = deptSonDao.searchDepartmentsByName("TEST DEPARTMENT");
		int countDept = 0;
		for (int i = 0; i < theDept.size(); i++) {
			countDept++;
		}

		String expectedName = "TEST DEPARTMENT";
		assertNotNull(theDept);
		assertEquals(expectedName, theDept.get(theDept.size() - 1).getName());

	}

	@Test
	public void test_save_dept() {
		List<Department> depts = deptSonDao.searchDepartmentsByName("TEST DEPARTMENT");

		depts.get(0).setName("TEST TEST");
		deptSonDao.saveDepartment(depts.get(0));

		String expectedName = "TEST TEST";
		assertNotNull(depts);
		assertEquals(expectedName, depts.get(depts.size() - 1).getName());

	}

	@Test
	public void test_create_dept() {
		List<Department> depts = deptSonDao.searchDepartmentsByName("TEST DEPARTMENT");
		depts.get(0).setName("TEST TEST");
		deptSonDao.createDepartment(depts.get(0));
		String expectedName = "TEST TEST";
		assertNotNull(depts);
		assertEquals(expectedName, depts.get(depts.size() - 1).getName());

	}
	@Test
	public void test_get_dept_by_id() {
		Department depts = deptSonDao.getDepartmentById(5L);
		
		String expectedResult = "TEST DEPARTMENT";
		assertNotNull(depts);
		assertEquals(expectedResult, depts.getName());
	}
	
}
