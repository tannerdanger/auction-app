import storage.Calendar;
import storage.UserDB;
import users.*;

import java.io.*;
import java.util.Scanner;


public class ConsoleUI {
	private static Scanner scanner;
	private static Calendar myAuctionCalendar;
	private static UserDB myUserDB;
	private static final String USERDB_FILE_NAME = "users.ser";
	private static final String AUCTIONDB_FILE_NAME = "calendar.ser";

	public static void start() {
		scanner = new Scanner(System.in);

		myUserDB = new UserDB();
		myAuctionCalendar = new Calendar();
		deserialize();
		//initialize(); //do this if data is empty?


		System.out.println("Login:"); //prompt user to reenter email address, or register new account?
		User currentUser = myUserDB.getUser(scanner.next());//user enters email, system checks userDB for user

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
		serialize();
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
		myAuctionCalendar = null;
		try {

			FileInputStream fileIn = new FileInputStream(AUCTIONDB_FILE_NAME);
			ObjectInputStream objectIn = new ObjectInputStream(fileIn);
			myAuctionCalendar = (Calendar) objectIn.readObject();

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
			objectOut.writeObject(myAuctionCalendar);
			fileOut.close();
			objectOut.close();
		}catch (IOException e){
			System.out.println("IOException Serializing Auction DB");
		}

	}

	private static void bidderConsole(User theBidder){
		System.out.println("Welcome " +theBidder.getFirstName() +" "+theBidder.getLastName()+"!");
		System.out.println("------------------------------------------");
		System.out.println("          * Available Options *           ");
		System.out.println("------------------------------------------");
		System.out.println("1. View Upcoming Auctions\n2. View my history\n3. Place a bid\n\n0. Logout");
	}

	private static void contactConsole(User theContact){
		System.out.println("You are a Contact");
	}

	//Builds sample data so deliverable 1 methods can be tested.
	private static void initialize(){

		User contactUser = new ContactPerson("Contact", "McContact", "contact@contact.com");

		User bidderUser = new Bidder("Bidder","McBidder", "bidder@bidder.com");

		myUserDB.addUser(contactUser);
		myUserDB.addUser(bidderUser);

	}
}
