package com.techelevator.projects.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Department;
import com.techelevator.projects.model.Employee;
import com.techelevator.projects.model.Project;
import com.techelevator.projects.model.ProjectDAO;

public class JDBCProjectDAO implements ProjectDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCProjectDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<Project> getAllActiveProjects() {
		String sqlFindAllProjects = "SELECT * FROM project WHERE((to_date > CURRENT_DATE) " + 
				"OR (to_date IS NULL)) " + 
				"AND (from_date IS NOT NULL) ";
		List<Project> allProjects = new ArrayList<>();
		SqlRowSet result = jdbcTemplate.queryForRowSet(sqlFindAllProjects);

		while (result.next()) {
			allProjects.add(mapRowToProjects(result));

		}
		return allProjects;
	}

	@Override
	public void removeEmployeeFromProject(Long projectId, Long employeeId) {
		String sqlRemoveEmployee = "DELETE FROM project_employee WHERE project_id = ? AND employee_id = ?";
		jdbcTemplate.update(sqlRemoveEmployee, projectId, employeeId);
	}

	@Override
	public void addEmployeeToProject(Long projectId, Long employeeId) {
		
		String sqlAddEmployee = "INSERT INTO project_employee (project_id, employee_id) VALUES (?, ?) ";
		jdbcTemplate.update(sqlAddEmployee, projectId, employeeId);
		
		
	}
	
	private Project mapRowToProjects(SqlRowSet results) {
		Project project = new Project();
		project.setId(results.getLong("project_id")); 
		project.setName(results.getString("name"));
		return project;
	}

}
