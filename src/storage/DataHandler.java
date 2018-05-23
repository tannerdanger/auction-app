package storage;

import auctiondata.*;
import users.*;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * A class that allows the program to handle data through serialization and creating
 * sample data for testing as necessary.
 * @author Tanner Brown
 * @version 6 May 2018
 *
 */
public class DataHandler extends Observable{
    private static final String USERDB_FILE_NAME = "users.ser";
    private static final String AUCTIONDB_FILE_NAME = "calendar.ser";


    AuctionCalendar myAuctionCalendar;
    UserDB myUserDB;

    /**
     * Constructs data by serializing or initializing new data as necessary.
     */
    public DataHandler(){
        if(new File(USERDB_FILE_NAME).exists()
                && new File(AUCTIONDB_FILE_NAME).exists())
            deserialize();
            //Else, initialize with sample data.
        else
            initializeData();

        createAdditionalSampleData();
        //createObservers();
    }


    // ------------------- DATA CREATING METHODS --------------- //

    /**
     * A method for developers to add sample data to be serialized as necessary.
     */
    private void createAdditionalSampleData() {



    }

    /**
     * Deserializes userDB and auctionCalendar data.
     */
    public void deserialize(){
        Boolean badData = false;
        try {
            FileInputStream fileIn = new FileInputStream(USERDB_FILE_NAME);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            myUserDB = (UserDB)objectIn.readObject();
            objectIn.close();
        } catch (IOException e) {
            System.out.println("IOException user "
                    + "deserialization is caught. Initializing Sample Data.");
            initializeData();
            badData = true;
        } catch (ClassNotFoundException e){
            System.out.println("User database "
                    + "class not found. Initializing Sample Data.");
            initializeData();
            badData = true;
        }
        if(!badData) {
            try {
                FileInputStream fileIn = new FileInputStream(AUCTIONDB_FILE_NAME);
                ObjectInputStream objectIn = new ObjectInputStream(fileIn);
                myAuctionCalendar = (AuctionCalendar) objectIn.readObject();
                objectIn.close();
            } catch (IOException e) {
                System.out.println("IOException Auction Data "
                        + "deserialization is caught. Using Sample Data");

            } catch (ClassNotFoundException e) {
                System.out.println("Auction Database class "
                        + "not found. Using Sample Data");

            }
        }
    }

    /**
     * Initializes new data in case serialization fails.
     */
    public void initializeData(){

        myUserDB = new UserDB();
        myAuctionCalendar = new AuctionCalendar();

        //create users
        User contactUser =
                new ContactPerson("Contact", "McContact", "contact@contact.com");
        ((ContactPerson) contactUser).setMyOrgName("Pat's Pneumonic Penguin Preservation");

        ContactPerson contactUser3 = new ContactPerson("Lars", "Rush", "contact3@contact.com");
        contactUser3.setMyOrgName("Odio Corp.");

        ContactPerson contactUser4 = new ContactPerson("Russ", "Walker", "Contact4@contact.com");
        contactUser4.setMyOrgName("Risus Industries");


        User bidderUser =
                new Bidder("Bidder","McBidder", "bidder@bidder.com");

        //create auctions
        Auction auction1 =
                new Auction(((ContactPerson) contactUser).getMyOrgName(),
                        ((ContactPerson) contactUser).getMyOrgID(),
                        LocalDateTime.of(2018, 05, 30, 10, 00),
                        null);

        Auction auction2 = new Auction("#SaveTheDoDo", "#SaveTheDoDo".hashCode(),
                LocalDateTime.of(2018, 05, 30, 15, 00),
                null);

        Auction auction3 = new Auction(contactUser3.getMyOrgName(), contactUser3.getMyOrgName().hashCode(), LocalDateTime.of(2017, 04, 1, 11, 00), null);
        Auction auction4 = new Auction(contactUser3.getMyOrgName(), contactUser3.getMyOrgName().hashCode(), LocalDateTime.of(2018, 06, 20, 11, 00), null);
        Auction auction5 = new Auction(contactUser3.getMyOrgName(), contactUser3.getMyOrgName().hashCode(), LocalDateTime.of(2016, 03, 2, 11, 00), null);
        Auction auction6 = new Auction(contactUser3.getMyOrgName(), contactUser3.getMyOrgName().hashCode(), LocalDateTime.of(2015, 2, 17, 11, 00), null);
        Auction auction7 = new Auction(contactUser4.getMyOrgName(), contactUser4.getMyOrgName().hashCode(), LocalDateTime.of(2018, 2, 15, 11, 00), null);
        Auction auction8 = new Auction(contactUser4.getMyOrgName(), contactUser4.getMyOrgName().hashCode(), LocalDateTime.of(2017 , 2, 10, 11, 00), null);
        Auction auction9 = new Auction(contactUser4.getMyOrgName(), contactUser4.getMyOrgName().hashCode(), LocalDateTime.of(2016, 1, 25, 11, 00), null);
        Auction auction10 = new Auction(contactUser4.getMyOrgName(), contactUser4.getMyOrgName().hashCode(), LocalDateTime.of(2014 , 8, 10, 11, 00), null);
        Auction auction11 = new Auction(contactUser4.getMyOrgName(), contactUser4.getMyOrgName().hashCode(), LocalDateTime.of(2013 , 4, 21, 11, 00), null);

        AuctionItem item1 = new AuctionItem(20.00, "Penguin Pre-Breathers");
        AuctionItem item2 = new AuctionItem(50.00, "I'm out of clever names");
        auction4.addInventoryItem(new AuctionItem(62.00, "Veal - Chops, Split, Frenched"));
        auction4.addInventoryItem(new AuctionItem(	71.00, "Lettuce - Escarole"));
        auction4.addInventoryItem(new AuctionItem(46.00, "Scampi Tail"));
        auction4.addInventoryItem(new AuctionItem(76.00, "Tumeric"));
        auction4.addInventoryItem(new AuctionItem(88.00, "Creamers - 10%"));
        auction4.addInventoryItem(new AuctionItem(22.00, "Oil - Shortening - All - Purpose"));

        auction7.addInventoryItem(new AuctionItem(22.00, "Tumeric"));
        auction7.addInventoryItem(new AuctionItem(35.00, "Nantucket - Orange Mango Cktl"));
        auction7.addInventoryItem(new AuctionItem(45.00, "Spinach - Spinach Leaf"));
        auction7.addInventoryItem(new AuctionItem(85.00, "Truffle Cups - White Paper"));
        auction7.addInventoryItem(new AuctionItem(26.00, "Milk - 1%"));
        auction7.addInventoryItem(new AuctionItem(30.00, "Sultanas"));


        auction1.addInventoryItem(item1);
        auction1.addInventoryItem(item2);

        ((ContactPerson) contactUser).setMyCurrentAuction(auction1);
        myAuctionCalendar.addAuction(auction1);
        myAuctionCalendar.addAuction(auction2);
        myAuctionCalendar.addAuction(auction3);
        myAuctionCalendar.addAuction(auction4);
        myAuctionCalendar.addAuction(auction5);
        myAuctionCalendar.addAuction(auction6);
        myAuctionCalendar.addAuction(auction7);
        myAuctionCalendar.addAuction(auction8);
        myAuctionCalendar.addAuction(auction9);
        myAuctionCalendar.addAuction(auction10);
        myAuctionCalendar.addAuction(auction11);

        myUserDB.addUser(bidderUser);
        myUserDB.addUser(contactUser);
    }

    /**
     * Serializes the data to be used in the future.
     */
    public void serialize(){

//Delete old files
        //new File(USERDB_FILE_NAME).delete();
        //new File(AUCTIONDB_FILE_NAME).delete();
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
    /**
     * Getter for an auction Calendar
     * @return the auctionCalendar
     */
    private AuctionCalendar getMyAuctionCalendar() {
        return myAuctionCalendar;
    }
    /**
     * Getter for a user database.
     * @return the userDB.
     */
    private UserDB getMyUserDB() {
        return myUserDB;
    }

    // ------------------- DATA HANDLING METHODS --------------- //

    //~~User Data Handling~~//
    public Boolean userExists(String email){
        return myUserDB.userDirectory.containsKey(email);
    }

    /**
     * Returns a user associated with an email address.
     * @param theEmail - an email address key that is associated with a user.
     * @return the user associated with the email address.
     */
    public User getUser(String theEmail){
        return myUserDB.getUser(theEmail);
    }


    //~~Auctions Data Handling~~//

    /**
     * Returns a sorted list of all auctions by organization, sorted chronologically
     * from newest to oldest.
     * @return an array of auctions associated with an organization.
     */
    public ArrayList<Auction> getAuctionsByOrg(String theOrg){
        ArrayList<Auction> tmpList = myAuctionCalendar.getAuctionDataBase();
        ArrayList<Auction> returnList = new ArrayList<>();

        for(Auction a : tmpList){
            if(a.getOrgName().compareTo(theOrg) == 0){
                returnList.add(a);
            }
        }
        sortAuctions(returnList);

        return returnList;
    }

    /**
     * Sorts an array list of auctions chronologically.
     * @param theAuctions the arraylist of auctions to be sorted.
     */
    public void sortAuctions(ArrayList<Auction> theAuctions){
        theAuctions.sort((a1, a2)->{
            if(a1.getAuctionDate().isBefore(a2.getAuctionDate())){
                return 1;
            }else{
                return -1;
            }
        });
    }

    /**
     * Returns an auction by its auction ID
     * @param auctionID the ID of the auction being requested
     * @return an auction associated with the auctionID
     */
    public Auction getAuctionByID(int auctionID){

        return myAuctionCalendar.auctionDataBase.get(auctionID);
    }

    /**
     * Creates an array list of auctions that have been scheduled that are "active" - meaning
     * they haven't passed yet and can be actively bid on.
     * @return an arraylist of auctions.
     */
    public ArrayList<Auction> getActiveAuctions(){
        ArrayList<Auction> tmpList = myAuctionCalendar.getAuctionDataBase();
        ArrayList<Auction> returnList = new ArrayList<>();

        for(Auction a : tmpList){
            if(a.getAuctionDate().isAfter(LocalDate.now()))
                returnList.add(a);
        }
        returnList.sort((a1, a2) -> {
            if(a1.getAuctionDate().isBefore(a2.getAuctionDate())){
                return 1;
            }else{
                return -1;
            }
        });
        return returnList;
    }

    //~~Auction Item Data Handling ~~//

    public Map<Integer, AuctionItem> getAuctionItemsByAuction(Auction theAuction){
        return theAuction.getInventorySheet();
    }

    /**
     * Returns an auction item by its item ID.
     * @param theItemID the id of the auction item
     * @return an auction item.
     */
    public AuctionItem getAuctionItem(int theItemID){
        ArrayList<Auction> activeAuctions = new ArrayList<Auction>();
        for(Auction a: myAuctionCalendar.auctionDataBase) {
            if(a.getInventorySheet().containsKey(theItemID))
                return a.getItem(theItemID);
        }
        return null; //returns null if no item is ever found.
    }

    /**
     * Overloaded method for retrieving an item if the auction and id are available.
     * @param theItemID the auction item ID
     * @param theAuction the auction the auction item belongs to
     * @return the auction item
     */
    public AuctionItem getAuctionItem(int theItemID, Auction theAuction){
        return theAuction.getItem(theItemID);
    }

    /**
     * Adds an auction to the calendar, then notifies observers.
     * @param theAuction
     */
    public void addAuction(Auction theAuction){
        myAuctionCalendar.auctionDataBase.add(theAuction);
        sortAuctions(myAuctionCalendar.auctionDataBase);
        notifyUpdateAuctions(theAuction);
    }

    /**
     * Adds an auction item to an auction and notifies observers.
     * @param theAuction an item is being added to.
     * @param theItem an item being added to the auction.
     */
    public void addAuctionItem(Auction theAuction, AuctionItem theItem){
        theAuction.addInventoryItem(theItem);
        notifyUpdateItem(theItem);
    }

    //~~Bids Data Handling~~//

    /**
     * Adds a new bid to an auction item and notifies observers.
     * @param theBid being added to an item.
     * @param theItemID the unique id of the auction item being bid on.
     */
    public void placeBid(Bid theBid, int theItemID){
        getAuctionItem(theItemID).addSealedBid(theBid);
        notifyUpdateBids(theBid);
    }

    /**
     * Returns a list of bids associated with a bidder.
     * Note: This method may be redundant, but is included in the datahandler to try to consolidate data handling.
     * @param theBidder
     * @return
     */
    public List<Bid> getBids(Bidder theBidder){
        return theBidder.getBids();
    }


    //~~Observer Notifying~~//

    private void notifyUpdateBids(Bid theBid) {
        notifyObservers(theBid);
    }

    private void notifyUpdateAuctions(Auction theAuction){
        notifyObservers(theAuction);
    }

    private void notifyUpdateItem(AuctionItem theItem){
        notifyObservers(theItem);
    }

}