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
import com.techelevator.view.JDBCCampgroundDAO;
import com.techelevator.view.JDBCParkDAO;

public class JDBCCampgroundDAOTest {

		private static SingleConnectionDataSource dataSource;
		private JDBCCampgroundDAO campgroundDAO;
		private JDBCParkDAO parkDAO;

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
//			String sqlInsertPark = "INSERT INTO park (park_id, name, location, establish_date, area, visitors, description )"
//					+ " VALUES (4 , 'TEST PARK', 'MICHIGAN', '1910-05-01', 10, 100,'BEST PARK') ";
			
			String sqlInsertCampGround = "INSERT INTO campground (campground_id, park_id, name, open_from_mm, open_to_mm, daily_fee) "
					+ " VALUES (8 ,1, 'CAMPTEST', 05, 10, 100) ";
		

			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
//			jdbcTemplate.update(sqlInsertPark);
			campgroundDAO = new JDBCCampgroundDAO(dataSource);
			parkDAO = new JDBCParkDAO(dataSource);
		}

		@After
		public void rollback() throws SQLException {
			dataSource.getConnection().rollback();
		}

	@Test
	public void test_to_get_all_campgrounds() {
		List<Campground> allCampgrounds = campgroundDAO.getAllCampgrounds();
		String expectedCampgroundName = "CAMPTEST";
		assertNotNull(allCampgrounds);
		assertEquals(expectedCampgroundName, allCampgrounds.get(allCampgrounds.size()-1).getCampgroundName());
		
	}

}
