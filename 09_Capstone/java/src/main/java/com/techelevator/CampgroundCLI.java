package com.techelevator;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.model.Campground;
import com.techelevator.model.CampgroundDAO;
import com.techelevator.model.CampsiteDAO;
import com.techelevator.model.Park;
import com.techelevator.model.ParkDAO;
import com.techelevator.model.Reservation;
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
	@SuppressWarnings("resource")
	private String getUserInput(String prompt) {
		System.out.print(prompt + " >>> ");
		return new Scanner(System.in).nextLine();
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

	private void listCampgrounds(List<Campground> campgrounds) {
		System.out.println();
		System.out.printf("    %-24s \t%4s \t%5s \t%10s\n", "Name", "Open", "Close", "Daily Fee");
		if(campgrounds.size() > 0) {
			for(int i = 0; i < campgrounds.size(); i++) {
				int counter = i + 1;
				System.out.printf("%1d.) %-26s \t%2s \t%2s   \t$%.2f\n", counter, campgrounds.get(i).getCampgroundName(), 
				campgrounds.get(i).getOpenMonth(), campgrounds.get(i).getCloseMonth(), campgrounds.get(i).getDailyFee());
				
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
			listCampgrounds(campgroundDAO.getCampgroundsByParkName(parkName));
			handleCampgroundMenu(parkName);
		} 
	}

	public void handleCampgroundMenu(String parkName) {
		System.out.println("Campground Info and Menu");
		String choice = (String)menu.getChoiceFromOptions(menuOptions.CAMPGROUND_MENU_OPTIONS);
		if (choice.equals(menuOptions.CAMPGROUND_MENU_OPTION_SEARCH_FOR_AVAILABLE_RESERVATION)) {
			listCampgrounds(campgroundDAO.getCampgroundsByParkName(parkName));
			handleReservationSearch(parkName);
		} 
	}
	
	public void handleReservationSearch(String parkName) {
		System.out.println();
		System.out.println("Enter Campground (enter 0 to cancel): ");
		List<Campground> campgrounds = campgroundDAO.getCampgroundsByParkName(parkName);
		String[] campgroundNames = new String[campgrounds.size()];
		for (int i = 0; i < campgrounds.size(); i++) {
			campgroundNames[i] = campgrounds.get(i).getCampgroundName();
		}
		String campgroundReserveString = getUserInput("Enter Campground (enter 0 to cancel):");
		String reserveStartString = getUserInput("Enter start date (yyyy-mm-dd): ");
		String reserveEndString = getUserInput("Enter end date (yyyy-mm-dd): ");
		
		List<Reservation> availableReservations = new ArrayList<>();
		
		System.out.println(campgroundReserveString);
		System.out.println(reserveStartString);
		System.out.println(reserveEndString);
		
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
