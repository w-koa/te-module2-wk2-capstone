package com.techelevator.view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Campground;
import com.techelevator.model.Park;
import com.techelevator.model.Reservation;
import com.techelevator.model.ReservationDAO;

public class JDBCReservationDAO implements ReservationDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCReservationDAO(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	

	@Override
	public List<Reservation> getOverlappingReservations(int campgroundId, LocalDate startDate, LocalDate endDate) {
		List<Reservation> overlappingReservations = new ArrayList<>();
		Reservation reservation = null;
		String sqlGetOverlapping = "SELECT * FROM reservation r "
				+ "JOIN site s ON s.site_id = r.site_id "
				+ "WHERE s.campground_id = ? AND (? BETWEEN from_date AND to_date "
				+ "OR ? BETWEEN from_date AND to_date)";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetOverlapping, campgroundId, startDate, endDate);
		while (results.next()) {
			reservation = (mapRowToReservation(results));
			overlappingReservations.add(reservation);
		}
		if (overlappingReservations.size() == 0) {
			System.out.println("No overlapping reservations!");
		}
		return overlappingReservations;
		
	}
	@Override
	public void createReservation(Campground campground, Reservation newReservation) {
		newReservation.setReservationId(getNextReservationId());
		LocalDate createDate = LocalDate.now();
		String sqlCreateReservation = "INSERT INTO reservation (reservation_id, site_id, name, from_date, to_date, create_date) "
				+ "VALUES (?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update(sqlCreateReservation, newReservation.getReservationId(), newReservation.getSiteId(), 
				newReservation.getName(), newReservation.getFromDate(), newReservation.getToDate(), createDate);
		
		

	}
	// Gets next reservation ID.... ONLY USE THIS IF ABSOLUTELY CREATING A NEW RESERVATION
	private int getNextReservationId() {
		SqlRowSet nextIdResult = jdbcTemplate.queryForRowSet(" SELECT nextval('reservation_reservation_id_seq')");
		if (nextIdResult.next()) {
			return nextIdResult.getInt(1);
		} else {
			throw new RuntimeException("Uhoh!  Something went wrong while getting the next id!");
		}
	}
	private Reservation mapRowToReservation(SqlRowSet results) {
		Reservation reservation = new Reservation();
		reservation.setReservationId(results.getInt("reservation_id"));
		reservation.setSiteId(results.getInt("site_id"));
		reservation.setName(results.getString("name"));
		reservation.setFromDate(results.getDate("from_date").toLocalDate());
		reservation.setToDate(results.getDate("to_date").toLocalDate());
		reservation.setReserveDate(results.getDate("create_date").toLocalDate());
		return reservation;
	}
}
