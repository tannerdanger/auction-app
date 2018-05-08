package users;

import auctiondata.Auction;
import auctiondata.AuctionItem;
import auctiondata.Scheduler;
import storage.DataHandler;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
/**
 * Contact person class, saves relevent information to a Contact
 */
public class ContactPerson extends User implements Serializable {
	/**
	 * serialized id
	 */
	private static final long serialVersionUID = -710092057770182337L;
	//private ArrayList<Auction> mySubmittedAuctions;

	private String myOrgName;
	private int myOrgID;

	/*
	 * The Contact Person's previous Auction
	 */
	private Auction myPriorAuction;



    /*
	 * The Contact Person's current Auction
	 */
	private Auction myCurrentAuction;
	private Scheduler myScheduler;
	/*
	 * Constructor for Contact Person, Will take a First name, Last name, and email.
	 * Will set the current and prior auction to null.
	 */
	public ContactPerson(String theFirst, String theLast, String theEmail) {
		super(theFirst, theLast, theEmail); //pass basic ID values to User superclass
		myPriorAuction = null;
		myCurrentAuction = null;
	}

	public boolean addInventoryItem(String name, BigDecimal minBid) {
		boolean result = false;


		if(null != name && minBid != null) {
			myCurrentAuction.addInventoryItem(new AuctionItem(minBid.doubleValue(), name));
			myCurrentAuction.printInventorySheet();
			result = true;
		}
		return result;
	}

	public Auction createNewAuction(LocalDateTime theDate) {
		Auction newAuction = new Auction(myOrgName, myOrgID, theDate, null);
		setMyCurrentAuction(newAuction);
	    //mySubmittedAuctions.add(newAuction);
		myCurrentAuction = newAuction;
		return newAuction;
	}
	
	public Auction getMyCurrentAuction() {
		return myCurrentAuction;
	}

	public void setMyOrgName(String myOrgName) {
		this.myOrgName = myOrgName;
		this.myOrgID = myOrgName.hashCode();
	}

    public int getMyOrgID() {
        return myOrgID;
    }

    public Auction getPriorAuction() {
    	return myPriorAuction;
    }
    
    public String getMyOrgName() {
        return myOrgName;
    }
    //TODO: Delete this after testing?
    public void setMyCurrentAuction(Auction theCurrentAuction) {
    	if(myCurrentAuction != null) {
    		myPriorAuction = myCurrentAuction;
    	}
        myCurrentAuction = theCurrentAuction;
    }
}