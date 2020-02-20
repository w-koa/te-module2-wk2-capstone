package com.techelevator.model;

import java.util.List;

public interface CampgroundDAO {

	public List<Campground> getAllCampgrounds();
	public List<Campground> getCampgroundById(int id);
	public List<Campground> getCampgroundByParkId(int id);
	public List<Campground> getCampgroundsByParkName(String parkName);
	
	
}
