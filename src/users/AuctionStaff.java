package users;


import storage.AuctionCalendar;
import storage.DataHandler;

import javax.swing.*;
import java.io.Serializable;

/**
 * This class creates a basic login for an Auction Central admin.
 * Most functionality is done through the calendar, this class mainly
 * exists to ensure only a staff member accesses some methods within the calendar.
 */
public class AuctionStaff extends User implements Serializable {

	private AuctionCalendar myCalendar;

	public AuctionStaff(String theFirst, String theLast, String theEmail, AuctionCalendar theCalendar){
		super(theFirst, theLast, theEmail); //pass basic ID values to User superclass
		myCalendar = theCalendar;
	}


	public void updateMaxAuctionsDay(String theResponse) {

			int newMax = Integer.parseInt(theResponse);
			myCalendar.setMAX_AUCTIONS_DAY(newMax);

	}

	public void updateNewMaxAuctions(String response) {

		int newMax = Integer.parseInt(response);
		myCalendar.setMAX_UPCOMING_AUCTIONS(newMax);

	}

	public void cancelAuction(String response, DataHandler theData) {
		int auctionID = Integer.parseInt(response);
		theData.cancelAuction(auctionID);
	}
}
