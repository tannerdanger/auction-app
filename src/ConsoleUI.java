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

import auctiondata.Scheduler;

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
	private User activeUser;
	private static TreeNode headNode;
	private static Scheduler myScheduler;

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

	private static void bidderConsole(User theUser){

		//ConsoleTree tree = new ConsoleTree(theBidder);
		buildBidder(theUser);
		runBehavior();


	}

	private static void buildBidder(User theUser) {

		TreeNode welcome_Node = new TreeNode(buildTopMessage(theUser), null);

		TreeNode viewAuctions_Node = new TreeNode(buildViewAuctions(), AuctionCalendar::getActiveAuctions);

		TreeNode historyOptions_Node = new TreeNode(buildHistoryMessage(theUser), null);

		TreeNode auctionsBidOn_Node = new TreeNode("", theUser::printAllMyBidAuctions);

		TreeNode itemsBidOn_Node = new TreeNode("\n1. View All\n2. By Specific Auction\n\n0. Return", ()->printItemsBid(theUser));

		welcome_Node.response1 = viewAuctions_Node;
		welcome_Node.response2 = historyOptions_Node;

		historyOptions_Node.response1 = auctionsBidOn_Node;
		historyOptions_Node.response2 = itemsBidOn_Node;

		welcome_Node.parent = null;
		viewAuctions_Node.parent = welcome_Node;
		historyOptions_Node.parent = welcome_Node;
		auctionsBidOn_Node.parent = historyOptions_Node;
		itemsBidOn_Node.parent = historyOptions_Node;

		headNode = welcome_Node;

	}

	private static void runBehavior() {
		boolean loop = true;
		TreeNode currentNode = headNode;
		Scanner scan = new Scanner(System.in);
		int response = 0;
		while (loop){
			System.out.println(currentNode);
			if(null != currentNode.nodeAction){

				currentNode.nodeAction.run();

				System.out.println(currentNode.consoleMessage);
				response = Integer.parseInt(scan.next());

				if(response == 1)
					currentNode = currentNode.response1;
				else if(response == 2)
					currentNode = currentNode.response2;
				else if(response == 0)
					currentNode = currentNode.parent;

			} else {

				System.out.println(currentNode.consoleMessage);
				response = Integer.parseInt(scan.next());

				if (response == 1)
					currentNode = currentNode.response1;
				else if (response == 2)
					currentNode = currentNode.response2;
				else if (response == 0)
					currentNode = currentNode.parent;

			}


		}

	}

	private static void printItemsBid(User theUser) {

		//Scanner scan = new Scanner(System.in);
		//int userResponse = Integer.parseInt(scan.next());
		int userResponse = 1;

		if (userResponse == 1){
			theUser.printAllPlacedBids();
		}else if(userResponse == 2){
			theUser.printBidsInAnAuction();
		}
	}

	private void viewMyAuctionsWithBids() {
		AuctionCalendar.getActiveAuctions();
		System.out.println("1. Place Bid \n");
		System.out.println("0. Return");
	}

	private static String buildViewAuctions() {
		StringBuilder sb = new StringBuilder();
		sb.append("------------------------------------------\n");
		sb.append("         * Available  Auctions *          \n");
		sb.append("------------------------------------------\n");

		return sb.toString();
	}

	private static String buildHistoryMessage(User theUser) {
		StringBuilder sb = new StringBuilder();
		sb.append("------------------------------------------\n");
		sb.append("           * History Options *            \n");
		sb.append("------------------------------------------\n");
		if (Bidder.class.equals(theUser.getClass())) {
			sb.append("1. View all auctions I have placed bids on" +
					"\n2. View items I have bid on" +
					"\n\n0. Return to previous menu");
		}


		return sb.toString();
	}

	private void logout(User theUser) {
		System.out.println("Thank you for using Auction Central!");
		System.out.println("\nGoodbye, " + theUser.getFirstName() + " " + theUser.getLastName() + "!\n\n" );
		System.exit(0);
	}

	private static String buildTopMessage(User theUser) {

		StringBuilder sb = new StringBuilder();

		//Menu 0 - Top level menu
		sb.append("------------------------------------------\n");
		sb.append("          * Available Options *           \n");
		sb.append("------------------------------------------\n");
		if (Bidder.class.equals(theUser.getClass())) {
			sb.append("1. View Upcoming Auctions\n2. View my history\n\n0. Logout");
		} else if (ContactPerson.class.equals(theUser.getClass())) {
			sb.append("1. View My Active Auction\n2. Submit New Auction Request\n\n0. Logout");
		}

		return sb.toString();

	}

	private static void logout() {
		System.exit(0);
	}

	private static void contactConsole(User theContact){
		System.out.println("You are a Contact");
	}



	//Builds sample data so deliverable 1 methods can be tested.
	private static void initialize(){

		User contactUser = new ContactPerson("Contact", "McContact", "contact@contact.com", myScheduler);

		User bidderUser = new Bidder("Bidder","McBidder", "bidder@bidder.com");

		myUserDB.addUser(contactUser);
		myUserDB.addUser(bidderUser);

	}

	private static class TreeNode {

		/**
		 * The message associated with this string
		 */
		private String consoleMessage;

		private Runnable nodeAction;


		private TreeNode response1;
		private TreeNode response2;

		private TreeNode parent;


		public TreeNode(String theMessage, Runnable theAction){
			consoleMessage = theMessage;
		}



	}



}
