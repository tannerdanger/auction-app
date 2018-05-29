package storage;

import GUI.ErrorPopup;
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


    private AuctionCalendar myAuctionCalendar;
    private UserDB myUserDB;

    /**
     * Constructs data by serializing or initializing new data as necessary.
     */
    public DataHandler(){
        ///TODO: Uncomment below when changes are done being made to serialized data classes.
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
    private void initializeData(){

        myAuctionCalendar = new AuctionCalendar();
        myUserDB = new UserDB();

        //create users
        AuctionStaff staffUser = new AuctionStaff("James", "GuyantPeach", "staff@staff.com");
        addUser(staffUser);

        //Create contact with active auction
        ContactPerson contactWithActiveAuction =
                new ContactPerson("Alfred", "McContact", "contact@contact.com", myAuctionCalendar);
        contactWithActiveAuction.setMyOrgName("Pat's Pneumonic Penguin Preservation");
        addUser(contactWithActiveAuction);
        //set active auction
        Auction auction1 = new Auction(contactWithActiveAuction.getMyOrgName(),contactWithActiveAuction.getMyOrgID(), LocalDateTime.of(2018, 05, 30, 10, 00),null);
        addAuction(auction1);
        AuctionItem item1 = new AuctionItem(20.00, "Penguin Pre-Breathers", auction1);
        AuctionItem item2 = new AuctionItem(50.00, "Penguin Flippers", auction1);
        AuctionItem item3 = new AuctionItem(62.00, "Veal - Chops, Split, Frenched", auction1);
        addAuctionItem(auction1, item1);
        addAuctionItem(auction1, item2);
        addAuctionItem(auction1, item3);
        //contactWithActiveAuction.setMyCurrentAuction(auction1);

        //Create contact without active auctions
        ContactPerson contactWithoutAuctions = new ContactPerson("Migel", "Garcia", "noauction@contact.com", myAuctionCalendar);
        contactWithoutAuctions.setMyOrgName("The Human Fund");
        addUser(contactWithActiveAuction);

        //Create a bidder with bids
        Bidder bidderwithbids = new Bidder("Bidder","McBidder", "bidder@bidder.com");
        addUser(bidderwithbids);
        Bid bid1 = new Bid(auction1, BigDecimal.valueOf(75), getAuctionItem(647));
        placeBid(bidderwithbids, bid1);

        //Create bidder without bids
        Bidder bidderWithoutBids = new Bidder("Bud", "Bidder", "nobids@bidder.com");
        addUser(bidderwithbids);

        //Create an anonymous contact with lots of auctions and bids
        ContactPerson anonContact = new ContactPerson("Anon", "NoName", "dont@login.com", myAuctionCalendar);
        anonContact.setMyOrgName("anonymouslol");
        Auction auction2 = new Auction(anonContact.getMyOrgName(),anonContact.getMyOrgID(), LocalDateTime.of(2017, 05, 20, 10, 00),null);
        addAuction(auction2);
        Auction auction3 = new Auction(anonContact.getMyOrgName(),anonContact.getMyOrgID(), LocalDateTime.of(2016, 05, 10, 10, 00),null);
        addAuction(auction3);
        Auction auction4 = new Auction(anonContact.getMyOrgName(),anonContact.getMyOrgID(), LocalDateTime.of(2015, 05, 1, 10, 00),null);
        addAuction(auction4);

        AuctionItem item4 = new AuctionItem(46.00, "Scampi Tail", auction2);
        AuctionItem item5 = new AuctionItem(76.00, "Tumeric", auction2);
        AuctionItem item6 = new AuctionItem(20.00, "Bikes", auction2);
        AuctionItem item7 = new AuctionItem(25.00, "Kars for Kids", auction2);
        AuctionItem item8 = new AuctionItem(5.25, "Plain ol water", auction2);
        AuctionItem item9 = new AuctionItem(50.25, "Fancy Wanter", auction2);
        AuctionItem item10 = new AuctionItem(10.00, "Riding Lawnmower", auction3);
        AuctionItem item11 = new AuctionItem(15, "Baloons?", auction3);
        AuctionItem item12 = new AuctionItem(20, "A spaceship", auction3);
        addAuctionItem(auction2, item4);
        addAuctionItem(auction2, item5);
        addAuctionItem(auction2, item6);
        addAuctionItem(auction2, item7);
        addAuctionItem(auction2, item8);
        addAuctionItem(auction2, item9);
        addAuctionItem(auction3, item10);
        addAuctionItem(auction3, item11);
        addAuctionItem(auction3, item12);
        //Create bidder with max bids
        Bidder bidderWithMaxBids = new Bidder("Max", "Bidder", "max@bidder.com");
        addUser(bidderWithMaxBids);
        placeBid(bidderWithMaxBids, new Bid(auction1, BigDecimal.valueOf(80), item1));
        placeBid(bidderWithMaxBids, new Bid(auction1, BigDecimal.valueOf(80), item2));
        placeBid(bidderWithMaxBids, new Bid(auction1, BigDecimal.valueOf(80), item3));
        placeBid(bidderWithMaxBids, new Bid(auction2, BigDecimal.valueOf(80), item4));
        placeBid(bidderWithMaxBids, new Bid(auction2, BigDecimal.valueOf(80), item5));
        placeBid(bidderWithMaxBids, new Bid(auction2, BigDecimal.valueOf(80), item6));
        placeBid(bidderWithMaxBids, new Bid(auction2, BigDecimal.valueOf(80), item7));
        placeBid(bidderWithMaxBids, new Bid(auction2, BigDecimal.valueOf(80), item8));
        placeBid(bidderWithMaxBids, new Bid(auction2, BigDecimal.valueOf(80), item9));
        placeBid(bidderWithMaxBids, new Bid(auction3, BigDecimal.valueOf(80), item10));
        placeBid(bidderWithMaxBids, new Bid(auction3, BigDecimal.valueOf(80), item11));
        placeBid(bidderWithMaxBids, new Bid(auction3, BigDecimal.valueOf(80), item12));



        ContactPerson contactUser2 = new ContactPerson("Lars", "Rush", "contact2@contact.com", myAuctionCalendar);
        contactUser2.setMyOrgName("Odio Corp.");

        ContactPerson contactUser3 = new ContactPerson("Russ", "Walker", "Contact3@contact.com", myAuctionCalendar);
        contactUser3.setMyOrgName("Risus Industries");

        ContactPerson noContact = new ContactPerson("Migel", "Garcia", "contact4@contact.com", myAuctionCalendar);
        noContact.setMyOrgName("The Human Fund");

        myUserDB.addUser(noContact);



        Bidder bidderUser2 =
                new Bidder("Bidly", "Bidderson", "bidder2@bidder.com");


        addUser(bidderUser2);

        addUser(contactUser2);
        addUser(contactUser3);

        Bid bid2 = new Bid(auction1, BigDecimal.valueOf(80), getAuctionItem(647));
        placeBid(bidderUser2, bid2);

        // Build auction 8 for contact user //
        Auction auction8 = new Auction(contactWithActiveAuction.getMyOrgName(), contactWithActiveAuction.getMyOrgName().hashCode(), LocalDateTime.of(2017 , 2, 10, 11, 00), null);
        addAuction(auction8);

        AuctionItem itemm = new AuctionItem(	71.00, "Lettuce - Escarole", auction8);
        addAuctionItem(auction8, itemm);
        AuctionItem item = new AuctionItem(22.00, "Tumeric", auction8);
        addAuctionItem(auction8, item);
        AuctionItem item16 = new AuctionItem(35.00, "Nantucket - Orange Mango Cocktail", auction8);
        addAuctionItem(auction8, item16);

        //Build auction 9 for contact user//
        Auction auction9 = new Auction(contactUser3.getMyOrgName(), contactUser3.getMyOrgName().hashCode(), LocalDateTime.of(2016, 1, 25, 11, 00), null);
        addAuction(auction9);




        //  Bid 3 = new Bid(auction8, BigDecimal.valueOf(76), getAuctionItem())



        // Contactuser 2 //

        Auction auction13 = new Auction(contactUser2.getMyOrgName(), contactUser2.getMyOrgName().hashCode(), LocalDateTime.of(2017, 04, 1, 11, 00), null);
        addAuction(auction13);

        Auction auction14 = new Auction(contactUser2.getMyOrgName(), contactUser2.getMyOrgName().hashCode(), LocalDateTime.of(2018, 06, 20, 11, 00), null);
        addAuction(auction14);

        AuctionItem item19 = new AuctionItem(88.00, "Creamers - 10%", auction14);
        AuctionItem item110 = new AuctionItem(22.00, "Oil - Shortening - All - Purpose", auction14);



        addAuctionItem(auction14, item19);
        addAuctionItem(auction14, item110);


        Auction auction5 = new Auction(contactUser2.getMyOrgName(), contactUser2.getMyOrgName().hashCode(), LocalDateTime.of(2016, 03, 2, 11, 00), null);
        addAuction(auction5);
        Auction auction6 = new Auction(contactUser2.getMyOrgName(), contactUser2.getMyOrgName().hashCode(), LocalDateTime.of(2015, 2, 17, 11, 00), null);
        addAuction(auction6);



        // Contactuser 3//
        Auction auction7 = new Auction(contactUser3.getMyOrgName(), contactUser3.getMyOrgName().hashCode(), LocalDateTime.of(2018, 2, 15, 11, 00), null);
        addAuction(auction7);
        addAuctionItem(auction7, new AuctionItem(45.00, "Spinach - Spinach Leaf", auction7));
        addAuctionItem(auction7, new AuctionItem(85.00, "Truffle Cups - White Paper", auction7));
        addAuctionItem(auction7, new AuctionItem(26.00, "Milk - 1%", auction7));
        addAuctionItem(auction7, new AuctionItem(30.00, "Sultanas", auction7));

        Auction auction10 = new Auction(contactUser3.getMyOrgName(), contactUser3.getMyOrgName().hashCode(), LocalDateTime.of(2014 , 8, 10, 11, 00), null);
        addAuction(auction10);
        Auction auction11 = new Auction(contactUser3.getMyOrgName(), contactUser3.getMyOrgName().hashCode(), LocalDateTime.of(2013 , 4, 21, 11, 00), null);
        addAuction(auction11);


        //randomly scheduled future auctions
        addAuction(new Auction("United Path", 222,LocalDateTime.of(2018, 6, 15, 10,00), null));
        addAuction(new Auction("EvergreenPeace", 333,LocalDateTime.of(2018, 6, 15, 14,00), null));
        addAuction(new Auction("Orange Cross",444,LocalDateTime.of(2018, 6, 18, 10,00), null));
        addAuction(new Auction("Shell Corporation",555,LocalDateTime.of(2018, 6, 18, 14,00), null));
        addAuction(new Auction("Umbrella Corp.", 111,LocalDateTime.of(2018, 6, 25, 10,00), null));


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
        //myAuctionCalendar.updateCalendar();
        return myAuctionCalendar.activeAuctions;
    }



    public ArrayList<Auction> getPastAuctions(){
        return myAuctionCalendar.pastAuctions;
    }

    //~~Auction Item Data Handling ~~//

    /**
     * Allows a contact to delete an auction by it's auction ID
     * @param theAuctionID a unique ID identifying the auction
     */
    public void deleteAuction(int theAuctionID){
        deleteAuction(getAuctionByID(theAuctionID));
    }

    /**
     * Allows a contact to delete an auction by a reference to the auction
     * @param theAuction an auction to delete from the database
     */
    public void deleteAuction(Auction theAuction){
        //Verify that the auction
        Set<AuctionItem> itemSet = myAuctionCalendar.auctionDB.get(theAuction).keySet();
        for(AuctionItem item : itemSet){
            if(null == item.getSealedBids()
                    || item.getSealedBids().isEmpty()){
                myUserDB.userDirectory.remove(theAuction);
            }else if(item.getSealedBids().size() > 0){
                new ErrorPopup("Remove Auction Error","Cant remove Auction because it contains active bids");
            }
        }
    }

    private void verifyNoBids(AuctionItem theItem) {
        theItem.getSealedBids();
    }

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

        //Adds auction to database with empty auction item list
        myAuctionCalendar.auctionDB.put(theAuction, emptyMap);
        //Updates Calendar
        myAuctionCalendar.updateCalendar();
        //Gets the contact person associated with auction
        ContactPerson c = getContactUserForOrg(theAuction.getOrgName());
        //sets the current auction for that contact
        if(!(null==c)) {
            c.setMyCurrentAuction(theAuction);
        }

        notifyUpdateAuctions(theAuction);
    }
    
    public void cancelAuction (int auctionID) {
    	int index = 0;
    	for(Auction a: myAuctionCalendar.getActiveAuctions()){
            if(a.getauctionID() == auctionID) {
                break;
            }
            index++;
        }
    	if (!(myAuctionCalendar.getActiveAuctions().get(index).hasBid())) {
    		myAuctionCalendar.auctionDB.remove(myAuctionCalendar.getActiveAuctions().get(index));
    		myAuctionCalendar.getActiveAuctions().remove(index);
    		
    	}
    }

    /**
     * Overloaded method that allows for faster adding of auctions if a reference
     * to the contact user is available.
     *
     * @param theAuction the auction being added as a key to the database.
     * @param theContact the contact user assocaited with this auction
     */
    public void addAuction(Auction theAuction, ContactPerson theContact){

        HashMap<AuctionItem, ArrayList<Bid>> emptyMap = new HashMap<>();

        //Adds auction to database with empty auction item list
        myAuctionCalendar.auctionDB.put(theAuction, emptyMap);
        //Updates Calendar
        myAuctionCalendar.updateCalendar();

        //sets the current auction for that contact
        theContact.setMyCurrentAuction(theAuction);

        notifyUpdateAuctions(theAuction);
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

    public Auction getNextAuction(){
        myAuctionCalendar.updateCalendar();
        return myAuctionCalendar.activeAuctions.get(0);
    }

    public String getContactForOrg(String theOrgName){
        String retStr = "No Contact info Found";
        for (User u : myUserDB.userDirectory.values()){

            if(ContactPerson.class.equals(u.getClass())) {
                ContactPerson c = (ContactPerson)u;
                if (c.getMyOrgName().compareTo(theOrgName) == 0) {
                    retStr = c.getEmail();
                }
            }
        }
        return retStr;
    }

    public ContactPerson getContactUserForOrg(String theOrgName){


        for (User u : myUserDB.userDirectory.values()){

            if(ContactPerson.class.equals(u.getClass())) {
                ContactPerson c = (ContactPerson)u;
                if (c.getMyOrgName().compareTo(theOrgName) == 0) {
                    return c;
                }
            }
        }
        return null;
    }


    //~~Bids Data Handling~~//

    /**
     * Adds a new bid to an auction item and notifies observers.
     * @param theBid being added to an item.
     * @param theBidder the unique bidder associated with this bid
     */
    public void placeBid(Bidder theBidder, Bid theBid){
        AuctionItem item = theBid.getItem();
        Auction auction = theBid.getAuction();
        myAuctionCalendar.auctionDB.get(auction).get(item).add(theBid); //Add to DB

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
        serialize();
        this.setChanged();
        notifyObservers(theBid);
    }

    private void notifyUpdateAuctions(Auction theAuction){
        serialize();
        this.setChanged();
        notifyObservers(theAuction);
    }

    private void notifyUpdateItem(AuctionItem theItem){
        serialize();
        this.setChanged();
        notifyObservers(theItem);
    }
}
