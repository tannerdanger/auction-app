import jdk.nashorn.api.tree.Tree;
import storage.AuctionCalendar;
import storage.UserDB;
import users.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.util.Scanner;

import auctiondata.Scheduler;

/**
 * Things I need from other classes:
 * 1. auctionDB needs to print active auctions
 * 2. Auction class toString() needs to print the auction in the format: | <Auction ID#> -- <Auction Date> |
 * 3. Bid class toString() needs to print the item in the format: | <Item><Bid Amount> -- <Auction Date> |
 */
class ConsoleUI {
	private static AuctionCalendar myAuctionAuctionCalendar;
	private static UserDB myUserDB;
	private static final String USERDB_FILE_NAME = "users.ser";
	private static final String AUCTIONDB_FILE_NAME = "calendar.ser";
	private static TreeNode headNode;

	protected static void start() {
        Scanner scanner = new Scanner(System.in);

		myUserDB = new UserDB();
		myAuctionAuctionCalendar = new AuctionCalendar();
		//deserialize();
		initialize();


		System.out.println("Login:");
		User activeUser = myUserDB.getUser(scanner.next());
		//scanner.close();
		//if user is null (doesn't exist)
		if (null != activeUser) {
            System.out.println("Welcome " +activeUser.getFirstName() + " " + activeUser.getLastName() + "!");

			//else if user is a Bidder
			if (Bidder.class.equals(activeUser.getClass())) {
                buildBidder((Bidder)activeUser);
                runBehavior();

				//else user is a contact
			}else if(ContactPerson.class.equals(activeUser.getClass())){
			    buildContact((ContactPerson) activeUser);
			    runBehavior();
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
	private static void buildContact(ContactPerson theContact){

        TreeNode welcome_Node = new TreeNode(buildTopMessage(theContact), null);

        TreeNode auctionRequest_Node = new TreeNode("", theContact::submitAuctionRequest);

        TreeNode activeAuction_Node = new TreeNode(buildAuctionMessage(theContact), () -> theContact.getMyCurrentAuction().toString());

        TreeNode viewAllItems_Node = new TreeNode("\n0. Return", () -> theContact.getMyCurrentAuction().printInventorySheet());

        TreeNode addItem_Node = new TreeNode("0. Return", theContact::addInventoryItem);

        welcome_Node.response1 = activeAuction_Node;
        welcome_Node.response2 = auctionRequest_Node;
        activeAuction_Node.response1 = viewAllItems_Node;
        activeAuction_Node.response2 = addItem_Node;
        viewAllItems_Node.response1 = addItem_Node;

        activeAuction_Node.parent = welcome_Node;
        viewAllItems_Node.parent = welcome_Node;
        auctionRequest_Node.parent = welcome_Node;
        addItem_Node.parent = viewAllItems_Node;

        headNode = welcome_Node;

    }

    private static String buildAuctionMessage(User theUser) {

        StringBuilder sb = new StringBuilder();
        sb.append("------------------------------------------\n");
        sb.append("           * Auction Options *            \n");
        sb.append("------------------------------------------\n");

        sb.append("1. View All Items in this Auction" +
                    "\n2. Add new auction item" +
                    "\n\n0. Return to previous menu");



        return sb.toString();

    }


    private static void buildBidder(Bidder theBidder) {

		TreeNode welcome_Node = new TreeNode(buildTopMessage(theBidder), null);

		TreeNode viewAuctions_Node = new TreeNode(buildViewAuctions(), AuctionCalendar::getActiveAuctions);

		TreeNode historyOptions_Node = new TreeNode(buildHistoryMessage(theBidder), null);

		TreeNode auctionsBidOn_Node = new TreeNode("", theBidder::printAllMyBidAuctions);

		TreeNode itemsBidOn_Node = new TreeNode("\n1. View All\n2. By Specific Auction\n\n0. Return", null);

		TreeNode allItemBids_Node = new TreeNode("", theBidder::printAllPlacedBids);

        TreeNode itemsByAuction_Node = new TreeNode("", theBidder::printBidsInAnAuction);

		welcome_Node.response1 = viewAuctions_Node;
		welcome_Node.response2 = historyOptions_Node;

		historyOptions_Node.response1 = auctionsBidOn_Node;
		historyOptions_Node.response2 = itemsBidOn_Node;

		itemsBidOn_Node.response1 = allItemBids_Node;
		itemsBidOn_Node.response2 = auctionsBidOn_Node;

		welcome_Node.parent = null;
		viewAuctions_Node.parent = welcome_Node;
		historyOptions_Node.parent = welcome_Node;
		auctionsBidOn_Node.parent = historyOptions_Node;
		itemsBidOn_Node.parent = historyOptions_Node;
		allItemBids_Node.parent = itemsBidOn_Node;
		itemsByAuction_Node.parent = itemsBidOn_Node;

		headNode = welcome_Node;

	}

	private static void runBehavior() {

		TreeNode currentNode = headNode;
		Scanner scan = new Scanner(System.in);
		int response = 0;

		while (null != currentNode){


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

				if (null != currentNode.response1 && response == 1)
					currentNode = currentNode.response1;

				else if (null != currentNode.response2 && response == 2)
					currentNode = currentNode.response2;

				else if (response == 0)
					currentNode = currentNode.parent;

			}


		}

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

	private static void logout(User theUser) {
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



	private static void contactConsole(){
		System.out.println("You are a Contact");
	}



	//Builds sample data so deliverable 1 methods can be tested.
	private static void initialize(){

		User contactUser = new ContactPerson("Contact", "McContact", "contact@contact.com");

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


		protected TreeNode(String theMessage, Runnable theAction){
			consoleMessage = theMessage;
			nodeAction = theAction;
		}
	}



}
