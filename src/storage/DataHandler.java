package storage;

import auctiondata.*;
import users.*;

import java.io.*;
import java.math.BigDecimal;
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

        if(null == myAuctionCalendar
        || null == myAuctionCalendar.auctionDB){
            initializeData();
        }
        createAdditionalSampleData();
        myAuctionCalendar.updateCalendar();

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
            
            // ADDED BY CHARLIE
            fileIn = new FileInputStream(AUCTIONDB_FILE_NAME);
            objectIn = new ObjectInputStream(fileIn);
            myAuctionCalendar = (AuctionCalendar)objectIn.readObject();
            objectIn.close();
            // END ADDED BY CHARLIE
            
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
        ContactPerson contactUser =
                new ContactPerson("Contact", "McContact", "contact@contact.com");
        contactUser.setMyOrgName("Pat's Pneumonic Penguin Preservation");

        ContactPerson contactUser2 = new ContactPerson("Lars", "Rush", "contact2@contact.com");
        contactUser2.setMyOrgName("Odio Corp.");

        ContactPerson contactUser3 = new ContactPerson("Russ", "Walker", "Contact3@contact.com");
        contactUser3.setMyOrgName("Risus Industries");


        Bidder bidderUser =
                new Bidder("Bidder","McBidder", "bidder@bidder.com");

        Bidder bidderUser2 =
                new Bidder("Bidly", "Bidderson", "bidder2@bidder.com");


        addUser(bidderUser);
        addUser(bidderUser2);
        addUser(contactUser);
        addUser(contactUser2);
        addUser(contactUser3);

        //create auctions

        Auction auction2 = new Auction("#SaveTheDoDo", "#SaveTheDoDo".hashCode(),
                LocalDateTime.of(2018, 05, 30, 15, 00),
                null);

        // Build Auction 1 for contactUser //
        Auction auction1 = new Auction(contactUser.getMyOrgName(),contactUser.getMyOrgID(), LocalDateTime.of(2018, 05, 30, 10, 00),null);
        addAuction(auction1);
        AuctionItem item1 = new AuctionItem(20.00, "Penguin Pre-Breathers");
        AuctionItem item2 = new AuctionItem(50.00, "Penguin Flippers");
        AuctionItem item3 = new AuctionItem(62.00, "Veal - Chops, Split, Frenched");
        addAuctionItem(auction1, item1);
        addAuctionItem(auction1, item2);
        addAuctionItem(auction1, item3);
        Bid bid1 = new Bid(auction1, BigDecimal.valueOf(75), getAuctionItem(647));
        placeBid(bidderUser, bid1);
        Bid bid2 = new Bid(auction1, BigDecimal.valueOf(80), getAuctionItem(647));
        placeBid(bidderUser2, bid2);

        // Build auction 8 for contact user //
        Auction auction8 = new Auction(contactUser.getMyOrgName(), contactUser.getMyOrgName().hashCode(), LocalDateTime.of(2017 , 2, 10, 11, 00), null);
        addAuction(auction8);

        AuctionItem item4 = new AuctionItem(	71.00, "Lettuce - Escarole");
        addAuctionItem(auction8, item4);
        AuctionItem item5 = new AuctionItem(22.00, "Tumeric");
        addAuctionItem(auction8, item5);
        AuctionItem item6 = new AuctionItem(35.00, "Nantucket - Orange Mango Cocktail");
        addAuctionItem(auction8, item6);

        //Build auction 9 for contact user//
        Auction auction9 = new Auction(contactUser.getMyOrgName(), contactUser.getMyOrgName().hashCode(), LocalDateTime.of(2016, 1, 25, 11, 00), null);
        addAuction(auction9);




        //  Bid 3 = new Bid(auction8, BigDecimal.valueOf(76), getAuctionItem())

        contactUser.setMyCurrentAuction(auction1);

        // Contactuser 2 //

        Auction auction3 = new Auction(contactUser2.getMyOrgName(), contactUser2.getMyOrgName().hashCode(), LocalDateTime.of(2017, 04, 1, 11, 00), null);
        addAuction(auction3);

        Auction auction4 = new Auction(contactUser2.getMyOrgName(), contactUser2.getMyOrgName().hashCode(), LocalDateTime.of(2018, 06, 20, 11, 00), null);
        addAuction(auction4);
        AuctionItem item7 = new AuctionItem(46.00, "Scampi Tail");
        AuctionItem item8 = new AuctionItem(76.00, "Tumeric");
        AuctionItem item9 = new AuctionItem(88.00, "Creamers - 10%");
        AuctionItem item10 = new AuctionItem(22.00, "Oil - Shortening - All - Purpose");
        addAuctionItem(auction4, item7);
        addAuctionItem(auction4, item8);
        addAuctionItem(auction4, item9);
        addAuctionItem(auction4, item10);


        Auction auction5 = new Auction(contactUser2.getMyOrgName(), contactUser2.getMyOrgName().hashCode(), LocalDateTime.of(2016, 03, 2, 11, 00), null);
        addAuction(auction5);
        Auction auction6 = new Auction(contactUser2.getMyOrgName(), contactUser2.getMyOrgName().hashCode(), LocalDateTime.of(2015, 2, 17, 11, 00), null);
        addAuction(auction6);



        // Contactuser 3//
        Auction auction7 = new Auction(contactUser3.getMyOrgName(), contactUser3.getMyOrgName().hashCode(), LocalDateTime.of(2018, 2, 15, 11, 00), null);
        addAuction(auction7);
        addAuctionItem(auction7, new AuctionItem(45.00, "Spinach - Spinach Leaf"));
        addAuctionItem(auction7, new AuctionItem(85.00, "Truffle Cups - White Paper"));
        addAuctionItem(auction7, new AuctionItem(26.00, "Milk - 1%"));
        addAuctionItem(auction7, new AuctionItem(30.00, "Sultanas"));

        Auction auction10 = new Auction(contactUser3.getMyOrgName(), contactUser3.getMyOrgName().hashCode(), LocalDateTime.of(2014 , 8, 10, 11, 00), null);
        addAuction(auction10);
        Auction auction11 = new Auction(contactUser3.getMyOrgName(), contactUser3.getMyOrgName().hashCode(), LocalDateTime.of(2013 , 4, 21, 11, 00), null);
        addAuction(auction11);


        System.out.println("Finished adding sample data");

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
    public AuctionCalendar getMyAuctionCalendar() {
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

    private void addUser(User theUser){
        myUserDB.userDirectory.put(theUser.getEmail(), theUser);

        if(theUser.getClass().equals(Bidder.class))
            myAuctionCalendar.userBids.put((Bidder)theUser, new ArrayList<Bid>());
    }


    //~~Auctions Data Handling~~//

    /**
     * Returns a sorted list of all auctions by organization, sorted chronologically
     * from newest to oldest.
     * @return an array of auctions associated with an organization.
     */
    public ArrayList<Auction> getAuctionsByOrg(String theOrg){

        ArrayList<Auction> returnList = new ArrayList<>();

        for(Auction a : myAuctionCalendar.auctionDB.keySet()){
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

        for(Auction a : myAuctionCalendar.auctionDB.keySet()){
            if(a.getauctionID()==auctionID)
                return a;
        }
        return null;
    }

    /**
     * Creates an array list of auctions that have been scheduled that are "active" - meaning
     * they haven't passed yet and can be actively bid on.
     * @return an arraylist of auctions.
     */
    public ArrayList<Auction> getActiveAuctions(){
        myAuctionCalendar.updateCalendar();
        return myAuctionCalendar.activeAuctions;
    }

    public ArrayList<Auction> getPastAuctions(){
        return myAuctionCalendar.pastAuctions;
    }

    //~~Auction Item Data Handling ~~//

    public Set<AuctionItem> getAuctionItemsByAuction(Auction theAuction){
        return myAuctionCalendar.auctionDB.get(theAuction).keySet();
    }

    /**
     * Returns an auction item by its item ID.
     * @param theItemID the id of the auction item
     * @return an auction item.
     */
    public AuctionItem getAuctionItem(int theItemID){

        for (Auction a: myAuctionCalendar.auctionDB.keySet()) {
            for(AuctionItem i : myAuctionCalendar.auctionDB.get(a).keySet()){
                if(null != i && i.getUniqueID() == theItemID)
                    return i;
            }
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

        for (AuctionItem i: myAuctionCalendar.auctionDB.get(theAuction).keySet()) {

            if(i.getUniqueID() == theItemID)
                return i;
        }
        return null;
    }

    /**
     * Creates a null Hashset for Items,Bids and adds it to the database
     * @param theAuction the auction being added as a key to the database.
     */
    public void addAuction(Auction theAuction){

        HashMap<AuctionItem, ArrayList<Bid>> emptyMap = new HashMap<>();

        myAuctionCalendar.auctionDB.put(theAuction, emptyMap);
        myAuctionCalendar.updateCalendar();



    }

    /**
     * Adds an auction item to an auction and notifies observers.
     * @param theAuction an item is being added to.
     * @param theItem an item being added to the auction.
     */
    public void addAuctionItem(Auction theAuction, AuctionItem theItem){

        ArrayList<Bid> emptyBidList = new ArrayList<>();
        myAuctionCalendar.auctionDB.get(theAuction).put(theItem, emptyBidList);

        theAuction.addInventoryItem(theItem);

        myAuctionCalendar.updateCalendar();
        notifyUpdateItem(theItem);
    }

    //~~Bids Data Handling~~//

    /**
     * Adds a new bid to an auction item and notifies observers.
     * @param theBid being added to an item.
     * @param theBidder the unique bidder assocaited with this bid
     */
    public void placeBid(Bidder theBidder, Bid theBid){
        AuctionItem item = theBid.getItem();
        Auction auction = theBid.getAuction();
        myAuctionCalendar.auctionDB.get(auction).get(item).add(theBid);


        if(!myAuctionCalendar.userBids.isEmpty()
                && myAuctionCalendar.userBids.get(theBidder).contains(theBid)){

            myAuctionCalendar.userBids.get(theBidder).remove(theBid);
        }

        myAuctionCalendar.userBids.get(theBidder).add(theBid);
        theBidder.placeBid(theBid);
        myAuctionCalendar.updateCalendar();
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
        this.setChanged();
        notifyObservers(theBid);
    }

    private void notifyUpdateAuctions(Auction theAuction){
        this.setChanged();
        notifyObservers(theAuction);
    }

    private void notifyUpdateItem(AuctionItem theItem){
        this.setChanged();
        notifyObservers(theItem);
    }

}