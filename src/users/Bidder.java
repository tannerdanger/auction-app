package users;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import auctiondata.Auction;
import auctiondata.AuctionItem;
import auctiondata.Bid;


/**
 * @author Tanner Brown, Charles Grumer
 * @version 05/05/18
 * Bidder class, saves relevent information to a bidder
 */
public class Bidder extends User implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 2136532391266767810L;
    public static final int MAX_BIDS_TOTAL = 12;
    public static final int MAX_BIDS_PER_AUCTION = 8;

    //TODO: Define values associated with a bidder. i.e. an array of auctions that bids have been placed in?
    //And/or an array of auction items that have been bid on?
    private final List<Bid> myBids;
    private final String myID;
    private HashMap<Auction, Integer> myBidsPerAuction;

    public Bidder(String theFirst, String theLast, String theEmail) {
        super(theFirst, theLast, theEmail); //pass basic ID values to User superclass
        myBids = new ArrayList<>();
        myID = theEmail;
        myBidsPerAuction = new HashMap<>();
    } 

    public boolean isBidPlaceable(final Auction theAuction, final AuctionItem theItem, final BigDecimal theBidAmount) {
    	return isDateValid(theAuction.getAuctionDate())
    		   && isBelowMaxBidsPerAuction(theAuction) 
    		   && isBelowMaxTotalBids();
    }
       
    public static boolean isDateValid(LocalDate theAuctionDate) {
        return LocalDate.now().isBefore(theAuctionDate);
    }

    public boolean isBelowMaxBidsPerAuction(final Auction theAuction) {
    	return myBidsPerAuction.containsKey(theAuction)
    		   && MAX_BIDS_PER_AUCTION < myBidsPerAuction.get(theAuction);
    }
    
    public boolean isBelowMaxTotalBids() {
    	return myBids.size() < MAX_BIDS_TOTAL;
    }
    
    public void placeBid(Bid theBid){
    	myBids.add(theBid);
    }

    public boolean placeBid(final Auction theAuction, final BigDecimal theBidAmount, final AuctionItem theItem) {
	  final boolean result = isBidPlaceable(theAuction, theItem, theBidAmount) && Bid.isBidValid(theBidAmount);
	  if(result) {
		  final Bid bid = new Bid(theAuction, theBidAmount, theItem);
		  myBids.add(bid);
		  theItem.addSealedBids(myID, theBidAmount);
		  myBidsPerAuction.putIfAbsent(theAuction, 0);
		  myBidsPerAuction.put(theAuction, myBidsPerAuction.get(theAuction) + 1);
	  }
	  return result;
	}
  
    public List<Bid> getBids() {
      return new ArrayList<Bid>(myBids);
  }

    public String getMyID(){
    	return myID;
    }
  
}
