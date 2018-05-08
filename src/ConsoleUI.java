import auctiondata.Auction;
import auctiondata.AuctionItem;
import auctiondata.Bid;
import storage.AuctionCalendar;
import storage.DataHandler;
import storage.UserDB;
import users.*;
 
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Scanner;
 
import auctiondata.Scheduler;
 
/**
 * This class creates console outputs and defines behavior for the Auction Central/
 * @author Tanner Brown
 * @version 7 May 2018
 *
 */
class ConsoleUI {
    private static AuctionCalendar myCalendar;
    private static Scheduler myScheduler;
    private static UserDB myUserDB;
    private static TreeNode headNode;
    private static DataHandler myData;
 
    /**
     * This method gathers necessary data, logs in a user
     * and starts the program based on the type of user.
     */
    protected static void start() {
        Scanner scanner = new Scanner(System.in);
        myData = new DataHandler();
        myUserDB = myData.getMyUserDB();
        myCalendar = myData.getMyAuctionCalendar();
        myScheduler = new Scheduler(myData);
 
        System.out.println("Login:");
        User activeUser = myUserDB.getUser(scanner.next());
 
        if (null != activeUser) {
            System.out.println("Welcome "
                    +activeUser.getFirstName() + " " + activeUser.getLastName() + "!");
 
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
 
    }
 
    /**
     * Method that runs while the program is running to allow
     * movement between console outputs.
     */
    private static void runBehavior() {
 
        TreeNode currentNode = headNode;
        Scanner scan = new Scanner(System.in);
        int response = 0;
 
        while (null != currentNode){
 
 
            if(null != currentNode.nodeAction){
 
 
                currentNode.nodeAction.run();
 
                System.out.println(currentNode.consoleMessage);
                response = scan.nextInt();
 
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
    /**
     * Logs out user and serializes data.
     */
    private static void logout() {
        myData.serialize();
        System.out.println("Thank you for using Auction Central!");
        System.out.println("You have sucessfully logged out. Goodbye! ");
        System.exit(0);
    }
 
    /**
     * Builds the nodes for console behavior defined for a bidder.
     * @param theBidder the client user
     */
    private static void buildBidder(Bidder theBidder) {
 
        TreeNode welcome_Node =
                new TreeNode(buildTopMessage(theBidder), null);
 
        TreeNode viewAuctions_Node =
                new TreeNode(buildViewAuctions(),
                        () -> printActiveAuctions());
 
        TreeNode historyOptions_Node =
                new TreeNode(buildHistoryMessage(theBidder), null);
 
        TreeNode auctionsBidOn_Node =
                new TreeNode("\n0. Return to previous menu",
                        () -> printAllMyBidAuctions(theBidder));
 
 
        TreeNode itemsBidOn_Node =
                new TreeNode("\n1. View All"
                        + "\n2. By Specific Auction"
                        + "\n\n0. Return to previous menu", null);
 
 
        TreeNode allItemBids_Node =
                new TreeNode("",
                        () -> printAllPlacedBids(theBidder));
 
        TreeNode itemsByAuction_Node =
                new TreeNode("\n0. Return to previous menu",
                        ()->printBidsInAnAuction(theBidder));
 
        TreeNode auctionItemsInAuction_Node =
                new TreeNode("\n0. Return to previous menu",
                        ()->promptBid(theBidder));
 
 
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
 
    /**
     * Prints all bids a bidder has placed.
     * @param theBidder the client bidder.
     */
    public static void printAllPlacedBids(Bidder theBidder) {
 
 
        for(Bid b : theBidder.getBids()){
            printBid(b);
        }
        System.out.println("0. Return to previous menu");
    }
 
    /**
     * Prints all auctions a bidder has bid in.
     * @param theBidder the client user.
     */
    public static void printAllMyBidAuctions(Bidder theBidder) {
 
        int i = 1;
        for(Bid b : theBidder.getBids()){
            System.out.println(i + ".  " + b.getAuction().toString());
        }
    }
 
    /**
     * Prints all auctions currently scheduled.
     */
    public static void printActiveAuctions() {
        System.out.println(" Active Auction  \n" +" ---------------\n ");
        for(Auction a : myCalendar.getActiveAuctions()){
            printAuction(a);
        }
    }
 
    /**
     * Builds the nodes for console behavior defined for a contact.
     * @param theContact the client representing a non-profit org.
     */
    private static void buildContact(ContactPerson theContact){
 
        TreeNode welcome_Node = new TreeNode(buildTopMessage(theContact), null);
 
        TreeNode auctionRequest_Node = new TreeNode("", ()->createAuctionRequest(theContact));
        Auction currAuc = theContact.getCurrentAuction();
 
        TreeNode activeAuction_Node = new TreeNode(buildAuctionMessage(theContact),
                ()->printAuction(theContact.getCurrentAuction()));
 
        TreeNode viewAllItems_Node =
                new TreeNode("\n0. Return to previous menu",
                        () -> theContact.getCurrentAuction().printInventorySheet());
 
        TreeNode addItem_Node =
                new TreeNode("0. Return to previous menu",
                        () -> addInventoryItem(theContact));
 
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
 
    /**
     * Helper method that prints an auction data.
     * @param theAuction the auction being printed
     */
    public static void printAuction(Auction theAuction) {
        if(null != theAuction) {
            System.out.println("| Auction ID: "
                    + String.valueOf(theAuction.getauctionID())
                    + " | ORG: " + theAuction.getOrgName()
                    + " | DATE: " + theAuction.getAuctionDate().toString()
                    + " |" + "\n");
        }
    }
 
    /**
     * Prints all bids in a single auction.
     * @param theBidder the bidder client.
     */
    public static void printBidsInAnAuction(Bidder theBidder) {
 
        Scanner scan = new Scanner(System.in);
        printBidderAuctions(theBidder);
        System.out.println("Enter Auction ID:");
        Auction auction = myCalendar.getAuction(scan.nextInt());
        for(Bid b : theBidder.getBids()){
            if(b.getAuction().getauctionID() == auction.getauctionID()) {
                printBid(b);
            }
        }
    }
 
    /**
     * Prints the data to the console for a bid.
     * @param theBid the bid being printed.
     */
    public static void printBid(Bid theBid) {
        System.out.println("| Auction ID: " + String.valueOf(theBid.getAuction().getauctionID()
                + " | ITEM: " +theBid.getItem().getName()
                + " | DATE: " +theBid.getAuction().getAuctionDate().toString()
                + " | BID: $" +theBid.getBidAmount() ));
    }
 
    /**
     * Prints all auctions a bidder has bid in.
     * @param theBidder the bidder client.
     */
    public static void printBidderAuctions(Bidder theBidder) {
        System.out.println(" Auctions bid in  \n" +" ---------------\n ");
        for(Bid b : theBidder.getBids()) {
            printBid(b);
        }
    }
 
    /**
     * Prompts the user for data to place a new bid.
     * @param theBidder the client bidder.
     */
    public static void promptBid(Bidder theBidder) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter the Auction ID of the auction you would like to view:");
        int response = scan.nextInt();
        Auction selectedAuction = myData.getMyAuctionCalendar().getAuction(response);
        while (null == selectedAuction && response != 0) {
            //TODO: Left off here, parsing to ensure auction item is correct
        }
        selectedAuction.printInventorySheet();
        System.out.println("\n1. Place Bid \n0. Return to previous menu");
        if (scan.nextInt() == 1) {
            System.out.println("Enter Item ID:");
            int itemID = scan.nextInt();
            AuctionItem bidItem = selectedAuction.getItem(itemID);
            System.out.println("Bid Amount for "
            + bidItem.getName() + " (min bet is $"
                    + bidItem.getMinPrice() + ") :");
            double bidAmount = scan.nextDouble();
            System.out.println("\nConfirm place a $"
            + bidAmount + " bid on " + bidItem.getName() + "? (y/n)");
           
            if (scan.next().contains("y") || scan.next().contains("Y")) {
                boolean result =
                        theBidder.placeBid(selectedAuction,
                                BigDecimal.valueOf(bidAmount), bidItem);
               
                if(result) {
                    System.out.println("Bid placed successfully.");
                } else {
                    System.out.println("Bid failed.");
                }
            }
        }
    }
 
    /**
     * Prompts a contact for data to add a new inventory item to an auction.
     * @param theContact a client contact.
     */
    public static void addInventoryItem(ContactPerson theContact) {
        Scanner theScanner = new Scanner(System.in);
        System.out.println("Please enter your new item's name: ");
 
        String name = theScanner.nextLine();
 
        System.out.println("Please enter the minimum bid for " + name + " (Can be 0 for no minimum)");
        Double minBid = theScanner.nextDouble();
        boolean result = theContact.addInventoryItem(name, new BigDecimal(minBid));
        if(result) {
            System.out.println("Item added successfully");
        } else {
            System.out.println("Item add failed.");
        }
        //      theScanner.close();
    }
 
    /**
     * Creates a request for a new auction and prompts user for relevant data.
     * @param theContact the client contact.
     */
    public static void createAuctionRequest(ContactPerson theContact) {
        Scanner Scan = new Scanner(System.in);
 
        System.out.println("When do you plan to host your auction?");
        System.out.println("Please enter Date in the format MM/DD/YYYY:");
        String scannedLine = Scan.nextLine();
 
        Scanner lineScan = new Scanner(scannedLine);
        lineScan.useDelimiter("/");
 
        int theMonth = lineScan.nextInt();
        int theDay = lineScan.nextInt();
        int theYear = lineScan.nextInt();
 
        System.out.println("Validating your auction inventory sheet...");
        LocalDateTime newDate = LocalDateTime.of(theYear, theMonth, theDay, 0,0);
 
        Boolean sched =
                myScheduler.isAuctionRequestValid(
                        theContact.getCurrentAuction(), newDate);
       
        if (sched != null && sched) {
            System.out.println("Auction Inventory Sheet confirmed.");
            System.out.println("Your Auction is booked on " + newDate.toString());
            //  Auction newAuction = new Auction();
            //  newAuction.setAuctionDate(newDate);
            Auction a = theContact.createNewAuction(newDate);
            myCalendar.addAuction(a);
 
 
            //System.out.println("Here is your inventory sheet: ");
        } else {
            System.out.println("Auction cannot be submitted.");
        }
 
 
        //      Scan.close();
        //      lineScan.close();
    }
 
    /**
     * Builds the string for the top level console UI.
     * @param theUser the current user.
     * @return the string data.
     */
    private static String buildTopMessage(User theUser) {
 
        StringBuilder sb = new StringBuilder();
 
        //Menu 0 - Top level menu
        sb.append("------------------------------------------\n");
        sb.append("          * Available Options *           \n");
        sb.append("------------------------------------------\n");
        if (Bidder.class.equals(theUser.getClass())) {
            sb.append("1. View Upcoming Auctions\n2. View my history\n\n0. Logout");
        } else if (ContactPerson.class.equals(theUser.getClass())) {
            sb.append("1. View My Active Auction\n"
                    + "2. Submit New Auction Request\n\n"
                    + "0. Logout");
        }
 
        return sb.toString();
 
    }
    /**
     * Builds the string for the console for auction options.
     * @return the string data.
     */
    private static String buildViewAuctions() {
        StringBuilder sb = new StringBuilder();
        sb.append("------------------------------------------\n");
        sb.append("         * Auctions Options *          \n");
        sb.append("------------------------------------------\n");
        sb.append("\n1. View Items in an Auction / Place a bid");
        sb.append("\n\n0. Return to previous menu");
 
        return sb.toString();
    }
    /**
     * Builds the string for the console for auction history options.
     * @param theUser the client user.
     * @return the string data.
     */
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
    /**
     * Builds the string for the console for auction options.
     * @param theContact a client contact.
     * @return the string data.
     */
    private static String buildAuctionMessage(ContactPerson theContact) {
 
        StringBuilder sb = new StringBuilder();
        sb.append("------------------------------------------\n");
        sb.append("           * Auction Options *            \n");
        sb.append("------------------------------------------\n");
        if(null == theContact.getCurrentAuction()) {
            sb.append("You do not have an active Auction! "
                    + "Return to main menu to request a new auction."
                    + "\n0.Return to previous menu");
        } else {
            sb.append("1. View All Items in this Auction" +
                    "\n2. Add new auction item" +
                    "\n\n0. Return to previous menu");
        }
 
        return sb.toString();
    }
 
    /**
     * An inner class that is a datastructure that holds all of the
     * console UI data and behavior.
     *
     * @author Tanner Brown
     * @version 4 may 2018
     *
     */
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