package com.techelevator.view;

import java.io.ObjectInputStream.GetField;
import java.time.LocalDate;
import java.util.List;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import com.techelevator.model.Campground;
import com.techelevator.model.CampgroundDAO;
import com.techelevator.model.Park;
import com.techelevator.model.ParkDAO;

public class TestingStuff {

	private JdbcTemplate jdbcTemplate;

	public static void main(String[] args) {

		ParkDAO parkDAO;
		CampgroundDAO campgroundDAO;
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");

		parkDAO = new JDBCParkDAO(dataSource);
		campgroundDAO = new JDBCCampgroundDAO(dataSource);

		LocalDate now = LocalDate.now();
		System.out.println(now);

		LocalDate checkStartDate = LocalDate.now();
		LocalDate checkEndDate = LocalDate.of(2020, 4, 20);

		String sampleMonth = "05";
		int sampleInteger = Integer.parseInt(sampleMonth);
	System.out.println(sampleInteger);

//		List<Park> parks = parkDAO.getAllParks();
		
			
//			campground.setCampgroundOpenMonths(campgroundOpenMonths);
		
		
		List<Campground> campgrounds = campgroundDAO.getAllCampgrounds();
	
		int openMonth = Integer.parseInt(campgrounds.get(2).getOpenMonth());
		int closeMonth = Integer.parseInt(campgrounds.get(2).getCloseMonth());
		int difference = closeMonth - openMonth;
		int[] campgroundOpenMonths = new int[difference];
		for (int i = openMonth, j = 0; i < closeMonth; i++, j++) {
			campgroundOpenMonths[j] = i + 1;
		}
		System.out.println(openMonth);
		System.out.println(closeMonth);
		System.out.println(difference);
		
		campgrounds.get(2).setCampgroundOpenMonths(campgroundOpenMonths);
		for (int month : campgroundOpenMonths) {
			System.out.println(month);
		}
		campgroundDAO.getCampgroundOpenMonths(campgrounds.get(2));
//		if (((checkStartDate.getMonthValue() < Integer.parseInt(checkCampgroundToReserve.getOpenMonth())) &&
//				(checkStartDate.getMonthValue() > Integer.parseInt(checkCampgroundToReserve.getCloseMonth())))
//				|| ((checkEndDate.getMonthValue() < Integer.parseInt(checkCampgroundToReserve.getOpenMonth()))
//				&& (checkEndDate.getMonthValue() > Integer.parseInt(checkCampgroundToReserve.getCloseMonth())))) {
//			System.out.println("Sorry, campground is closed on that date.");
//		}
	}

}
