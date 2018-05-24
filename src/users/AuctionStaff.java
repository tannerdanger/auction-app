package users;


import storage.AuctionCalendar;
import java.io.Serializable;

/**
 * This class creates a basic login for an Auction Central admin.
 * Most functionality is done through the calendar, this class mainly
 * exists to ensure only a staff member accesses some methods within the calendar.
 */
public class AuctionStaff extends User implements Serializable {

	private AuctionCalendar myCalendar;

	public AuctionStaff(String theFirst, String theLast, String theEmail){
		super(theFirst, theLast, theEmail); //pass basic ID values to User superclass
	}


}
