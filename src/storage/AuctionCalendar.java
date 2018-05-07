package storage;

import auctiondata.Auction;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A serializable class that stores all auction data
 */
public class AuctionCalendar implements Serializable {

	private static final long serialVersionUID = 18675309L;

	public ArrayList<Auction> getActiveAuctions() {
		return activeAuctions;
	}

	private ArrayList<Auction> activeAuctions = new ArrayList<>();
	//TODO: an array of PAST auctions? (not in current requrements)

	public void printActiveAuctions() {
		for(Auction a : activeAuctions){
			System.out.println(a.toString());
		}
	}

	public void addAuction(Auction theAuction){

		activeAuctions.add(theAuction);

	}
	public Auction getAuction(int auctionID){
		Auction returnAuction = null;
		for(Auction a: activeAuctions){
			if(a.getauctionID() == auctionID) {
				returnAuction = a;
				break;
			}
		}
		return returnAuction;
	}

}
