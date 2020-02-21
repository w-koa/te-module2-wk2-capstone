package com.techelevator;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.dbcp2.BasicDataSource;
import org.postgresql.util.LruCache.CreateAction;

import com.techelevator.model.Campground;
import com.techelevator.model.CampgroundDAO;
import com.techelevator.model.Campsite;
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
		System.out.printf("\n    %-24s \t%4s \t%5s \t%10s\n", "Name", "Open", "Close", "Daily Fee");
		if (campgrounds.size() > 0) {
			for (int i = 0; i < campgrounds.size(); i++) {
				int counter = i + 1;
				System.out.printf("%1d.) %-26s \t%2s \t%2s   \t$%.2f\n", counter,
						campgrounds.get(i).getCampgroundName(), campgrounds.get(i).getOpenMonth(),
						campgrounds.get(i).getCloseMonth(), campgrounds.get(i).getDailyFee());

			}
		} else {
			System.out.println("\n*** No results ***");
		}
	}

	private void listCampsites(Campground campground, List<Campsite> campsites, LocalDate startInput,
			LocalDate endInput) {
		System.out.println();
		System.out.printf("    %8s \t%10s \t%10s\t%-10s \t%10s\n", "Site No.", "Max Occup.", "Accessible", "Utilities",
				"Total Cost");

		if (campsites.size() > 0) {
			for (int i = 0; i < campsites.size(); i++) {
				int counter = i + 1;
				double totalCost = 0;
				LocalDate endDate = endInput;
				LocalDate startDate = startInput;
				Duration diff = Duration.between(startDate.atStartOfDay(), endDate.atStartOfDay());
				long diffDays = diff.toDays();
				totalCost = campground.getDailyFee() * (diffDays);
				System.out.printf("%1d.) %-4s \t%-5s \t\t%5s  \t\t%5s \t\t$%.2f\n", counter,
						campsites.get(i).getSiteNumber(), campsites.get(i).getMaxOccupancy(),
						campsites.get(i).isAccessible(), campsites.get(i).isHasUtilities(), totalCost);

			}
		} else {
			System.out.println("\n*** No results ***");
		}
	}

	public void handleParkMenu(Park savedPark) {
		System.out.println("\nPark Info and Menu");
		String choice = (String) menu.getChoiceFromOptions(menuOptions.PARK_MENU_OPTIONS);
		if (choice.equals(menuOptions.PARK_MENU_OPTION_VIEW_CAMPGROUNDS)) {
			listCampgrounds(campgroundDAO.getCampgroundsByParkName(savedPark.getName()));
		} else if (choice.equals(menuOptions.PARK_MENU_OPTION_SEARCH_FOR_RESERVATION)) {
			listCampgrounds(campgroundDAO.getCampgroundsByParkName(savedPark.getName()));
			handleCampgroundMenu(savedPark);
		}
	}

	public void handleCampgroundMenu(Park savedPark) {
		System.out.println("\nCampground Info and Menu");
		String choice = (String) menu.getChoiceFromOptions(menuOptions.CAMPGROUND_MENU_OPTIONS);
		if (choice.equals(menuOptions.CAMPGROUND_MENU_OPTION_SEARCH_FOR_AVAILABLE_RESERVATION)) {
			handleReservationSearch(savedPark);
		}
	}

	public void handleReservationSearch(Park savedPark) {
		System.out.println();
		List<Campground> campgrounds = campgroundDAO.getCampgroundsByParkName(savedPark.getName());
		listCampgrounds(campgrounds);
		String[] campgroundNames = new String[campgrounds.size() + 1];
		for (int i = 1; i < campgrounds.size(); i++) {
			campgroundNames[i] = campgrounds.get(i).getCampgroundName();
		}
		String campgroundReserveString = getUserInput("Enter Campground (enter 0 to cancel): ");
		Campground checkCampgroundToReserve = campgrounds.get(Integer.parseInt(campgroundReserveString));
		if (checkCampgroundToReserve.equals(campgrounds.get(0))) {
			handleCampgroundMenu(savedPark);
		} else {
			LocalDate checkStartDate = getSafeUserDate("Enter start date (yyyy-mm-dd): ");
			LocalDate checkEndDate = getSafeUserDate("Enter end date (yyyy-mm-dd): ");
			int[] openMonths = campgroundDAO.getCampgroundOpenMonths(checkCampgroundToReserve);
			for (int i = 0; i < openMonths.length; i++) {
				if (checkStartDate.getMonthValue() < openMonths[i]
						&& checkEndDate.getMonthValue() > openMonths[i]) {
					System.out.println("Sorry, campground is closed on that date.");
					handleReservationSearch(savedPark);
				}
			}

			List<Reservation> overlappingReservations = reservationDAO
					.getOverlappingReservations(checkCampgroundToReserve, checkStartDate, checkEndDate);
			List<Campsite> availableCampsites = campsiteDAO.getTopFiveCampsites(checkCampgroundToReserve,
					overlappingReservations);

			listCampsites(checkCampgroundToReserve, availableCampsites, checkStartDate, checkEndDate);

			// Put into another handler maybe
			String campsiteReserveString = getUserInput("Enter the Campsite you would like to reserve: ");
			Campsite campsiteToReserve = availableCampsites.get(Integer.parseInt(campsiteReserveString) - 1);
			String reservationName = getUserInput("Enter name to reserve under: ");

			reservationDAO.createReservation(checkCampgroundToReserve, campsiteToReserve.getSiteId(), reservationName,
					checkStartDate, checkEndDate);

		}

	}

	// Gets LocalDate from user input, must have correct format
	private LocalDate getSafeUserDate(String prompt) {
		LocalDate reserveStartDate = null;
		while (reserveStartDate == null) {
			try {
				String reserveStartString = getUserInput(prompt);
				reserveStartDate = LocalDate.parse(reserveStartString);
			} catch (DateTimeParseException e) {
				System.out.println("Invalid date, please follow format (yyyy-mm-dd)");
			}
		}
		return reserveStartDate;
	}

	public void run() {
		while (true) {
			List<Park> parks = parkDAO.getAllParks();
//			parks.stream()
//				.map(Park::getName)
//				.collect(Collectors.toList())
//				.toArray();
			String[] parkNames = new String[parks.size()];
			for (int i = 0; i < parks.size(); i++) {
				parkNames[i] = parks.get(i).getName();
			}
			System.out.println("Welcome to the National Park Reservation System!");
			Park savedPark = null;
			String selectedParkName = (String) menu.getChoiceFromOptions(parkNames);
			for (Park park : parks) {
				if (selectedParkName.equals(park.getName())) {
					savedPark = park;
				}
			}

			if (selectedParkName.equals(menuOptions.MENU_OPTION_QUIT)) {
				break;
			} else {
//				parkDAO.displayParkInfo(selectedParkName);

				System.out.println(savedPark.getName() + " National Park \nLocation:\t" + savedPark.getLocation()
						+ "\nEstablished:\t" + savedPark.getEstablishDate() + "\nArea:\t\t" + savedPark.getArea()
						+ "\nVisitors:\t" + savedPark.getVisitors());
				System.out.println(savedPark.getDescription());
				handleParkMenu(savedPark);
//				handleParkMenu(selectedParkName);

			}

		}
	}

}
