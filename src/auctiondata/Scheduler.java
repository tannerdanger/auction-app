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
 * @version 05/07/2018
 */
public class Scheduler {

	private static final int MAX_DAYS_OUT = 60;

	private static int MAX_UPCOMING_AUCTIONS_LIMIT = 25;
	private static final int MAX_DAILY_AUCTION_CAPACITY = 2;
	private static final int MIN_DAYS_OUT = 14;
	
	private Scheduler(){
	}

	/**
	 * Checks to ensure auction is more than min days from today
	 * @param theAuctionDate the requested date of auction
	 * @return true if date is valid
	 */
	public static boolean isMinDaysOut(LocalDateTime theAuctionDate){
		return theAuctionDate.isAfter(LocalDateTime.now().plusDays(MIN_DAYS_OUT - 1));
	}

	//I'm not entirely sure, but instead of using AuctionCalender.get....
	//It will instead need a reference to a calender object we create somewhere
	//that holds all the auctions.
	public static boolean isMaxDailyAuctionsExceeded(LocalDateTime auctionRequestDate, ArrayList<Auction> theAuctions){

		int auctionCount = 0;
		for (Auction a : theAuctions) {
			if (a.getAuctionDate().isEqual(auctionRequestDate.toLocalDate()))
				auctionCount++;
		}
		return auctionCount >= MAX_DAILY_AUCTION_CAPACITY;
	}

	public static boolean isMaxDaysOutExceeded(LocalDateTime theAuctionDate){

		//Auction is scheduled for less than 60 days from now
		return theAuctionDate.isBefore(LocalDateTime.now().plusDays(MAX_DAYS_OUT + 1));
	}
	
	public static int getMaxDaysOut() {
		return MAX_DAYS_OUT;
	}
	
	public static int getMinDaysOut() {
		return MIN_DAYS_OUT;
	}
	
	public void setMaxUpcomingAuctionsLimit(int newNumber) {
		MAX_UPCOMING_AUCTIONS_LIMIT = newNumber;
	}

	public static boolean isMaxUpcomingAuctionsExceeded(int calendarSize) {
		return calendarSize >= MAX_UPCOMING_AUCTIONS_LIMIT;
	}
	
	/**
	 * This method goes through several checks that see if we can turn an
	 * auction request into an actual auction.
	 * @param thePriorAuction contact person's previous auction
	 * @param theCurrentAuction contact person's current auction
	 * @param theNewDate the contact person's suggested date for their new auction
	 * @return true if we can turn this request into an auction, false otherwise.
	 */
	public static boolean isAuctionRequestValid(LocalDateTime theNewDate, ArrayList<Auction> theAuctions,
			int theCalendarSize) {
		
		boolean flag = false;
		if (!flag) {
			flag = !isMaxDailyAuctionsExceeded(theNewDate, theAuctions) && !isMaxUpcomingAuctionsExceeded(theCalendarSize) &&
					isMinDaysOut(theNewDate) && isMaxDaysOutExceeded(theNewDate);
		}
		return flag;
	}
}
