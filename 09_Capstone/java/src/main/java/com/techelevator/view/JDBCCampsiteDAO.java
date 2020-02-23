package com.techelevator.view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Campground;
import com.techelevator.model.Campsite;
import com.techelevator.model.CampsiteDAO;
import com.techelevator.model.Reservation;

public class JDBCCampsiteDAO implements CampsiteDAO { 

	private JdbcTemplate jdbcTemplate;

	public JDBCCampsiteDAO(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Campsite> getAllCampsitesInCampground(Campground campground) {
		List<Campsite> allCampsitesInCampground = new ArrayList<>();
		String sqlGetAllCampsiteByCampgroundId = "SELECT site_id, cg.campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities "
				+ "FROM site s JOIN campground cg ON cg.campground_id = s.campground_id "
				+ "WHERE cg.campground_id = ? "
				+ "GROUP BY s.site_id, cg.campground_id, s.site_number, s.max_occupancy, s.accessible, s.max_rv_length, s.utilities "
				+ "ORDER BY s.site_id";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllCampsiteByCampgroundId, campground.getCampgroundId());
		while (results.next()) {
			allCampsitesInCampground.add(mapRowToCampsite(results));
		}
		return allCampsitesInCampground;
	}

	@Override
	public List<Campsite> getTopFiveCampsites(Campground campground, List<Reservation> overlappingReservations) {
		List<Campsite> topFiveCampsitesAvailable = new ArrayList<>();
		List<Campsite> campsitesInCampground = getAllCampsitesInCampground(campground);
		List<Reservation> overlapping = overlappingReservations;
		for (int i = 0; i < campsitesInCampground.size(); i++) {
			for (int j = 0; j < overlapping.size(); j++) {
				if (campsitesInCampground.get(i).getSiteId() == overlapping.get(j).getSiteId()) {
					campsitesInCampground.remove(i);
				}
			}
		}
		int campCount = campsitesInCampground.size();
		if (campCount < 5) {
			for (int i = 0; i < campsitesInCampground.size(); i++) {
				topFiveCampsitesAvailable.add(campsitesInCampground.get(i));
			}
		} else {
			for (int i = 0; i < 5; i++) {
				topFiveCampsitesAvailable.add(campsitesInCampground.get(i));
			}
		}
		

		return topFiveCampsitesAvailable;
	}
	//set public for test purpose only
	public Campsite mapRowToCampsite(SqlRowSet results) {
		Campsite campsite = new Campsite();

		campsite.setSiteId(results.getInt("site_id"));
		campsite.setCampgroundId(results.getInt("campground_id"));
		campsite.setSiteNumber(results.getInt("site_number"));
		campsite.setMaxOccupancy(results.getInt("max_occupancy"));
		campsite.setAccessible(results.getBoolean("accessible"));
		campsite.setMaxRVLength(results.getInt("max_rv_length"));
		campsite.setHasUtilities(results.getBoolean("utilities"));
		return campsite;
	}
}
