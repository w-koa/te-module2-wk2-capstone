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
	//set to public just for testing purpose
	public Campground mapRowToCampground(SqlRowSet results) {
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
	
}
