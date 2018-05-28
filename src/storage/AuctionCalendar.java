package storage;

import auctiondata.Auction;
import auctiondata.AuctionItem;
import auctiondata.Bid;
import users.Bidder;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * A serializable class that stores all auction data.
 * @author Tanner Brown
 * @version 3 May 2018
 */
public class AuctionCalendar implements Serializable {



	private int MAX_UPCOMING_AUCTIONS = 25;
	private int MAX_AUCTIONS_DAY = 2;
	private LocalDateTime MINIMUM_AUCTION_SCHEDULE_DATE = LocalDateTime.now().plusDays(14);
	private LocalDateTime MAX_AUCTION_SCHEDULE_DATE = LocalDateTime.now().plusDays(60);
	private int MAX_BIDS_PER_BIDDER = 12;
	private int MAX_ITEMS_PER_AUCTION = 10;


	private static final long serialVersionUID = 18675309L;


	//protected ArrayList<Auction> auctionDataBase = new ArrayList<>();
	protected HashMap<Auction, HashMap<AuctionItem, ArrayList<Bid>>> auctionDB = new HashMap<>();
	public Map<Bidder, ArrayList<Bid>> userBids = new HashMap<>();
//	protected HashMap<Auction, HashSet<AuctionItem>> auctionDataBase = new HashMap<>();
//	protected HashMap<AuctionItem, ArrayList<Bid>> bidsDB = new HashMap<>();
//	protected HashMap<Bidder, HashSet<Bid>> bidderDataBase = new HashMap<>();
	protected ArrayList<Auction> activeAuctions = new ArrayList<>();
	protected ArrayList<Auction> pastAuctions = new ArrayList<>();

	protected void updateCalendar(){
		updateActiveAuctions();
		sortAuctions(activeAuctions);
		sortAuctions(pastAuctions);
	}
	public ArrayList<Auction> getActiveAuctions() {
		updateCalendar();
		return activeAuctions;
	}
		

	/**
	 * Checks the DB to see what auctions have yet to take place, and creates an array
	 * of available auctions to bid on. It calls on a helper method to sort the array
	 * by date.
	 */
	private void updateActiveAuctions(){
		for (Auction a : auctionDB.keySet()) {

			if(a.getAuctionDate().isAfter(LocalDate.now())
					&& !activeAuctions.contains(a)){

				activeAuctions.add(a);
			}
			else
				pastAuctions.add(a);
		}
	}

	protected void sortAuctions(ArrayList<Auction> theAuctions){
		theAuctions.sort((a1, a2)->{
			if(a1.getAuctionDate().isBefore(a2.getAuctionDate())
					|| a1.getAuctionDate().isEqual(a2.getAuctionDate())){
				return 1;
			}else{
				return -1;
			}
		});
	}


	//~~Getters and Setters for admin/staff only~~//
	public int getMAX_UPCOMING_AUCTIONS() {
		return MAX_UPCOMING_AUCTIONS;
	}

	public void setMAX_UPCOMING_AUCTIONS(int theNewMax) {
		if(theNewMax >= activeAuctions.size()) {
			this.MAX_UPCOMING_AUCTIONS = theNewMax;
		}
	}

	public int getTotalScheduledAuctions(){
		updateCalendar();
		return activeAuctions.size();
	}

	/**
	 * Iterates through all of the active auctions and determines the max amount
	 * scheduled for any one day. This is used to ensure the new max doesn't conflict
	 * with the already scheduled amount of auctions in a day.
	 * @return the max number of auctions scheduled in a day
	 */
	public int getMaxAuctionsScheduledInADay(){
		HashMap<LocalDate, Integer> dateMap = new HashMap<>();
		for(Auction a : activeAuctions){

			if(dateMap.keySet().contains(a.getAuctionDate())) {
				Integer tmp = dateMap.get(a.getAuctionDate());
				tmp++;
				dateMap.replace(a.getAuctionDate(), tmp);
			}
		}
		int max = 0;
		for(Integer i : dateMap.values()){
			if(i > max)
				max = i;
		}
		return max;
	}

	public HashMap<Auction, HashMap<AuctionItem, ArrayList<Bid>>> getAuctionDB() {
		return auctionDB;
	}

	/**
	 * Finds all auctions between two dates and returns a list of them.
	 * @param theStart the start date of the date window
	 * @param theEnd the end date of the window
	 * @return an arraylist of auctions between the start and end dates.
	 */
	public ArrayList<Auction> getAuctionsBetweenDates(LocalDate theStart, LocalDate theEnd){
		if(theStart.isBefore(theEnd)) {
			ArrayList<Auction> returnList = new ArrayList<>();
			for (Auction a : auctionDB.keySet()) {
				if (a.getAuctionDate().isAfter(theStart)
						&& a.getAuctionDate().isBefore(theEnd)) {
					returnList.add(a);
				}
			}
			if (!returnList.isEmpty())
				sortAuctions(returnList);

			return returnList;
		}else
			return null;
	}



	public int getMAX_AUCTIONS_DAY() {
		return MAX_AUCTIONS_DAY;
	}

	public void setMAX_AUCTIONS_DAY(int theNewMax) {
		if(theNewMax > 0 && theNewMax >= getMaxAuctionsScheduledInADay())
			this.MAX_AUCTIONS_DAY = theNewMax;
	}

	public int getMAX_BIDS_PER_BIDDER() {
		return MAX_BIDS_PER_BIDDER;
	}

	public void setMAX_BIDS_PER_BIDDER(int MAX_BIDS_PER_BIDDER) {
		this.MAX_BIDS_PER_BIDDER = MAX_BIDS_PER_BIDDER;
	}

	public int getMAX_ITEMS_PER_AUCTION() {
		return MAX_ITEMS_PER_AUCTION;
	}

	public void setMAX_ITEMS_PER_AUCTION(int MAX_ITEMS_PER_AUCTION) {
		this.MAX_ITEMS_PER_AUCTION = MAX_ITEMS_PER_AUCTION;
	}


/*
    public void cancelAuction (int auctionID) {
    	int index = 0;
    	for(Auction a: allAuctions){
            if(a.getauctionID() == auctionID) {
                break;
            }
            index++;
        }
    	if (!(allAuctions.get(index).hasBid())) {
    		allAuctions.remove(index);
    	}
    }
    
    public ArrayList<Auction> getAuctionsBetweenTwoDates(LocalDateTime earlierDate, LocalDateTime laterDate) {
    	//updateCalendar();
    	ArrayList<Auction> auctionsToDisplay = new ArrayList<Auction>();
    	
    	for (Auction a: allAuctions) {
    		if (a.getAuctionDateTime().isAfter(earlierDate) || a.getAuctionDateTime().isEqual(earlierDate)
    			&& a.getAuctionDateTime().isBefore(laterDate) || a.getAuctionDateTime().isEqual(laterDate)) {
    			auctionsToDisplay.add(a);
    		}
    	}
    	return auctionsToDisplay;
    }*/

//	/**
//	 * Adds an auction to the calendar.
//	 * @param theAuction being added.
//	 */
//	private void addAuction(Auction theAuction){
//		auctionDataBase.add(theAuction);
//	}
//
//	/**
//	 * Returns scheduled auctions.
//	 * @return an arraylist of auctions.
//	 */
//	private HashMap<Auction, HashMap<AuctionItem, Bid>> getAuctionDataBase() {
//		return auctionDataBase;
//	}
//
//	private ArrayList<Auction> getPastAuctions() {
//		ArrayList<Auction> pastAuctions = new ArrayList<Auction>();
//		for(Auction a: auctionDataBase) {
//			if(a.getAuctionDate().isBefore(LocalDate.now())) {
//				pastAuctions.add(a);
//			}
//		}
//		return pastAuctions;
//	}
//
//	private ArrayList<Auction> getActiveAuctions() {
//		ArrayList<Auction> activeAuctions = new ArrayList<Auction>();
//		if(null == auctionDataBase)
//			return null;
//
//		else {
//			for (Auction a : auctionDataBase) {
//				if (LocalDate.now().isBefore(a.getAuctionDate())) {
//					activeAuctions.add(a);
//				}
//			}
//		}
//		return activeAuctions;
//	}
//
//	/**
//	 * Allows the system to retrieve information on an auction by its ID.
//	 * @param auctionID the ID of the auction.
//	 * @return an auction associated with th parameter.
//	 */
//	private Auction getAuction(int auctionID){
//		Auction returnAuction = null;
//		for(Auction a: auctionDataBase){
//			if(a.getauctionID() == auctionID) {
//				returnAuction = a;
//				break;
//			}
//		}
//		return returnAuction;
//	}
//
//	//~~Updating Default Values~~//
//
//	/**
//	 * Updates the max number of upcoming auctions, provided the new max is greater
//	 * than the number of currently scheduled auctions.
//	 * @param theNewMax - new max number of auctions that may be scheduled.
//	 */
//	public void updateMaxUpcomingAuctions(int theNewMax){
//		if(theNewMax > getActiveAuctions().size())
//			MAX_UPCOMING_AUCTIONS = theNewMax;
//	}


}
