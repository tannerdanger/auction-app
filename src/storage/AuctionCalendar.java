package storage;
 
import auctiondata.Auction;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
 
/**
 * A serializable class that stores all auction data.
 * @author Tanner Brown
 * @version 3 May 2018
 */
public class AuctionCalendar implements Serializable {

	private static int MAX_UPCOMING_AUCTIONS = 25;
	private static int MAX_AUCTIONS_DAY = 2;
	private static LocalDateTime MINIMUM_AUCTION_SCHEDULE_DATE = LocalDateTime.now().plusDays(14);
	private static LocalDateTime MAX_AUCTION_SCHEDULE_DATE = LocalDateTime.now().plusDays(60);
	private static int MAX_BIDS_PER_BIDDER = 12;
	private static int MAX_ITEMS_PER_AUCTION = 10;

 
    private static final long serialVersionUID = 18675309L;
 
    protected ArrayList<Auction> auctionDataBase = new ArrayList<>();
 
    /**
     * Adds an auction to the calendar.
     * @param theAuction being added.
     */
    public void addAuction(Auction theAuction){
        auctionDataBase.add(theAuction);
    }
   
    /**
     * Returns scheduled auctions.
     * @return an arraylist of auctions.
     */
    public ArrayList<Auction> getAuctionDataBase() {
        return auctionDataBase;
    }
   
    public ArrayList<Auction> getPastAuctions() {
    	ArrayList<Auction> pastAuctions = new ArrayList<Auction>();
    	for(Auction a: auctionDataBase) {
    		if(a.getAuctionDate().isBefore(LocalDate.now())) {
    			pastAuctions.add(a);
    		}
    	}
        return pastAuctions;
    }
    
    public ArrayList<Auction> getActiveAuctions() {
    	ArrayList<Auction> activeAuctions = new ArrayList<Auction>();
    	for(Auction a: auctionDataBase) {
    		if(LocalDate.now().isBefore(a.getAuctionDate())) {
    			activeAuctions.add(a);
    		}
    	}
        return activeAuctions;
    }
    
    /**
     * Allows the system to retrieve information on an auction by its ID.
     * @param auctionID the ID of the auction.
     * @return an auction associated with th parameter.
     */
    public Auction getAuction(int auctionID){
        Auction returnAuction = null;
        for(Auction a: auctionDataBase){
            if(a.getauctionID() == auctionID) {
                returnAuction = a;
                break;
            }
        }
        return returnAuction;
    }

	//~~Updating Default Values~~//

	/**
	 * Updates the max number of upcoming auctions, provided the new max is greater
	 * than the number of currently scheduled auctions.
	 * @param theNewMax - new max number of auctions that may be scheduled.
	 */
	public void updateMaxUpcomingAuctions(int theNewMax){
    	if(theNewMax > getActiveAuctions().size())
            MAX_UPCOMING_AUCTIONS = theNewMax;
	}

 
}