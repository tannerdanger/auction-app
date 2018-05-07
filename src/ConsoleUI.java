import auctiondata.Auction;
import auctiondata.AuctionItem;
import auctiondata.Bid;
import storage.AuctionCalendar;
import storage.DataHandler;
import storage.UserDB;
import users.*;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Scanner;

import auctiondata.Scheduler;

import javax.xml.crypto.Data;

/**
 * Things I need from other classes:
 * 1. auctionDB needs to print active auctions
 * 2. Auction class toString() needs to print the auction in the format: | <Auction ID#> -- <Auction Date> |
 * 3. Bid class toString() needs to print the item in the format: | <Item><Bid Amount> -- <Auction Date> |
 */
class ConsoleUI {
	private static AuctionCalendar myCalendar;
	private static Scheduler myScheduler;
	private static UserDB myUserDB;
	private static final String USERDB_FILE_NAME = "users.ser";
	private static final String AUCTIONDB_FILE_NAME = "calendar.ser";
	private static TreeNode headNode;
	private static DataHandler myData;

	protected static void start() {
        Scanner scanner = new Scanner(System.in);
        myData = new DataHandler();
        myUserDB = myData.getMyUserDB();
        myCalendar = myData.getMyAuctionCalendar();
        myScheduler = new Scheduler(myData);

		System.out.println("Login:");
		User activeUser = myUserDB.getUser(scanner.next());

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

				else if (response == 0) {
				    if(null == currentNode.parent)
				        logout();
                    currentNode = currentNode.parent;
                }

			}


		}

	}
	private static void logout() {
	    myData.serialize();
		System.out.println("Thank you for using Auction Central!");
		System.out.println("You have sucessfully logged out. Goodbye! ");
		System.exit(0);
	}

	//build nodes for console
    private static void buildBidder(Bidder theBidder) {

        TreeNode welcome_Node = new TreeNode(buildTopMessage(theBidder), null);

        TreeNode viewAuctions_Node = new TreeNode(buildViewAuctions(), () -> printActiveAuctions());

        TreeNode historyOptions_Node = new TreeNode(buildHistoryMessage(theBidder), null);

        TreeNode auctionsBidOn_Node = new TreeNode("0. Return", () -> printAllMyBidAuctions(theBidder));

        TreeNode itemsBidOn_Node = new TreeNode("\n1. View All\n2. By Specific Auction\n\n0. Return", null);

        TreeNode allItemBids_Node = new TreeNode("", () -> printAllPlacedBids(theBidder));

        TreeNode itemsByAuction_Node = new TreeNode("", ()->printBidsInAnAuction(theBidder));

        TreeNode auctionItemsInAuction_Node = new TreeNode("0. Return", ()->promptBid(theBidder));


        welcome_Node.response1 = viewAuctions_Node;
        welcome_Node.response2 = historyOptions_Node;

        historyOptions_Node.response1 = auctionsBidOn_Node;
        historyOptions_Node.response2 = itemsBidOn_Node;

        itemsBidOn_Node.response1 = allItemBids_Node;
        itemsBidOn_Node.response2 = itemsByAuction_Node;

        viewAuctions_Node.response1 = auctionItemsInAuction_Node;
        //auctionItemsInAuction_Node.response1 = placeBid_Node;
        //placeBid_Node.parent = welcome_Node;

        welcome_Node.parent = null;
        viewAuctions_Node.parent = welcome_Node;
        historyOptions_Node.parent = welcome_Node;
        auctionsBidOn_Node.parent = historyOptions_Node;
        itemsBidOn_Node.parent = historyOptions_Node;
        allItemBids_Node.parent = itemsBidOn_Node;
        itemsByAuction_Node.parent = itemsBidOn_Node;
        auctionItemsInAuction_Node.parent = welcome_Node;

        headNode = welcome_Node;

    }
    
    //View all items I have bid on
    public static void printAllPlacedBids(Bidder theBidder) {


        //Todo: Print out placed bids (all items). Return response covered in console logic
        int i = 1;
        for(Bid b : theBidder.getBids()){
            System.out.println(i +". " + b.toString());
        }
        System.out.println("0. Return");
    }

    //View all auctions I have placed bids on
    public static void printAllMyBidAuctions(Bidder theBidder) {

        //Todo: Print out all auctions that have been bid in (all auctions I have placed bids in).

        int i = 1;
        for(Bid b : theBidder.getBids()){
            System.out.println(i + ".  " + b.getAuction().toString());
        }
    }
    
    public static void printActiveAuctions() {
    	System.out.println(" Active Auction  \n" +" ---------------\n ");
    	for(Auction a : myCalendar.getActiveAuctions()){
			System.out.println("| Auction ID: " + String.valueOf(a.getauctionID()) + " | ORG: " 
								+ a.getOrgName() + " | DATE: " + a.getAuctionDate().toString() 
								+ " |" + "\n");
		}
    }

	private static void buildContact(ContactPerson theContact){

        TreeNode welcome_Node = new TreeNode(buildTopMessage(theContact), null);

        TreeNode auctionRequest_Node = new TreeNode("", ()->theContact.submitAuctionRequest(myData));
        Auction currAuc = theContact.getMyCurrentAuction();

        TreeNode activeAuction_Node = new TreeNode(buildAuctionMessage(theContact),
                ()->printCurrentAuction(theContact));
        
        TreeNode viewAllItems_Node = new TreeNode("\n0. Return", () -> theContact.getMyCurrentAuction().printInventorySheet());

        TreeNode addItem_Node = new TreeNode("0. Return", () -> addInventoryItem(theContact));

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
	
	public static void printCurrentAuction(ContactPerson theContact) {
		System.out.println("| Auction ID: " + String.valueOf(theContact.getMyCurrentAuction().getauctionID()) + " | ORG: " 
				+ theContact.getMyOrgName() + " | DATE: " + theContact.getMyCurrentAuction().getAuctionDate().toString()
				+ " |" + "\n");
	}
	
    //view all of my bids on a single auction
    public static void printBidsInAnAuction(Bidder theBidder) {
        //prompt user for which auction they want to view bids on
        //display all items for that auction
        //TODO: Prompt user for which auction, and then call getAuction from the calendar with that auction ID
        Scanner scan = new Scanner(System.in);
        printBidderAuctions(theBidder);
        System.out.println("Enter Auction ID:");
        Auction auction = myCalendar.getAuction(scan.nextInt());
        for(Bid b : theBidder.getBids()){
            if(b.getAuction().getauctionID() == auction.getauctionID()){
                System.out.println(b.toString());
            }
        }
    }
    
    public static void printBidderAuctions(Bidder theBidder) {
    	System.out.println(" Auctions bid in  \n" +" ---------------\n ");
    	for(Bid b : theBidder.getBids()) {
    		System.out.println("| Auction ID: " + String.valueOf(b.getAuction().getauctionID()) + " | ORG: " 
    				+ b.getAuction().getOrgName() + " | DATE: " + b.getAuction().getAuctionDate().toString()
    				+ " |" + "\n");
    	}
    }
	
	public static void promptBid(Bidder theBidder) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Which auction would you like to view?");
        int response = scan.nextInt();
        Auction selectedAuction = myData.getMyAuctionCalendar().getAuction(response);
        while (null == selectedAuction && response != 0) {
            //TODO: Left off here, parsing to ensure auction item is correct
        }
        selectedAuction.printInventorySheet();
        System.out.println("\n1. Place Bid \n0. Return");
        if (scan.nextInt() == 1) {
            System.out.println("Enter Item ID:");
            int itemID = scan.nextInt(); //TODO: Check for correct input
            AuctionItem bidItem = selectedAuction.getItem(itemID);
            System.out.println("Bid Amount for " + bidItem.getName() + " (min bet is $" + bidItem.getMinPrice() + ") :");
            double bidAmount = scan.nextDouble(); //TODO: Check for correct input
            System.out.println("\nConfirm place a $" + bidAmount + " bid on " + bidItem.getName() + "? (y/n)");
            if (scan.next().contains("y") || scan.next().contains("Y")) {
                boolean result = theBidder.placeBid(selectedAuction, BigDecimal.valueOf(bidAmount), bidItem);
                if(result) {
                	System.out.println("Bid placed successfully.");
                } else {
                	System.out.println("Bid failed.");
                }
            }
        }
    }
	
	public static void addInventoryItem(ContactPerson theContact) {
		Scanner theScanner = new Scanner(System.in);
		System.out.println("Please enter your new item's name: ");

		String name = theScanner.nextLine();

		System.out.println("Please enter the minimum bid for " + name + " (Can be 0 for no minimum)");
		Double minBid = theScanner.nextDouble();
		boolean result = theContact.addInventoryItem(name, new BigDecimal(minBid));
	}
	
	public static void createAuctionRequest(ContactPerson theContact) {
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
		LocalDateTime newDate = LocalDateTime.of(theYear, theMonth, theDay, 0,0);

		if (myScheduler.isAuctionRequestValid(theContact.getPriorAuction(), theContact.getMyCurrentAuction(), newDate)) {
			System.out.println("Auction Inventory Sheet confirmed.");
			System.out.println("Your Auction is booked on " + newDate.toString());
		//	Auction newAuction = new Auction();
		//	newAuction.setAuctionDate(newDate);
			Auction a = theContact.createNewAuction(newDate);
            myCalendar.addAuction(a);

			//TODO ITEM INVENTORY SHEET PRINTOUT
			System.out.println("Here is your inventory sheet: ");
		}


		Scan.close();
		lineScan.close();
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
    private static String buildViewAuctions() {
        StringBuilder sb = new StringBuilder();
        sb.append("------------------------------------------\n");
        sb.append("         * Auctions Options *          \n");
        sb.append("------------------------------------------\n");
        sb.append("\n1. View Items in an Auction / Place a bid");
        sb.append("\n\n0. Return");

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


    //Builds sample data so deliverable 1 methods can be tested.
//	private static void initialize(){
//	    myUserDB = new UserDB();
//	    myAuctionAuctionCalendar = new AuctionCalendar();
//		//create users
//		User contactUser = new ContactPerson("Contact", "McContact", "contact@contact.com");
//		((ContactPerson) contactUser).setMyOrgName("Pat's Pneumonic Penguin Preservation");
//		User bidderUser = new Bidder("Bidder","McBidder", "bidder@bidder.com");
//
//		//create auctions
//        Auction auction1 = new Auction(((ContactPerson) contactUser).getMyOrgName(),
//                ((ContactPerson) contactUser).getMyOrgID(),
//                LocalDateTime.of(2018, 05, 30, 10, 00),
//				null);
//
//        Auction auction2 = new Auction("#SaveTheDoDo", "#SaveTheDoDo".hashCode(),
//                LocalDateTime.of(2018, 05, 30, 15, 00),
//                null);
//
//        AuctionItem item1 = new AuctionItem(20.00, "Penguin Pre-Breathers");
//        AuctionItem item2 = new AuctionItem(50.00, "I'm out of clever names");
//        auction1.addInventoryItem(item1);
//        auction1.addInventoryItem(item2);
//
//        ((ContactPerson) contactUser).setMyCurrentAuction(auction1);
//        myAuctionAuctionCalendar.addAuction(auction1);
//        myAuctionAuctionCalendar.addAuction(auction2);
//
//	}
    //Nodes for console behavior
	private static class TreeNode {

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
