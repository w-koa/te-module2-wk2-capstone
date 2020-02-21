package com.techelevator.model;

import java.util.List;

public interface CampsiteDAO {

	public List<Campsite> getAllCampsitesInCampground(Campground campground);
	public List<Campsite> getTopFiveCampsites(Campground campground, List<Reservation> overlappingReservations); 
	
}
