package com.techelevator.model;

import java.util.List;

public interface ReservationDAO {

	
	public List<Reservation> searchForAvailableReservations();
	public void createReservation();
	
}
