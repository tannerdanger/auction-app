/**
 * 
 */
package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import auctiondata.Auction;
import auctiondata.AuctionItem;
import storage.AuctionCalendar;
import storage.DataHandler;
import users.AuctionStaff;

/**
 * @author Wen, David
 *
 */
public class AuctionStaffTest {
	private AuctionStaff staff;
	private AuctionStaff staffTwo;
	private AuctionCalendar calendar;
	private AuctionCalendar calendarTwo;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		calendar = new AuctionCalendar();
		staff = new AuctionStaff("wem", "shu", "wenshu@staff.com", calendar);
		
		DataHandler newHandler = new DataHandler();
		calendarTwo = newHandler.getMyAuctionCalendar();
		staffTwo = new AuctionStaff("David", "Perez", "daperez@staff.com", calendarTwo);
	}

	@Test
	public void updateNewMaxAuctions_nonPositive_exception() {
		staff.updateNewMaxAuctions("-1");
		assertFalse(calendar.getMAX_UPCOMING_AUCTIONS() == -1);
	}
	
	@Test
	public void updateNewMaxAuctions_positive_true() {
		staff.updateNewMaxAuctions("26");
		assertTrue(calendar.getMAX_UPCOMING_AUCTIONS() == 26);
	}
	
	@Test
	public void updateNewMaxAuctions_greaterThanExsitingNumberOfAuctions_true() {
		int temp = calendar.getActiveAuctions().size();
		staff.updateNewMaxAuctions("" + temp + 30);
		assertTrue(calendar.getMAX_UPCOMING_AUCTIONS() == temp + 30);
	}
	
	@Test
	public void cancelAuction_noBid_True() {
		int oldSize = calendarTwo.getActiveAuctions().size();
		Auction auctionToRemove = calendarTwo.getActiveAuctions().get(0);
		staffTwo.cancelAuction("3384");

		assertTrue((calendarTwo.getActiveAuctions().size() < oldSize) && auctionToRemove.getauctionID() == 3384);
	}
	
	@Test
	public void cancelAuction_oneBid_False() {
		int oldSize = calendarTwo.getActiveAuctions().size();
		staffTwo.cancelAuction("6531");
		assertFalse((calendarTwo.getActiveAuctions().size() < oldSize));
	}
	@Test
	public void cancelAuction_manyBids_False() {
		int oldSize = calendarTwo.getActiveAuctions().size();

		System.out.println(calendarTwo.getActiveAuctions());
		staffTwo.cancelAuction("1481");
		assertFalse((calendarTwo.getActiveAuctions().size() < oldSize));
	}
}
