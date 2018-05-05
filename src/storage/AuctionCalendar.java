package storage;

import auctiondata.Auction;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A serializable class that stores all auction data
 */
public class AuctionCalendar implements Serializable {

	private static final long serialVersionUID = 18675309L;

	public static ArrayList<Auction> getActiveAuctions() {
		return activeAuctions;
	}

	private static ArrayList<Auction> activeAuctions = new ArrayList<>();
	//TODO: an array of PAST auctions? (not in current requrements)

	//TODO: an array of currently scheduled auctions (next 30 days)
	public void printActiveAuctions() {
		for(Auction a : activeAuctions){
			System.out.println(a.toString());
		}
	}

	public void addAuction(Auction theAuction){

		activeAuctions.add(theAuction);

	}

}
