package com.techelevator.view;

import java.time.LocalDate;

public class TestingStuff {

	public static void main(String[] args) {
		LocalDate now = LocalDate.now();
		System.out.println(now);
		
		LocalDate checkStartDate = LocalDate.now();
		LocalDate checkEndDate = LocalDate.of(2020, 4, 20);
		
		String sampleMonth = "05";
		int sampleInteger = Integer.parseInt(sampleMonth);
		System.out.println(sampleInteger);
		
//		if (((checkStartDate.getMonthValue() < Integer.parseInt(checkCampgroundToReserve.getOpenMonth())) &&
//				(checkStartDate.getMonthValue() > Integer.parseInt(checkCampgroundToReserve.getCloseMonth())))
//				|| ((checkEndDate.getMonthValue() < Integer.parseInt(checkCampgroundToReserve.getOpenMonth()))
//				&& (checkEndDate.getMonthValue() > Integer.parseInt(checkCampgroundToReserve.getCloseMonth())))) {
//			System.out.println("Sorry, campground is closed on that date.");
//		}
	}

}
