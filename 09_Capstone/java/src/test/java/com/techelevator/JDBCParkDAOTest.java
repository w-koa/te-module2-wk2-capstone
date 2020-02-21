package com.techelevator;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Park;
import com.techelevator.model.ParkDAO;
import com.techelevator.view.JDBCParkDAO;

public class JDBCParkDAOTest {
	private static SingleConnectionDataSource dataSource;
	private JDBCParkDAO parkDao;

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

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sqlInsertPark);
		parkDao = new JDBCParkDAO(dataSource);
	}

	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}

	@Test
	public void test_to_get_all_park() {

		List<Park> allPark = parkDao.getAllParks();
		
		String expectedParkName = "TEST PARK";
		assertNotNull(allPark);
		assertEquals(expectedParkName, allPark.get(allPark.size() - 1).getName());
	}

	@Test
	public void test_display_park_info() {
		List<Park> allPark = parkDao.getAllParks();
		
		int expectedParkId = 4;
		String expectedParkName = "TEST PARK";
		String expectedLocation = "MICHIGAN";
		LocalDate expectedEstablishDate = LocalDate.of (1910,05,01);
		int expectedArea = 10;
		int expectedVisitors = 100;
		String expectedDescription = "BEST PARK";

		assertNotNull(allPark);
		assertEquals(expectedParkName, allPark.get(allPark.size() - 1).getName());
		assertEquals(expectedLocation, allPark.get(allPark.size() - 1).getLocation());
		assertEquals(expectedEstablishDate, allPark.get(allPark.size() - 1).getEstablishDate());
		assertEquals(expectedArea, allPark.get(allPark.size() - 1).getArea());
		assertEquals(expectedVisitors, allPark.get(allPark.size() - 1).getVisitors());
		assertEquals(expectedDescription, allPark.get(allPark.size() - 1).getDescription());

	}

}
