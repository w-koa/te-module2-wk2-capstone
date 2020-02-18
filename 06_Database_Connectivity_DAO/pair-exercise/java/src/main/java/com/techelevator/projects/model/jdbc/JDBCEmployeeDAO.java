package com.techelevator.projects.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Department;
import com.techelevator.projects.model.Employee;
import com.techelevator.projects.model.EmployeeDAO;

public class JDBCEmployeeDAO implements EmployeeDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCEmployeeDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Employee> getAllEmployees() {

		String sqlFindAllEmp = "SELECT * FROM employee ORDER BY employee_id ASC";
		List<Employee> allEmployees = new ArrayList<>();
		SqlRowSet result = jdbcTemplate.queryForRowSet(sqlFindAllEmp);

		while (result.next()) {
			allEmployees.add(mapRowToEmp(result));

		}
		return allEmployees;
	}

	@Override
	public List<Employee> searchEmployeesByName(String firstNameSearch, String lastNameSearch) {
		List<Employee> employeeNames = new ArrayList<Employee>();
		String sqlFindEmpByNames = "SELECT * FROM employee WHERE first_name ILIKE ? AND last_name ILIKE ?";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlFindEmpByNames, firstNameSearch, lastNameSearch);

		while (results.next()) {
			Employee employee = mapRowToEmp(results);
			employeeNames.add(employee);

		}
		return employeeNames;
	}

	@Override
	public List<Employee> getEmployeesByDepartmentId(long id) {
		return new ArrayList<>();
	}

	@Override
	public List<Employee> getEmployeesWithoutProjects() {

		List<Employee> employeeNames = new ArrayList<Employee>();
		String sqlEmpNoProject = "SELECT a.*" + " FROM employee a "
				+ " WHERE a.employee_id NOT IN (SELECT b.employee_id FROM project_employee b)";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlEmpNoProject);

		while (results.next()) {
			Employee employee = mapRowToEmp(results);
			employeeNames.add(employee);

		}
		return employeeNames;

	}

	@Override
	public List<Employee> getEmployeesByProjectId(Long projectId) {
		return new ArrayList<>();
	}

	@Override
	public void changeEmployeeDepartment(Long employeeId, Long departmentId) {
		String sqlUpdateEmpDept = "UPDATE employee SET department_id = ? WHERE employee_id = ?";
		jdbcTemplate.update(sqlUpdateEmpDept, departmentId, employeeId);

	}

	private Employee mapRowToEmp(SqlRowSet results) {
		Employee employee = new Employee();
		employee.setId(results.getLong("employee_id")); // inside the parens is the db column name and mapping to the
														// City
		// object
		employee.setDepartmentId(results.getLong("department_id"));
		employee.setFirstName(results.getString("first_name"));
		employee.setLastName(results.getString("last_name"));
//		employee.setBirthDay(results.getDate("birth_date"));
//		employee.setGender(results.getString("gender"));
//		employee.setHireDate(results.getDate("hire_date"));
		
		return employee;
	}

}
