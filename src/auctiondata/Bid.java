package auctiondata;

import java.math.BigDecimal;

/**
 * An object representing a user's bid
 */
public class Bid {
	
	final Auction whichAuction;
	final AuctionItem item;
	final BigDecimal sealedBidAmount;
	final Boolean placedBid;
	
	public Bid(final Auction theAuction, final BigDecimal theBidAmount, final AuctionItem theItem) {
		whichAuction = theAuction;
		sealedBidAmount = theBidAmount;
		item = theItem;
		placedBid = checkBid();
	}
	
	private boolean checkBid() {
		return sealedBidAmount.doubleValue() >= item.getMinPrice();
	}
	
	public boolean isBidPlaced() {
		return placedBid;
	}
}
