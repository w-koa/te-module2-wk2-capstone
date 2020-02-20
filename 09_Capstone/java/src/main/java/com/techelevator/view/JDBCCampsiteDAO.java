package com.techelevator.view;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.techelevator.model.Campsite;
import com.techelevator.model.CampsiteDAO;

public class JDBCCampsiteDAO implements CampsiteDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCCampsiteDAO(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<Campsite> getAllCampsites() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Campsite> getTopFiveCampsites() {
		// TODO Auto-generated method stub
		return null;
	}

}
