import storage.AuctionCalendar;
import storage.UserDB;
import users.*;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Things I need from other classes:
 * 1. auctionDB needs to print active auctions
 * 2. Auction class toString() needs to print the auction in the format: | <Auction ID#> -- <Auction Date> |
 * 3. Bid class toString() needs to print the item in the format: | <Item><Bid Amount> -- <Auction Date> |
 */
public class ConsoleUI {
	private static Scanner scanner;
	private static AuctionCalendar myAuctionAuctionCalendar;
	private static UserDB myUserDB;
	private static final String USERDB_FILE_NAME = "users.ser";
	private static final String AUCTIONDB_FILE_NAME = "calendar.ser";

	public static void start() {
		scanner = new Scanner(System.in);

		myUserDB = new UserDB();
		myAuctionAuctionCalendar = new AuctionCalendar();
		//deserialize();
		initialize(); //do this if data is empty?


		System.out.println("Login:"); //prompt user to reenter email address, or register new account?
		User currentUser = myUserDB.getUser(scanner.next());//user enters email, system checks userDB for user
		//scanner.close();
		//if user is null (doesn't exist)
		if (null != currentUser) {

			//else if user is a Bidder
			if (Bidder.class.equals(currentUser.getClass())) {
				bidderConsole(currentUser);

				//else user is a contact
			}else if(ContactPerson.class.equals(currentUser.getClass())){
				contactConsole(currentUser);
			}
		}
		//serialize();
	}

	private static void deserialize() {

		//Load the users data
		myUserDB = null;
		try {
			FileInputStream fileIn = new FileInputStream(USERDB_FILE_NAME);
			ObjectInputStream objectIn = new ObjectInputStream(fileIn);
			myUserDB = (UserDB)objectIn.readObject();
		} catch (IOException e) {
			System.out.println("IOException user deserialization is caught");
		} catch (ClassNotFoundException e){
			System.out.println("User database class not found");
		}

		//Load the auction history data
		myAuctionAuctionCalendar = null;
		try {

			FileInputStream fileIn = new FileInputStream(AUCTIONDB_FILE_NAME);
			ObjectInputStream objectIn = new ObjectInputStream(fileIn);
			myAuctionAuctionCalendar = (AuctionCalendar) objectIn.readObject();

		} catch (IOException e) {
			System.out.println("IOException Auction Data deserialization is caught");
		} catch (ClassNotFoundException e){
			System.out.println("Auction Database class not found");
		}


	}

	private static void serialize(){

		//Serialize users
		try{
			FileOutputStream fileOut = new FileOutputStream(USERDB_FILE_NAME);
			ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
			objectOut.writeObject(myUserDB);
			fileOut.close();
			objectOut.close();
		}catch (IOException e){
			System.out.println("IOException Serializing User DB");
		}

		//Serialize Auctions
		try{
			FileOutputStream fileOut = new FileOutputStream(AUCTIONDB_FILE_NAME);
			ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
			objectOut.writeObject(myAuctionAuctionCalendar);
			fileOut.close();
			objectOut.close();
		}catch (IOException e){
			System.out.println("IOException Serializing Auction DB");
		}

	}

	private static void bidderConsole(User theBidder){

		ArrayList<String> menuArray = buildBidderConsole(theBidder);
		int currentMenuLevel = 0;
		String userResponse = null;
		boolean validResponse = false;
		boolean logout = false;


		while(!logout) {
			scanner = new Scanner(System.in);

			System.out.println(menuArray.get(currentMenuLevel));

			//if(currentMenuLevel != 0)
				System.out.println("\nPlease select an option:");

			userResponse = scanner.next();

			switch(currentMenuLevel){


				case 0: //menu was top level menu
					if(userResponse.compareTo("1") == 0) { //view upcoming auctions
						myAuctionAuctionCalendar.printActiveAuctions();
						currentMenuLevel = 2; //place bid menu
						break;

					}else if(userResponse.compareTo("2") == 0){ //view my history
						currentMenuLevel = 1; //view history menu
						break;

					}else if(userResponse.compareTo("0") == 0){ //logout

						currentMenuLevel = 3;
						break;
					}

				case 1: //view menu history
					if(userResponse.compareTo("1") == 0){ //view all auctions I have placed bids on
						theBidder.printAllMyBidAuctions();
						//currentMenuLevel = ?;
						break;

					}else if(userResponse.compareTo("2") == 0){ //view all items I have bid on
						theBidder.printAllPlacedBids();
						//currentMenuLevel = ?;
						break;

					}else if(userResponse.compareTo("3") == 0){ //View all of my bids on a single auction
						theBidder.printBidsInAnAuction();
						//currentMenuLevel = ?;
						break;

					}else if(0 == userResponse.compareTo("0")){ //Return to previous menu
						currentMenuLevel = 0;
						break;

					}

				case 2: //place bid menu
					if(userResponse.compareTo("1") == 0) { //place bid
						Bidder.placeBid();
						break;

					}else if(userResponse.compareTo("0") == 0) { //Return to previous menu
						currentMenuLevel = 0;
						break;

					}

				case 3: //logout
					logout();

			}
			//todo: loop to ensure valid response



//7e No auction can be scheduled less than a set number of days from the current date, default of 14.




		}
	}

	private static void logout() {
		System.exit(0);
	}

	private static void contactConsole(User theContact){
		System.out.println("You are a Contact");
	}


	private static ArrayList<String> buildBidderConsole(User theBidder){
		ArrayList<String> responseArray = new ArrayList<String>();

		StringBuilder sb = new StringBuilder();

		//Menu 0 - Top level menu
		sb.append("\nWelcome " + theBidder.getFirstName() + " " + theBidder.getLastName() + "!\n\n" );
		sb.append("------------------------------------------\n");
		sb.append("          * Available Options *           \n");
		sb.append("------------------------------------------\n");
		sb.append("1. View Upcoming Auctions\n2. View my history\n\n0. Logout");

		responseArray.add(sb.toString());
		sb.setLength(0); //clear stringbuilder

		//Menu 1 - View My History
		sb.append("------------------------------------------\n");
		sb.append("           * History Options *            \n");
		sb.append("------------------------------------------\n");
		sb.append("1. View all auctions I have placed bids on" +
				"\n2. View all items I have bid on" +
				"\n3. View all of my bids on a single auction" +
				"\n\n0. Return to previous menu");
		responseArray.add(sb.toString());
		sb.setLength(0); //clear stringbuilder


		//Menu 2 - Place Bid Options
		sb.append("------------------------------------------\n");
		sb.append("             * Bid Options *              \n");
		sb.append("------------------------------------------\n");
		sb.append("1. Place bid\n\n0. Return");
		responseArray.add(sb.toString());
		sb.setLength(0); //clear stringbuilder


		//Menu 3 - Logout message
		sb.append("Thank you for using Auction Central!");
		sb.append("\nGoodbye, " + theBidder.getFirstName() + " " + theBidder.getLastName() + "!\n\n" );
		responseArray.add(sb.toString());
		sb.setLength(0);



		return responseArray;

	}


	//Builds sample data so deliverable 1 methods can be tested.
	private static void initialize(){

		User contactUser = new ContactPerson("Contact", "McContact", "contact@contact.com");

		User bidderUser = new Bidder("Bidder","McBidder", "bidder@bidder.com");

		myUserDB.addUser(contactUser);
		myUserDB.addUser(bidderUser);

	}
}
