package com.techelevator.projects.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Department;
import com.techelevator.projects.model.DepartmentDAO;

public class JDBCDepartmentDAO implements DepartmentDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCDepartmentDAO(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Department> getAllDepartments() {
		String sqlFindAllDept = "SELECT * FROM department;";
		System.out.println("in get all depts");
		List<Department> allDepartments = new ArrayList<>();
		SqlRowSet result = jdbcTemplate.queryForRowSet(sqlFindAllDept);

		while (result.next()) {
			System.out.println("have results");
			allDepartments.add(mapRowToDept(result));

		}
		return allDepartments;
	}

	

	@Override
	public List<Department> searchDepartmentsByName(String nameSearch) {
		List<Department> departmentNames = new ArrayList<Department>();
		String sqlFindDeptByName = "SELECT department_id, name FROM department WHERE name = ?";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlFindDeptByName, nameSearch);
		if (results.next()) {
			departmentNames.add(mapRowToDept(results));
		}
		return departmentNames;
	}

	private long getNextDeptId() {
		SqlRowSet nextIdResult = jdbcTemplate.queryForRowSet(" SELECT nextval('seq_department_id')");
		if (nextIdResult.next()) {
			return nextIdResult.getInt(1);
		} else {
			throw new RuntimeException("Uhoh!  Something went wrong while getting the next id!");
		}
	}

	@Override
	public void saveDepartment(Department updatedDepartment) {
		String sqlUpdateDepartment = "UPDATE department SET name = ? WHERE department_id = ?";
		jdbcTemplate.update(sqlUpdateDepartment, updatedDepartment.getName(), updatedDepartment.getId());

	}

	@Override
	public Department createDepartment(Department newDepartment) {
		String sqlInsertDepartment = "INSERT INTO department(department_id, name) " + "VALUES(?, ?)";
		newDepartment.setId(getNextDeptId());

		jdbcTemplate.update(sqlInsertDepartment, newDepartment.getId(), newDepartment.getName());
		return newDepartment;
	}

	@Override
	public Department getDepartmentById(Long id) {
		Department theDept = null;
		String sqlFindDeptById = "SELECT id, name " + "FROM department WHERE id = ?";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlFindDeptById, id);
		if (results.next()) {
			theDept = mapRowToDept(results);
		}
		return theDept;
	}



	private Department mapRowToDept(SqlRowSet results) {
		Department theDept = new Department();
		theDept.setId(results.getLong("department_id")); // inside the parens is the db column name and mapping to the City
													// object
		theDept.setName(results.getString("name"));
		return theDept;
	}



}
