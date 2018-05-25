package users;

import auctiondata.Auction;
import auctiondata.AuctionItem;
import auctiondata.Scheduler;
import storage.DataHandler;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
/**
 * Contact person class, saves relevent information to a Contact
 * @author Gurchetan Singh, David Perez
 * @version 05/07/2018
 */
public class ContactPerson extends User {
	/**
	 * serialized id
	 */
	private static final long serialVersionUID = -710092057770182337L;
	private ArrayList<Auction> mySubmittedAuctions;
	private static final long REQUIRED_YEARS_IN_BETWEEN_AUCTION = 1;
	private static final long REQUIRED_MONTHS_IN_BETWEEN_AUCTION = 0;
	private static final long REQUIRED_DAYS_IN_BETWEEN_AUCTION = 0;
	
	private String myOrgName;
	private int myOrgID;

	/*
	 * The Contact Person's previous Auction
	 */
	private Auction myCurrentAuction;

	/*
	 * Constructor for Contact Person, Will take a First name, Last name, and email.
	 * Will set the current and prior auction to null.
	 */
	public ContactPerson(String theFirst, String theLast, String theEmail) {
		super(theFirst, theLast, theEmail); //pass basic ID values to User superclass
		myCurrentAuction = null;
		mySubmittedAuctions = new ArrayList<Auction>();
	}


	public void displaySubmittedAuctions() {
		for(Auction a : mySubmittedAuctions){
			System.out.println(a.toString());
		}
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

	public void auctionSuccesfullyCreated(Auction theAuction) {
		setMyCurrentAuction(theAuction);
	    mySubmittedAuctions.add(theAuction);
	}
	
	public Auction createNewAuction(LocalDateTime theDate) {
		Auction newAuction = new Auction(myOrgName, myOrgID, theDate, null);
		auctionSuccesfullyCreated(Auction newAuction) {
		return newAuction;
	}	
	
	public void setMyOrgName(String myOrgName) {
		this.myOrgName = myOrgName;
		this.myOrgID = myOrgName.hashCode();
	}

	public boolean isValidAuction(LocalDateTime theDate) {
		boolean flag = isThereNoPriorAuction();
		if (!flag) {
			flag = isRequiredTimeElapsedBetweenPriorAndNewAuctionMet(theDate.toLocalDate());
		}
		return flag;
	}
	
	public boolean isRequiredTimeElapsedBetweenPriorAndNewAuctionMet(final LocalDate theNewAuctionDate) {
		
		boolean flag = false;
		if (!isThereNoPriorAuction()) {
			LocalDate checkRequiredTimePassDate = 
					myCurrentAuction.getAuctionDate().plusYears(REQUIRED_YEARS_IN_BETWEEN_AUCTION
					).plusMonths(REQUIRED_MONTHS_IN_BETWEEN_AUCTION
					).plusDays(REQUIRED_DAYS_IN_BETWEEN_AUCTION);
			flag = checkRequiredTimePassDate.isBefore(theNewAuctionDate) 
				|| checkRequiredTimePassDate.equals(theNewAuctionDate);
		}
		return flag;

	}
	public boolean isThereNoPriorAuction () {
		boolean flag = false;
		if (myCurrentAuction == null) {
			flag = true;
		}
		return flag;
	}
	
    public int getMyOrgID() {
        return myOrgID;
    }

    public Auction getCurrentAuction() {
    	return myCurrentAuction;
    }
    
    public String getMyOrgName() {
        return myOrgName;
    }
    //TODO: Delete this after testing?
    public void setMyCurrentAuction(Auction theCurrentAuction) {
        myCurrentAuction = theCurrentAuction;
    }
    
}	
