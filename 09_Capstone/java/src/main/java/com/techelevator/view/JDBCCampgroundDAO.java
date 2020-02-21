package com.techelevator.view;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Campground;
import com.techelevator.model.CampgroundDAO;
import com.techelevator.model.Park;

public class JDBCCampgroundDAO implements CampgroundDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCCampgroundDAO(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	private Campground mapRowToCampground(SqlRowSet results) {
		Campground campground = new Campground();
		campground.setCampgroundId(results.getInt("campground_id"));
		campground.setParkId(results.getInt("park_id"));
		campground.setCampgroundName(results.getString("name"));
		campground.setOpenMonth(results.getString("open_from_mm"));
		campground.setCloseMonth(results.getString("open_to_mm"));
		campground.setDailyFee(results.getDouble("daily_fee"));
		return campground;
	}
	@Override
	public List<Campground> getAllCampgrounds() {
		String sqlFindAllCampgrounds= "SELECT * FROM campground";
		List<Campground> allCampgrounds = new ArrayList<>();
		SqlRowSet result = jdbcTemplate.queryForRowSet(sqlFindAllCampgrounds);

		while (result.next()) {
			allCampgrounds.add(mapRowToCampground(result));
		}
		return allCampgrounds;
	}

	@Override
	public List<Campground> getCampgroundById(int id) {
		
		
		List<Campground> campgrounds = new ArrayList<>();
		String sqlFindCampgroundById = "SELECT campground_id, park_id, name, open_from_mm, "
				+ "open_to_mm, daily_fee FROM campground WHERE campground_id = ?";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlFindCampgroundById, id);
		while (results.next()) {
			campgrounds.add(mapRowToCampground(results));
		}
		return campgrounds;
	}

	@Override
	public List<Campground> getCampgroundByParkId(int id) {
		List<Campground> campgrounds = new ArrayList<>();
		String sqlFindCampgroundById = "SELECT campground_id, park_id, name, open_from_mm, "
				+ "open_to_mm, daily_fee FROM campground WHERE park_id = ?";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlFindCampgroundById, id);
		while (results.next()) {
			campgrounds.add(mapRowToCampground(results));
		}
		return campgrounds;
	}
	
	@Override
	public List<Campground> getCampgroundsByParkName(String parkName) {
		List<Campground> campgrounds = new ArrayList<>();
		String sqlFindCampgroundsByParkName = "SELECT * FROM campground JOIN park ON park.park_id = campground.park_id "
				+ "WHERE park.name = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlFindCampgroundsByParkName, parkName);
		while(results.next()) {
			campgrounds.add(mapRowToCampground(results));
		}
		return campgrounds;
	}
	@Override
	public int[] getCampgroundOpenMonths(Campground campground) {
		int openMonth = Integer.parseInt(campground.getOpenMonth());
		int closeMonth = Integer.parseInt(campground.getCloseMonth());
		int difference = closeMonth - openMonth;
		int[] campgroundOpenMonths = new int[difference];
		for (int i = openMonth; i < difference; i++) {
			campgroundOpenMonths[i] = i;
		}
		campground.setCampgroundOpenMonths(campgroundOpenMonths);
		return campgroundOpenMonths;
	}
}
