package users;

/**
 * Bidder class, saves relevent information to a bidder
 */
public class Bidder extends User {

	//TODO: Define values associated with a bidder. i.e. an array of auctions that bids have been placed in?
	//And/or an array of auction items that have been bid on?

	public Bidder(String theFirst, String theLast, String theEmail) {
		super(theFirst, theLast, theEmail); //pass basic ID values to User superclass
	}
}
