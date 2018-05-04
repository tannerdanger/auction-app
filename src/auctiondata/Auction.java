/*
 * TCSS 360 deliverable 2
 * Group 4
 */
package auctiondata;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import auctiondata.AuctionItem;

/**
 * An object representing an auction included in an calendar.
 * @author Wen Shu
 * @version May 4th 2018
 */
public class Auction {

	private LocalDate auctionDate;
	
	private Date auctionTime;
	
	private final String organizationName;
	
	private final String organizationID;

	private Map<String, AuctionItem> inventorySheet = new HashMap<String, AuctionItem>();
	
	/**
	 * A private constructor to prevent the default constructor to create an null auction.
	 * User has to provide all item info to create the auction class object.
	 */
	private Auction(){
		organizationName = null;
		organizationID = null;
	}
	
	public Auction(final String theOrganizationName,
				   final String theOranizationID,
				   final LocalDate theAuctionDate,
				   final Date theAuctionTime,
				   Map<String, AuctionItem> theInventorySheet) {
		organizationName = theOrganizationName;
		organizationID = theOranizationID;
		auctionDate = theAuctionDate;
		auctionTime = theAuctionTime;
		inventorySheet = theInventorySheet;
	}
	
	public void addInventoryItem(final AuctionItem theItem) {
		inventorySheet.put(theItem.getUniqueID(), theItem);
	}
	
	public void removeInventoryItem(final String theUniqueID) {
		inventorySheet.remove(theUniqueID);
	}
	
	public void setAuctionDate(LocalDate auctionDate) {
		this.auctionDate = auctionDate;
	}
	
	@SuppressWarnings("deprecation")
	public void setAuctionTime(int theHours) {
		auctionTime.setHours(theHours);
	}

	public LocalDate getAuctionDate() {
		return auctionDate;
	}

	@SuppressWarnings("deprecation")
	public int getAuctionTime() {
		return auctionTime.getHours();
	}
	
	public Map<String, AuctionItem> getInventorySheet(){
		return inventorySheet;
	}

}