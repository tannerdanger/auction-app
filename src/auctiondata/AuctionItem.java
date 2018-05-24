/*
 * TCSS 360 deliverable 2
 * Group 4
 */
package auctiondata;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


/**
 * An object representing an item included in an auction
 * @author Wen Shu
 * @version May 4th 2018
 */
public class AuctionItem implements Serializable {

    private static final long serialVersionUID = 9212387915390331L;
	private BigDecimal minPrice;
	
	private String name;
	
	private final int uniqueID;
	
	private Map<String, BigDecimal> sealedBids = new HashMap<String, BigDecimal>();


	/**
	 * A private constructor to prevent the default constructor to create an null auction item.
	 * User has to provide all item info to create the auction item class object.
	 */
	private AuctionItem() {
		uniqueID = -1;
		minPrice = null;
		name = null;
	}
	
	public AuctionItem(final double theMinPrice, String theName) {
		minPrice = new BigDecimal(theMinPrice);
		name = theName;
		uniqueID = createID();
	}

	private int createID() {
		int id = Math.abs(name.hashCode() + minPrice.hashCode());

		if(id <= 1000)
			id /= 2;
		else
			id %= 1000;

		return id;
	}

	public void setName(String theName) {
		name = theName;
	}
	
	public void setMinPrice(double thePrice) {
		minPrice = new BigDecimal(thePrice);
	}
	
	public String getName() {
		return name;
	}
	
	public double getMinPrice() {
		return minPrice.doubleValue();
	}
	
	public int getUniqueID() {
		return uniqueID;
	}
	
	public void addSealedBids(String theBidderID, BigDecimal theSealedBid) {
		sealedBids.put(theBidderID, theSealedBid);
	}
	
	public Map<String, BigDecimal> getSealedBids(){
		return sealedBids;
	}

	@Override
	public String toString(){
		return "ID: " + String.valueOf(this.uniqueID) + " | NAME: " + this.name;
	}
}