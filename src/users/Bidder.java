package users;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
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

    //TODO: Define values associated with a bidder. i.e. an array of auctions that bids have been placed in?
    //And/or an array of auction items that have been bid on?
    private final List<Bid> myBids;
    private final String myID;
    private int myBidCounter;

    public Bidder(String theFirst, String theLast, String theEmail) {
        super(theFirst, theLast, theEmail); //pass basic ID values to User superclass
        myBids = new ArrayList<>();
        myID = theEmail;
        myBidCounter = 0;
    } 


    public List<Bid> getBids() {
        return new ArrayList<Bid>(myBids);
    }
    

    public boolean placeBid(final Auction theAuction, final BigDecimal theBidAmount, final AuctionItem theItem) {
        //Todo: Prompt user for info for a bid to place, then try to create a new Bid object and add it to the bidder's bid array
        final Bid b = new Bid(theAuction, theBidAmount, theItem);
        boolean result = false;
        if (b.isBidPlaced() && (myBidCounter < 4)) {
            myBids.add(b);
            theItem.addSealedBids(myID, theBidAmount);
            myBidCounter++;
            result = true;
        }
        return result;
    }
}
