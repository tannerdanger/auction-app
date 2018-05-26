/**
 *
 */
package tests;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import auctiondata.Auction;
import auctiondata.AuctionItem;
import users.Bidder;

/**
 * @author wen
 * @version May 7th 2018
 */
public class BidderTest {

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
		item = new AuctionItem(MIN_PRICE, "answers for TCSS 360 final", auction );
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
}
