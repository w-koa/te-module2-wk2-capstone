package com.techelevator;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Reservation;
import com.techelevator.view.JDBCReservationDAO;

public class JDBCReservationDAOTest {

	private static SingleConnectionDataSource dataSource;
	private JDBCReservationDAO reservationDAO;
	
	@BeforeClass
	public static void setupDataSource() {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");  
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		dataSource.setAutoCommit(false);
	}

	@AfterClass
	public static void closeDataSource() { 
		dataSource.destroy();
	}

	@Before
	public void setup() {

		String sqlInsertPark = "INSERT INTO park (park_id, name, location, establish_date, area, visitors, description )"
				+ " VALUES (4 , 'TEST PARK', 'MICHIGAN', '1910-05-01', 10, 100,'BEST PARK') ";
		String sqlInsertCampGround = "INSERT INTO campground (campground_id, park_id, name, open_from_mm, open_to_mm, daily_fee) "
				+ " VALUES (8, 4, 'CAMPTEST', '05', '10', 100) ";
		String sqlInsertSite = "INSERT INTO site (site_id, campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities) "
				+ " VALUES (623 , 8 , 1, 5, false, 0, true ) ";
		String sqlInsertReservationActive = "INSERT INTO reservation (reservation_id, site_id, name, from_date, to_date, create_date) "
				+ " VALUES (45 , 623, 'Sonthaya Deelua', '2020-02-18', '2020-02-25', '2020-02-21') ";

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sqlInsertPark);
		jdbcTemplate.update(sqlInsertCampGround);
		jdbcTemplate.update(sqlInsertSite);
		jdbcTemplate.update(sqlInsertReservationActive);
		reservationDAO = new JDBCReservationDAO(dataSource);

	}

	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}
	
	@Test
	public void test_create_new_reservation() {
		List<Reservation> reservations = new ArrayList<>();
		Reservation test = new Reservation();
		//No need to set reservation ID because we already have method getNextReservationId in main menu
		test.setSiteId(623);
		test.setName("TEST");
		test.setFromDate(LocalDate.of(2020, 02, 28));
		test.setToDate(LocalDate.of(2020, 04, 2));
		reservations.add(test);
		
		reservationDAO.createReservation(test);

		assertEquals(1, reservations.size());
		assertEquals(623, reservations.get(reservations.size()-1).getSiteId());
		assertEquals("TEST", reservations.get(reservations.size()-1).getName()); 
	}
	
	@Test
	public void test_to_get_overlapping_reservation() {
		List<Reservation> activeBooked = reservationDAO.getOverlappingReservations( 8, LocalDate.parse("2020-02-22"), 
				LocalDate.parse("2020-02-23"));
		
		assertEquals(1, activeBooked.size());
		
	}
}
