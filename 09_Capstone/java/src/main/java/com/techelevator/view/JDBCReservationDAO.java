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
	public List<Reservation> searchForActiveReservations() {
		List<Reservation> availableReservations = new ArrayList<>();
		
		String sqlFindReservations = "SELECT * FROM site s JOIN reservation r ON r.site_id = s.site_id " +
				 "WHERE((to_date > CURRENT_DATE) OR (to_date IS NULL)) AND (from_date IS NOT NULL) "; 

		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlFindReservations);
		
		while(results.next()) {
			availableReservations.add(mapRowToReservation(results));
		}
		return availableReservations;
	}

	@Override
	public List<Reservation> getOverlappingReservations(Campground campground, LocalDate startDate, LocalDate endDate) {
		List<Reservation> overlappingReservations = new ArrayList<>();
		Reservation reservation = null;
		String sqlGetOverlapping = "SELECT * FROM reservation r "
				+ "JOIN site s ON s.site_id = r.site_id "
				+ "WHERE s.campground_id = ? AND ? BETWEEN from_date AND to_date "
				+ "OR ? BETWEEN from_date AND to_date";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetOverlapping, campground.getCampgroundId(), startDate, endDate);
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
	public void createReservation(Campground campground, int siteId, String name, LocalDate startDate, LocalDate endDate) {
		int reservationId = getNextReservationId();
		LocalDate createDate = LocalDate.now();
		String sqlCreateReservation = "INSERT INTO reservation (reservation_id, site_id, name, from_date, to_date, create_date) "
				+ "VALUES (?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update(sqlCreateReservation, reservationId, siteId, name, startDate, endDate, createDate);
		System.out.println("Thank you for booking a reservation at " + campground.getCampgroundName());
		System.out.println("Reservation ID: " + reservationId);
		

	}
	// Gets next reservation ID.... ONLY USE THIS IF ABSOLUTELY CREATING A NEW RESERVATION
	private int getNextReservationId() {
		SqlRowSet nextIdResult = jdbcTemplate.queryForRowSet(" SELECT nextval('seq_reservation_id')");
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
