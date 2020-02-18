package com.techelevator.projects.model.jdbc;

import java.time.LocalDate;
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
		List<Employee> employees = new ArrayList<>();
		String sqlFindEmployeeByDeptId = "SELECT * FROM employee WHERE department_id = ?";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlFindEmployeeByDeptId, id);
		while (results.next()) {
			employees.add(mapRowToEmp(results));
		}
		return employees;
	}

	@Override
	public List<Employee> getEmployeesWithoutProjects() {

		List<Employee> employeeNames = new ArrayList<Employee>();
		String sqlEmpNoProject = "SELECT * FROM employee a "
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
		List<Employee> employees = new ArrayList<>();
		String sqlFindEmployeeByProjectId = "SELECT * FROM project_employee pe "
				+ "JOIN employee e ON e.employee_id = pe.employee_id WHERE project_id = ?";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlFindEmployeeByProjectId, projectId);
		while (results.next()) {
			employees.add(mapRowToEmp(results));
		}
		return employees;
	}

	@Override
	public void changeEmployeeDepartment(Long employeeId, Long departmentId) {
		String sqlUpdateEmpDept = "UPDATE employee SET department_id = ? WHERE employee_id = ?";
		jdbcTemplate.update(sqlUpdateEmpDept, departmentId, employeeId);

	}

	private Employee mapRowToEmp(SqlRowSet results) {
		Employee employee = new Employee();
		employee.setId(results.getLong("employee_id"));
		employee.setDepartmentId(results.getLong("department_id"));
		employee.setFirstName(results.getString("first_name"));
		employee.setLastName(results.getString("last_name"));
		employee.setBirthDay(LocalDate.parse(results.getString("birth_date")));
		employee.setGender(results.getString("gender").charAt(0));
		employee.setHireDate(LocalDate.parse(results.getString("hire_date")));

		return employee;
	}

	private long getNextEmployeeId() {
		SqlRowSet nextIdResult = jdbcTemplate.queryForRowSet("SELECT nextval('seq_employee_id')");
		if (nextIdResult.next()) {
			return nextIdResult.getLong(1);
		} else {
			throw new RuntimeException("Something went wrong while getting an id for the new city");
		}
	}

	public Employee createEmployee(Employee newEmployee) {
		String sqlInsertEmployee = "INSERT INTO employee(employee_id,department_id,first_name, "
				+ " last_name, birth_date , gender, hire_date ) " + "VALUES(?, ?, ?, ?, ?, ?, ?)";
		newEmployee.setId(getNextEmployeeId());

		jdbcTemplate.update(sqlInsertEmployee, newEmployee.getId(), newEmployee.getDepartmentId(),
				newEmployee.getFirstName(), newEmployee.getLastName(), newEmployee.getBirthDay(),
				newEmployee.getGender(), newEmployee.getHireDate());
		return newEmployee;
	}

}
