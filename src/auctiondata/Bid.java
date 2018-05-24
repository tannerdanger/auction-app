package auctiondata;

import users.Bidder;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * An object representing a user's bid
 * 
 * @author Charlie Grumer
 * @version 05/07/2018
 */
public class Bid implements Serializable {
	private static final long serialversionUID = 129348938L;
	
	private Auction whichAuction;
	private AuctionItem item;
	private BigDecimal sealedBidAmount;
	
	public Bid(final Auction theAuction, final BigDecimal theBidAmount, final AuctionItem theItem) {
		whichAuction = theAuction;
		sealedBidAmount = theBidAmount;
		item = theItem;
	}
	
	private boolean checkBid() {
		return sealedBidAmount.doubleValue() >= item.getMinPrice();
	}
	
	public Auction getAuction(){
		return this.whichAuction;
	}

	public double getBidAmount(){
		return sealedBidAmount.doubleValue();
	}

	public AuctionItem getItem() {
		return item;
	}
}
