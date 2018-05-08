package auctiondata;

import storage.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * A helper class for determining if auctions fall into
 * acceptable date ranges.
 *
 * @author Tanner Brown, Charlie Grumer, David Perez
 * 
 */
public class Scheduler {

	private static final int MAX_DAYS_OUT = 60;
	private static final long REQUIRED_YEARS_IN_BETWEEN_AUCTION = 1;
	private static final long REQUIRED_MONTHS_IN_BETWEEN_AUCTION = 0;
	private static final long REQUIRED_DAYS_IN_BETWEEN_AUCTION = 0;
	private static final int MAX_UPCOMING_AUCTIONS_LIMIT = 25;
	private static final int MAX_DAILY_AUCTION_CAPACITY = 2;
	DataHandler myData;

	public Scheduler(DataHandler theData){
		myData = theData;
	}

	/**
	 * Checks to ensure auction is more than 13 days from today
	 * @param theAuctionDate the requested date of auction
	 * @return true if date is valid
	 */
	public static boolean isMinDaysOut(LocalDateTime theAuctionDate){
		return theAuctionDate.isAfter(LocalDateTime.now().plusDays(13));
	}

	//I'm not entirely sure, but instead of using AuctionCalender.get....
	//It will instead need a reference to a calender object we create somewhere
	//that holds all the auctions.
	public boolean isMaxDailyAuctionsExceeded(LocalDateTime auctionRequestDate){

		ArrayList<Auction> auctions = myData.getMyAuctionCalendar().getActiveAuctions();

		int auctionCount = 0;
		for (Auction a : auctions) {
			if (a.getAuctionDate().equals(auctionRequestDate))
				auctionCount++;
		}
		return auctionCount >= MAX_DAILY_AUCTION_CAPACITY;
	}

	public static boolean isMaxDaysOutExceeded(LocalDateTime theAuctionDate){

		//Auction is scheduled for less than 60 days from now
		return theAuctionDate.isBefore(LocalDateTime.now().plusDays(61));
	}
	
	public static boolean isAuctionDateLessThanEqualToMaxDaysOut(final LocalDateTime theAuctionDate) {
		return theAuctionDate.isBefore(LocalDateTime.now().plusDays(MAX_DAYS_OUT + 1));
	}
	
	public static boolean isRequiredTimeElapsedBetweenPriorAndNewAuctionMet(final Auction thePriorAuction,
			final LocalDate theNewAuctionDate) {
		
		LocalDate checkRequiredTimePassDate = 
		thePriorAuction.getAuctionDate().plusYears(REQUIRED_YEARS_IN_BETWEEN_AUCTION
		).plusMonths(REQUIRED_MONTHS_IN_BETWEEN_AUCTION
		).plusDays(REQUIRED_DAYS_IN_BETWEEN_AUCTION);
		return checkRequiredTimePassDate.isBefore(theNewAuctionDate) 
				|| checkRequiredTimePassDate.equals(theNewAuctionDate);

	}
	
	public static boolean isThereNoPriorAuction (final Auction thePriorAuction) {
		boolean flag = false;
		if (thePriorAuction == null) {
			flag = true;
		}
		return flag;
	}
	
	//I'm not entirely sure, but instead of using AuctionCalender.get....
	//It will instead need a reference to a calender object we create somewhere
	//that holds all the auctions.
	public boolean isMaxUpcomingAuctionsExceeded() {
		return myData.getMyAuctionCalendar().getActiveAuctions().size()
				>= MAX_UPCOMING_AUCTIONS_LIMIT;
	}
	
	/**
	 * This method goes through several checks that see if we can turn an
	 * auction request into an actual auction.
	 * @param thePriorAuction contact person's previous auction
	 * @param theCurrentAuction contact person's current auction
	 * @param theNewDate the contact person's suggested date for their new auction
	 * @return true if we can turn this request into an auction, false otherwise.
	 */
	public boolean isAuctionRequestValid(Auction thePriorAuction, LocalDateTime theNewDate) {
		
		boolean flag = isThereNoPriorAuction(thePriorAuction);
		
		if (!flag) {
			flag = isRequiredTimeElapsedBetweenPriorAndNewAuctionMet(thePriorAuction,
					theNewDate.toLocalDate()) && !isMaxDailyAuctionsExceeded(theNewDate) && !isMaxUpcomingAuctionsExceeded() &&
					isMinDaysOut(theNewDate) && isMaxDaysOutExceeded(theNewDate);
		}
		return flag;
	}
}
