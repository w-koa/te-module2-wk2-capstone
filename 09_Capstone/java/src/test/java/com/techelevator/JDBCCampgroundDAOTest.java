package com.techelevator;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.model.Campground;
//import com.techelevator.model.Park;
import com.techelevator.view.JDBCCampgroundDAO;

public class JDBCCampgroundDAOTest {

	private static SingleConnectionDataSource dataSource;
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
		String sqlInsertCampGround = "INSERT INTO campground (campground_id, park_id, name, open_from_mm, open_to_mm, daily_fee) "
				+ " VALUES (8, 4, 'CAMPTEST', '05', '10', 100) ";

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sqlInsertPark);
		jdbcTemplate.update(sqlInsertCampGround);
		campgroundDAO = new JDBCCampgroundDAO(dataSource); 

	}

	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}


	@Test
	public void test_to_get_campground_by_park_Name() {
		List<Campground> allCampgroundInPark = campgroundDAO.getCampgroundsByParkName("TEST PARK");

		String expectedCampgroundName = "CAMPTEST";
		assertNotNull(allCampgroundInPark);
		assertEquals(expectedCampgroundName,
				allCampgroundInPark.get(allCampgroundInPark.size() - 1).getCampgroundName());
	}

}
