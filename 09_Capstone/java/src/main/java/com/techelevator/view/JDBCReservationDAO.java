package com.techelevator.view;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.techelevator.model.Reservation;
import com.techelevator.model.ReservationDAO;

public class JDBCReservationDAO implements ReservationDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCReservationDAO(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<Reservation> searchForAvailableReservations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createReservation() {
		// TODO Auto-generated method stub

	}

}
