package users;

import java.io.Serializable;

/**
 * User class, saves relevent information to both bidders and contacts,
 * i.e. contact information
 */
public class User implements Serializable{


	private String firstName;
	private String lastName;
	private String email;
	//etc...
	public User(String theFirst, String theLast, String theEmail){
		firstName = theFirst;
		lastName = theLast;
		email = theEmail;
	}


	//getters / setters
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String theFirstName) {
		this.firstName = theFirstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String theLastName) {
		this.lastName = theLastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String theEmail) {
		this.email = theEmail;
	}

	//View all items I have bid on
	public void printAllPlacedBids() {


		//Todo: Print out placed bids (all items). Return response covered in console logic
		//Also, the print statement can be adjusted.
		System.out.println("Print all placed bids\n\n0. Return");

	}

	//View all auctions I have placed bids on
	public void printAllMyBidAuctions() {

		//Todo: Print out all auctions that have been bid in (all auctions I have placed bids in).
		// Return response covered in console logic. Also, the print statement can be adjusted.
		System.out.println("Print all auctiosn I have placed bids in \n\n0. Return");

	}
	//view all of my bids on a single auction
	public void printBidsInAnAuction() {
		//prompt user for which auction they want to view bids on
		//display all items for that auction
		//TODO: Prompt user for which auction, and then call getAuction from the calendar with that auction ID
		System.out.println("Print all bids in a specific Auction \n\n0. Return");
	}
}
