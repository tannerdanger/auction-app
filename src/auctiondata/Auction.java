package auctiondata;

import java.time.LocalDate;
import java.util.Date;

/**
 * An object representing an auction
 */
public class Auction {

	LocalDate auctionDate;


	public Auction(){

	}


	public LocalDate getAuctionDate() {
		return auctionDate;
	}

	public void setAuctionDate(LocalDate auctionDate) {
		this.auctionDate = auctionDate;
	}

}
