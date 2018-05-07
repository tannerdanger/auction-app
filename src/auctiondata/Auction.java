/*
 * TCSS 360 deliverable 2
 * Group 4
 */
package auctiondata;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
	
	private LocalTime auctionTime;
	
	private final String organizationName;
	
	//private final String organizationID;
    private final int organizationID;

    private int auctionID;

	private Map<Integer, AuctionItem> inventorySheet = new HashMap<Integer, AuctionItem>();
	
	/**
	 * A private constructor to prevent the default constructor to create an null auction.
	 * User has to provide all item info to create the auction class object.
	 */
	private Auction(){
		organizationName = null;
		organizationID = -1;
	}


	
	public Auction(final String theOrganizationName,
				   int theOranizationID,// make orgID a hash value of the orgName?
				   final LocalDateTime theAuctionDate,
				   //final LocalDateTime theAuctionTime,
				   Map<Integer, AuctionItem> theInventorySheet) {
		organizationName = theOrganizationName;
		organizationID = theOranizationID;
		auctionDate = theAuctionDate.toLocalDate();
		auctionTime = theAuctionDate.toLocalTime();
		if(null != theInventorySheet)
		    inventorySheet = theInventorySheet;

		auctionID = createUniqueID();
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


	public void setAuctionTime(String theHours) {
	    this.auctionTime = LocalTime.parse(theHours);
		//auctionTime(theHours);
	}

	public LocalDate getAuctionDate() {
		return auctionDate;
	}

	@SuppressWarnings("deprecation")
	public LocalTime getAuctionTime() {
		return auctionTime;
	}
	
	public Map<Integer, AuctionItem> getInventorySheet(){
		return inventorySheet;
	}

	public void printInventorySheet(){
	    int i = 1;
		for (AuctionItem item : inventorySheet.values()) {
		    System.out.print(i + ": ");
			System.out.println(item.toString());
			i++;
		}
	}

    private int createUniqueID(){
	    int id = Math.abs(organizationID + auctionDate.hashCode());
	    if(id <= 10000)
	        id /= 2;
	    else
	        id %= 10000;
	    return id;
    }

    public int getauctionID(){
	    return this.auctionID;
    }

    @Override
    public String toString(){
	    return "| ID: " + String.valueOf(this.auctionID) + " | ORG: " +organizationName+ " | DATE: " + this.auctionDate.toString()+ " |";
    }
}