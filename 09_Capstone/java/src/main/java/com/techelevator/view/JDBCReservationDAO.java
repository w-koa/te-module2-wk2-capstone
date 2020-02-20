package com.techelevator.view;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Reservation;
import com.techelevator.model.ReservationDAO;

public class JDBCReservationDAO implements ReservationDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCReservationDAO(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<Reservation> searchForAvailableReservations(String campgroundId, String startDate, String endDate) {
		List<Reservation> availableReservations = new ArrayList<>();
		
		String sqlFindReservations = "SELECT reservation_id, site_id, name, from_date, "
				+ "to_date, create_date FROM reservation WHERE site_id = ? AND to_date = ? AND from_date = ?";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlFindReservations, campgroundId, startDate, endDate);
		
		return availableReservations;
	}

	@Override
	public void createReservation() {
		// TODO Auto-generated method stub

	}

}
