package com.techelevator.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Campground;
import com.techelevator.model.Park;
import com.techelevator.model.ParkDAO;

public class JDBCParkDAO implements ParkDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCParkDAO(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	private Park mapRowToPark(SqlRowSet results) {
		Park park = new Park();
		park.setParkId(results.getInt("park_id"));
		park.setName(results.getString("name"));
		park.setLocation(results.getString("location"));
		park.setEstablishDate(results.getDate("establish_date").toLocalDate());
		park.setArea(results.getInt("area"));
		park.setVisitors(results.getInt("visitors"));
		park.setDescription(results.getString("description"));
		return park;
	}

	/*
	 * Gets all parks and puts them into a List
	 */
	@Override
	public List<Park> getAllParks() {
		String sqlFindAllParks = "SELECT * FROM park";
		List<Park> allParks = new ArrayList<>();
		SqlRowSet result = jdbcTemplate.queryForRowSet(sqlFindAllParks);

		while (result.next()) {
			allParks.add(mapRowToPark(result));

		}
		return allParks;
	}

}
