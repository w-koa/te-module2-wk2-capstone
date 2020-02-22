package com.techelevator.model;

import java.time.LocalDate;
import java.util.List;

public interface ReservationDAO {

	public void createReservation(Reservation newReservation);
	public List<Reservation> getOverlappingReservations(int campgroundId, LocalDate startDate, LocalDate endDate);
	public int getNextReservationId();
	
}
