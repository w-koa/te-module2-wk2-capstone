package com.techelevator;

import java.util.List;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.model.Campground;
import com.techelevator.model.CampgroundDAO;
import com.techelevator.model.CampsiteDAO;
import com.techelevator.model.Park;
import com.techelevator.model.ParkDAO;
import com.techelevator.model.ReservationDAO;
import com.techelevator.view.JDBCCampgroundDAO;
import com.techelevator.view.JDBCCampsiteDAO;
import com.techelevator.view.JDBCParkDAO;
import com.techelevator.view.JDBCReservationDAO;
import com.techelevator.view.Menu;
import com.techelevator.view.MenuOptions;

public class CampgroundCLI {

	private Menu menu;
	private ParkDAO parkDAO;
	private CampgroundDAO campgroundDAO;
	private CampsiteDAO campsiteDAO;
	private ReservationDAO reservationDAO;
	private MenuOptions menuOptions;

	public static void main(String[] args) {

		CampgroundCLI application = new CampgroundCLI();
		application.run();
	}

	public CampgroundCLI() {
		this.menu = new Menu(System.in, System.out);

		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");

		parkDAO = new JDBCParkDAO(dataSource);
		campgroundDAO = new JDBCCampgroundDAO(dataSource);
		campsiteDAO = new JDBCCampsiteDAO(dataSource);
		reservationDAO = new JDBCReservationDAO(dataSource);
	}

	private void listParks(List<Park> parks) {
		System.out.println("Park List\n");
		if (parks.size() > 0) {
			for (Park park : parks) {
				System.out.println(park.getName());
			}
		} else {
			System.out.println("\n*** No results ***");
		}
	}
	private void listCampgrounds(List<Campground> campgrounds) {
		System.out.println();
		if(campgrounds.size() > 0) {
			for(Campground campground : campgrounds) {
				System.out.println(campground.getCampgroundName());
			}
		} else {
			System.out.println("\n*** No results ***");
		}
	}
	public void handleParkMenu(String parkName) {
		System.out.println("Park Info and Menu");
		String choice = (String)menu.getChoiceFromOptions(menuOptions.PARK_MENU_OPTIONS);
		if (choice.equals(menuOptions.PARK_MENU_OPTION_VIEW_CAMPGROUNDS)) {
			listCampgrounds(campgroundDAO.getCampgroundsByParkName(parkName));
		} else if (choice.equals(menuOptions.PARK_MENU_OPTION_SEARCH_FOR_RESERVATION)) {
			
		} 
	}

	public void run() {
		while (true) {
			List<Park> parks = parkDAO.getAllParks();
			String[] parkNames = new String[parks.size()];
			for (int i = 0; i < parks.size(); i++) {
				parkNames[i] = parks.get(i).getName();
			}


			String selectedParkName = (String) menu.getChoiceFromOptions(parkNames);
			if (selectedParkName.equals(menuOptions.MENU_OPTION_QUIT)) {
				break;
			} else {
				parkDAO.displayParkInfo(selectedParkName);
				handleParkMenu(selectedParkName);
			}

			

		}
	}

}
