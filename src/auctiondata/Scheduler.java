package auctiondata;

import users.User;
import storage.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * A helper class for determining if auctions fall into
 * acceptable date ranges.
 *
 * @author Tanner Brown
 */
public class Scheduler {



	/**
	 * Checks to ensure auction is more than 13 days from today
	 * @param theAuctionDate the requested date of auction
	 * @return true if date is valid
	 */
	public static boolean isMinDaysOut(LocalDate theAuctionDate){

		return theAuctionDate.isAfter(LocalDate.now().plusDays(13));
	}

	public static boolean isYearSinceLastAuction(User theUser){


		return false;

	}

	public static boolean isMaxDailyAuctionsExceeded(LocalDateTime auctionRequestDate){

		ArrayList<Auction> auctions = AuctionCalendar.getActiveAuctions();

		int auctionCount = 0;
		for (Auction a : auctions) {
			if (a.getAuctionDate().equals(auctionRequestDate))
				auctionCount++;
		}

		return auctionCount < 2;
	}

	public static boolean isMaxDaysOutExceeded(LocalDate theAuctionDate){

		//Auction is scheduled for less than 60 days from now
		return theAuctionDate.isBefore(LocalDate.now().plusDays(61));
	}
}
