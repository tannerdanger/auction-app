package users;

import auctiondata.Auction;

import java.time.LocalDate;
import java.util.Scanner;
/**
 * Contact person class, saves relevent information to a Contact
 */
public class ContactPerson extends User {
	/**
	 * serialized id
	 */
	private static final long serialVersionUID = -710092057770182337L;
	private static final int REQUIRED_YEARS_IN_BETWEEN_AUCTION = 1;
	/*
	 * The Contact Person's previous Auction
	 */
	private Auction myPriorAuction;
	/*
	 * The Contact Person's current Auction
	 */
	private Auction myCurrentAuction;
	
	/*
	 * Constructor for Contact Person, Will take a First name, Last name, and email.
	 * Will set the current and prior auction to null.
	 */
	public ContactPerson(String theFirst, String theLast, String theEmail) {
		super(theFirst, theLast, theEmail); //pass basic ID values to User superclass
		myPriorAuction = null;
		myCurrentAuction = null;
		
	}
	/*
	 * Tests for valid date of a new auction. 
	 * If the Contact Person has had no previous auction, return true
	 * If the Contact Person's new auction date is greater to or equal to one year passed
	 * 		the previous auction date, return true
	 * False otherwise
	 */
	public boolean isValidDate(LocalDate theNewDate) {
		boolean flag = false;
		if (myPriorAuction == null) {
			flag = true;
		} else {
			LocalDate checkForOneYearPassDate = 
					myPriorAuction.getAuctionDate().plusYears(REQUIRED_YEARS_IN_BETWEEN_AUCTION);
			
			int num = checkForOneYearPassDate.compareTo(theNewDate);
			if (num <= 0) {
				flag = true;
			}
		}
		return flag;
	}
	
	/*
	 * Goes through the process of submitting an auction request.
	 * Asks user for the date of the new auction.
	 * Checks the validity of this date.
	 * If valid, it lets the Contact Person know and then creates this auction and sets it to
	 * 		the Contact Person's current auction.
	 * 		Side note, this does not yet check for current auction as I'm not sure yet sure how we will
	 * 		deal with transitioning current auctions to prior auctions yet.
	 */
	public void submitAuctionRequest() {
		Scanner Scan = new Scanner(System.in);
		System.out.println("When do you plan to host your auction?");
		System.out.println("Please enter Time and Date (24Hour time (HH:MI); MM/DD/YYYY:");
		String scannedLine = Scan.nextLine();
		
		Scanner lineScan = new Scanner(scannedLine);
	    lineScan.useDelimiter("/");
		
	    int theMonth = lineScan.nextInt();	
		int theDay = lineScan.nextInt();
		int theYear = lineScan.nextInt();
		
		System.out.println("Validating your auction inventory sheet...");
		LocalDate aDate = LocalDate.of(theYear, theMonth, theDay);	
				
		if (myPriorAuction != null) {
			boolean flag;
			
			flag = isValidDate(aDate);
		
			if (flag) {
				System.out.println("Failure to pass submit auction, one year has not elapsed");
			}
		} else {
			System.out.println("Auction Inventory Sheet confirmed.");
			System.out.println("Your Auction is booked on " + aDate.toString());
			Auction newAuction = new Auction();
			myCurrentAuction = newAuction;
			
			
			//TODO ITEM INVENTORY SHEET PRINTOUT
			System.out.println("Here is your inventory sheet: ");
		}
			
		
		Scan.close();
		lineScan.close();
	}
	//Gets the Contact Person's current auction
	//May not be necessary.
	public Auction getPriorAuction() {
		return myPriorAuction;
	}
	//Sets the Contact Person's current auction
	//May not be necessary
	public void setPriorAuction(Auction theAuction) {
		myPriorAuction = theAuction;
	}
}