package com.techelevator.model;

import java.util.List;

public interface CampsiteDAO {

	public List<Campsite> getAllCampsites();
	public List<Campsite> getTopFiveCampsites(); // Does this belong here or Reservations
	
}
