package tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import auctiondata.Auction;
import auctiondata.AuctionItem;
import users.Bidder;
/**
 * JUnit 4 test class that checks if requested auction date
 * is too far away.
 * 
 * @Author Wen Shu, Charlie Grumer
 * @version 05/07/2018
 */
public class AuctionDate {

    private static final double MIN_PRICE = 99;
    private static final int AUCTIONID = 12345;
    Bidder bidder;
    Auction auction;
    AuctionItem item;
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        bidder = new Bidder("wen", "shu", "bidder_wen@bidder.com");

        auction = new Auction("UWTacoma", AUCTIONID, LocalDateTime.of(2018, 05, 25, 12, 00), null);
	    item = new AuctionItem(MIN_PRICE, "answers for TCSS 360 final", auction);
        auction.addInventoryItem(item);
    }

    @Test
    public void placeBid_bidIsGreaterThanMinPrice_True() {
        assertTrue(bidder.placeBid(auction, new BigDecimal(MIN_PRICE + 1), item));
    }

    @Test
    public void placeBid_bidIsEqualtoMinPrice_True() {
        assertTrue(bidder.placeBid(auction, new BigDecimal(MIN_PRICE), item));
    }

    @Test
    public void placeBid_bidIsLessThanMinPrice_False() {
        assertFalse(bidder.placeBid(auction, new BigDecimal(MIN_PRICE - 1), item));
    }
    
    @Test
    public void placeBid_DateBeforeAuction_true() {
    	assertTrue(bidder.isDateValid(LocalDate.now().plusDays(1)));
    }
    @Test
    public void placeBid_DateOfAuction_false() {
    	assertFalse(bidder.isDateValid(LocalDate.now()));

    }
    @Test
    public void placeBid_DateAfterAuction_false() {
    	assertFalse(bidder.isDateValid(LocalDate.now().minusDays(1)));

    }
		
	@Test
	public void placebid_LessThanMaxAllowedBids_True() {
		for (int i = 0; i < 9; i++) {
	        auction = new Auction("UWTacoma", i, LocalDateTime.of(2018, 05, 25, 12, 00), null);
	    	bidder.placeBid(auction, new BigDecimal(MIN_PRICE + 1), item);
		}
		
	    assertTrue(bidder.placeBid(auction, new BigDecimal(MIN_PRICE), item));
		
	}
	@Test
	public void placebid_AtMaxAllowedBids_False() {
		for (int i = 0; i < 10; i++) {
	        auction = new Auction("UWTacoma", i, LocalDateTime.of(2018, 05, 25, 12, 00), null);
	    	bidder.placeBid(auction, new BigDecimal(MIN_PRICE + 1), item);
		}
			    	
	    assertFalse(bidder.placeBid(auction, new BigDecimal(MIN_PRICE), item));
	}
}


