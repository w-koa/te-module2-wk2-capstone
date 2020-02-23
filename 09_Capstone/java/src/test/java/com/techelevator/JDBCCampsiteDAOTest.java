package com.techelevator;

import static org.junit.Assert.*;

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

import com.techelevator.model.Campground;
import com.techelevator.model.Campsite;
import com.techelevator.model.Reservation;
import com.techelevator.view.JDBCCampgroundDAO;
import com.techelevator.view.JDBCCampsiteDAO;

public class JDBCCampsiteDAOTest {

	private static SingleConnectionDataSource dataSource;
	private JDBCCampsiteDAO campsiteDAO;
	private JDBCCampgroundDAO campgroundDAO;

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
//		String sqlInsertCampGround = "INSERT INTO campground (campground_id, park_id, name, open_from_mm, open_to_mm, daily_fee) "
//				+ " VALUES (8, 4, 'CAMPTEST', '05', '10', 100) ";
//		String sqlInsertSiteA = "INSERT INTO site (site_id, campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities) "
//				+ " VALUES (623 , 8 , 1, 5, false, 0, true ) ";
//		String sqlInsertSiteB = "INSERT INTO site (site_id, campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities) "
//				+ " VALUES (624 , 8 , 2, 6, false, 0, true ) ";
//		String sqlInsertReservationActive = "INSERT INTO reservation (reservation_id, site_id, name, from_date, to_date, create_date) "
//				+ " VALUES (9999 , 623, 'Sonthaya Deelua', '2020-02-18', '2020-02-25', '2020-02-21') ";

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sqlInsertPark);
//		jdbcTemplate.update(sqlInsertCampGround);
//		jdbcTemplate.update(sqlInsertSiteA);
//		jdbcTemplate.update(sqlInsertSiteB);
//		jdbcTemplate.update(sqlInsertReservationActive);
	}

	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}

	@Test
	public void test_to_get_all_campsite_in_campground() {
		
		List<Campground> campgrounds = new ArrayList<>();
		Campground test = new Campground();
		test.setCampgroundId(8);
		test.setParkId(4);
		test.setCampgroundName("CAMPTEST");
		test.setOpenMonth("05");
		test.setCloseMonth("10");
		test.setDailyFee(100);
		campgrounds.add(test);
		
		List<Campsite> campsites = new ArrayList<>();
		Campsite testCampsite = new Campsite();
		testCampsite.setSiteId(623);
		testCampsite.setCampgroundId(8);
		testCampsite.setSiteNumber(1);
		testCampsite.setMaxOccupancy(5);
		testCampsite.setAccessible(false);
		testCampsite.setMaxRVLength(0);
		testCampsite.setHasUtilities(true);
		campsites.add(testCampsite);
		
		
		campsiteDAO.getAllCampsitesInCampground(test);//seem we have problem with this line.
		JdbcTemplate jdbc = new JdbcTemplate(dataSource);
		String sqlFindTest = "SELECT site_id, cg.campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities "
				+ "FROM site s JOIN campground cg ON cg.campground_id = s.campground_id "
				+ "WHERE cg.campground_id = 8 ";
				
		SqlRowSet results = jdbc.queryForRowSet(sqlFindTest, test.getCampgroundId());
		while (results.next()) {
			campsites.add(campsiteDAO.mapRowToCampsite(results));
		}
		assertEquals(623 , campsites.get(0).getSiteId());

	}

}
