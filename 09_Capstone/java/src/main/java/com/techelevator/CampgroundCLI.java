package com.techelevator;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import javax.xml.bind.ValidationException;

import org.apache.commons.dbcp2.BasicDataSource;

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
	
	public void run() {
		while (true) {
			List<Park> parks = parkDAO.getAllParks();
			// Alternate method to show park names. Left in to reference later.
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

			if (selectedParkName.equals(MenuOptions.MENU_OPTION_QUIT)) {
				break;
			} else {
				// Prints Park Info
				System.out.println(savedPark.getName() + " National Park \nLocation:\t " + savedPark.getLocation()
						+ "\nEstablished:\t " + savedPark.getEstablishDate().format(DateTimeFormatter.ofPattern("MM/dd/YYYY")));
				System.out.printf("Area:\t\t %,d Sq. km", savedPark.getArea());
				System.out.printf("\nVisitors:\t %,d \n", savedPark.getVisitors());
				// String Builder formats the description of park from one long string to a few shorter ones.
				StringBuilder sb = new StringBuilder(savedPark.getDescription());
				int i = 0;
				while (i + 80 < sb.length() && (i = sb.lastIndexOf(" ", i + 80)) != -1) {
				    sb.replace(i, i + 1, "\n");
				}
				System.out.println("\n" + sb.toString());
				
				handleParkMenu(savedPark);

			}

		}
	}
	// Display park menu and get user input.
	public void handleParkMenu(Park savedPark) {
		System.out.println("\nPark Info and Menu");
		String choice = (String) menu.getChoiceFromOptions(MenuOptions.PARK_MENU_OPTIONS);
		if (choice.equals(MenuOptions.PARK_MENU_OPTION_VIEW_CAMPGROUNDS)) {
			listCampgrounds(campgroundDAO.getCampgroundsByParkName(savedPark.getName()));
			System.out.println();
		} else if (choice.equals(MenuOptions.PARK_MENU_OPTION_SEARCH_FOR_RESERVATION)) {
			listCampgrounds(campgroundDAO.getCampgroundsByParkName(savedPark.getName()));
			handleCampgroundMenu(savedPark);
		}
	}
	
	// Prints formatted information about campgrounds.
	private void listCampgrounds(List<Campground> campgrounds) {
		System.out.printf("\n    %-24s \t%4s \t%5s\t%10s\n", "Name", "Open", "Close", "Daily Fee");
		if (campgrounds.size() > 0) {
			for (int i = 0; i < campgrounds.size(); i++) {
				int counter = i + 1;
				System.out.printf("%1d.) %-26s \t%2s \t%2s   \t $%.2f\n", counter,
						campgrounds.get(i).getCampgroundName(), campgrounds.get(i).getOpenMonth(),
						campgrounds.get(i).getCloseMonth(), campgrounds.get(i).getDailyFee());

			}
		} else {
			System.out.println("\n*** No results ***");
		}
	}
	
	// Display campground menu and get input
	public void handleCampgroundMenu(Park savedPark) {
		System.out.println("\nCampground Info and Menu");
		String choice = (String) menu.getChoiceFromOptions(MenuOptions.CAMPGROUND_MENU_OPTIONS);
		if (choice.equals(MenuOptions.CAMPGROUND_MENU_OPTION_SEARCH_FOR_AVAILABLE_RESERVATION)) {
			handleReservationSearch(savedPark);
		} else {
			handleParkMenu(savedPark);
		}
	}
	
	// Display reservation search menu and gets user input
	public void handleReservationSearch(Park park) {
		System.out.println();
		System.out.println("Campground Reservation Search");
		List<Campground> campgrounds = campgroundDAO.getCampgroundsByParkName(park.getName());
		listCampgrounds(campgrounds);
		String campgroundOption = getUserInput("Enter Campground (enter 0 to cancel): ");
		if (Integer.parseInt(campgroundOption) == 0) {
			// Cancels search and goes back to previous menu
			handleCampgroundMenu(park);
		} else {
			Campground campgroundToReserve = campgrounds.get(Integer.parseInt(campgroundOption) - 1);
			LocalDate reservationStartDate = getSafeUserDate("Enter start date (yyyy-mm-dd): ");
			LocalDate reservationEndDate = getSafeUserDate("Enter end date (yyyy-mm-dd): ");

			Month campOpenMonth = Month.of(Integer.parseInt(campgroundToReserve.getOpenMonth()));
			Month campCloseMonth = Month.of(Integer.parseInt(campgroundToReserve.getCloseMonth()));

			try {
				validateReservation(reservationStartDate, reservationEndDate, campOpenMonth, campCloseMonth);
			} catch (ValidationException e) {
				System.out.println(e.getMessage());
				handleReservationSearch(park);
				return;
			}
			
			List<Reservation> overlappingReservations = reservationDAO
					.getOverlappingReservations(campgroundToReserve.getCampgroundId(), reservationStartDate, reservationEndDate);
			List<Campsite> availableCampsites = campsiteDAO.getTopFiveCampsites(campgroundToReserve,
					overlappingReservations);

			listCampsites(campgroundToReserve, availableCampsites, reservationStartDate, reservationEndDate);

			// Put into another handler maybe
			String campsiteOption = getUserInput("Enter the Campsite you would like to reserve: ");
			Campsite campsiteToReserve = availableCampsites.get(Integer.parseInt(campsiteOption) - 1);
			String reservationName = getUserInput("Enter name to reserve under: ");
			Reservation reservation = saveReservation(reservationStartDate, reservationEndDate,
					campsiteToReserve, reservationName);
			System.out.println("Thank you for booking a reservation at " + campgroundToReserve.getCampgroundName());
			System.out.println("Reservation ID: " + reservation.getReservationId() + "\n");
		}
	}

	private Reservation saveReservation(LocalDate checkStartDate, LocalDate checkEndDate,
			Campsite campsiteToReserve, String reservationName) {
		Reservation newReservation = new Reservation();
		newReservation.setReservationId(reservationDAO.getNextReservationId());
		newReservation.setSiteId(campsiteToReserve.getSiteId());
		newReservation.setName(reservationName);
		newReservation.setFromDate(checkStartDate);
		newReservation.setToDate(checkEndDate);
		reservationDAO.createReservation(newReservation);
		return newReservation;
	}
	
	// Displays formatted information about campsites. User does not see site ID.
	private void listCampsites(Campground campground, List<Campsite> campsites, LocalDate startInput,
			LocalDate endInput) {
		System.out.println();
		System.out.printf("    %8s \t%10s \t%10s\t%-10s \t%10s\n", "Site No.", "Max Occup.", "Accessible", "Utilities",
				"Total Cost");

		if (campsites.size() > 0) {
			for (int i = 0; i < campsites.size(); i++) {
				double totalCost = 0;
				Duration diff = Duration.between(startInput.atStartOfDay(), endInput.atStartOfDay());
				long diffDays = diff.toDays();
				totalCost = campground.getDailyFee() * (diffDays);
				System.out.printf("%1d.) %-4s \t%-5s \t\t%5s  \t\t%5s \t\t$%.2f\n", i + 1,
						campsites.get(i).getSiteNumber(), campsites.get(i).getMaxOccupancy(),
						campsites.get(i).isAccessible(), campsites.get(i).isHasUtilities(), totalCost);

			}
		} else {
			System.out.println("\n*** No results ***");
		}
	}
	
	// Checks if months are within campground open season. 
	public boolean isMonthWithinRange(Month campOpen, Month campClose, Month dateToCheck) {
		return dateToCheck.compareTo(campOpen) >= 0 && dateToCheck.compareTo(campClose) <= 0;
	}

	public boolean isDurationValid(Month campOpen, Month campClose, LocalDate startDateToCheck,
			LocalDate endDateToCheck) {
		if (campOpen.equals(Month.JANUARY) && campClose.equals(Month.DECEMBER)) {
			return true;
		}
		if (startDateToCheck.getYear() == endDateToCheck.getYear()) {
			return true;
		}
		return false;
	}

	// Checks that a reservation is valid. Throws exception for different error situations and gives user 
	// relevant feedback on their error.
	private void validateReservation(LocalDate checkStartDate, LocalDate checkEndDate, Month openMonth,
			Month closeMonth) throws ValidationException {
		if (checkStartDate.isBefore(LocalDate.now())) {
			throw new ValidationException("\nSorry, reservations must be for future dates.");
		} else if (!isMonthWithinRange(openMonth, closeMonth, checkStartDate.getMonth())
				|| !isMonthWithinRange(openMonth, closeMonth, checkEndDate.getMonth())) {
			throw new ValidationException("\nSorry, campground is closed on that date.");
		} else if (!checkStartDate.isBefore(checkEndDate)) {
			throw new ValidationException("\nStarting date must be before ending date.");
		} else if (!isDurationValid(openMonth, closeMonth, checkStartDate, checkEndDate)) {
			throw new ValidationException("\nSorry, your reservation is too long.");
		}
	}

	// Gets LocalDate from user input, must have correct format or exception thrown.
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
}
