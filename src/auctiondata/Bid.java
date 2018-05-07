package auctiondata;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * An object representing a user's bid
 */
public class Bid implements Serializable {
	private static final long serialversionUID = 129348938L;
	
	private Auction whichAuction;
	private AuctionItem item;
	private BigDecimal sealedBidAmount;
	private Boolean placedBid;
	
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

	public Auction getAuction(){
		return this.whichAuction;
	}

	//TODO: ToString method
}
