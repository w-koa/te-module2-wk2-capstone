package com.techelevator.model;

import java.time.LocalDate;
import java.util.List;

public interface ReservationDAO {

	
	public List<Reservation> searchForActiveReservations();
	public void createReservation(Campground campground, Reservation newReservation);
	List<Reservation> getOverlappingReservations(Campground campground, LocalDate startDate, LocalDate endDate);
	
}
