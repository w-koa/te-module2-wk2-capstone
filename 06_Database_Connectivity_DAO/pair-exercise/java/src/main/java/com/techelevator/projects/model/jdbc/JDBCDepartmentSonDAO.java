package com.techelevator.projects.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Department;
import com.techelevator.projects.model.DepartmentDAO;

public class JDBCDepartmentSonDAO implements DepartmentDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCDepartmentSonDAO(DataSource datasource) {
		jdbcTemplate = new JdbcTemplate(datasource);

	}

	@Override
	public List<Department> getAllDepartments() {
		List<Department> allDept = new ArrayList<>();
		String sqlGetAllDepartments = "SELECT department_id, name " + "FROM department ";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllDepartments);
		while (results.next()) {
			Department theDept = mapRowToDepartment(results);
			allDept.add(theDept);
		}
		return allDept;
	}

	private Department mapRowToDepartment(SqlRowSet results) {
		Department theDept = new Department();

		theDept.setId(results.getLong("department_id"));
		theDept.setName(results.getString("name"));

		return theDept;
	}

	@Override
	public List<Department> searchDepartmentsByName(String nameSearch) {
		List<Department> deptNameSearch = new ArrayList<>();
		Department theDept = new Department();
		String sqlSearchDepartmentByName = "SELECT department_id, name " + "FROM department WHERE name = ? ";
		SqlRowSet result = jdbcTemplate.queryForRowSet(sqlSearchDepartmentByName, nameSearch);
		if (result.next()) {
			theDept = mapRowToDepartment(result);
			deptNameSearch.add(theDept);
		}

		return deptNameSearch;
	}

	@Override
	public void saveDepartment(Department updatedDepartment) {
		String sqlSaveDepartment = " UPDATE department SET name = ? WHERE department_id = ? ";
		jdbcTemplate.update(sqlSaveDepartment, updatedDepartment.getName(), updatedDepartment.getId());

	}

	@Override
	public Department createDepartment(Department newDepartment) {
		String sqlCreateDepartment = "INSERT INTO department (department_id, name ) " +
									 "VALUES (?, ?) ";
		newDepartment.setId(getNextDepartmentId());
		
		jdbcTemplate.update(sqlCreateDepartment, newDepartment.getId(), newDepartment.getName());
		
		return newDepartment;
		
	}

	private long getNextDepartmentId() {

		SqlRowSet nextOneIdResult = jdbcTemplate.queryForRowSet(" SELECT nextval('seq_department_id')");
		if (nextOneIdResult.next()) {
			return nextOneIdResult.getInt(1);
		} else {
			throw new RuntimeException("New Department ID is not available");
		}

	}

	@Override
	public Department getDepartmentById(Long id) {
		Department theDept = new Department();
		String sqlGetDepartmentById = "SELECT department_id , name " +
									  "FROM department WHERE department_id = ? ";

		SqlRowSet resultId = jdbcTemplate.queryForRowSet(sqlGetDepartmentById, id);
		if(resultId.next()) {
			theDept = mapRowToDepartment(resultId);
			
		}
		return theDept;
	}

}
