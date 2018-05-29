/**
 * 
 */
package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import storage.AuctionCalendar;
import users.AuctionStaff;

/**
 * @author wen
 *
 */
public class AuctionStaffTest {
	private AuctionStaff staff;
	private AuctionCalendar calendar;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		calendar = new AuctionCalendar();
		staff = new AuctionStaff("wem", "shu", "wenshu@staff.com", calendar);
	}

	@Test(expected = IllegalArgumentException.class)
	public void updateNewMaxAuctions_nonPositive_exception() {
		staff.updateNewMaxAuctions("-1");
	}
	
	@Test
	public void updateNewMaxAuctions_positive_true() {
		staff.updateNewMaxAuctions("1");
	}
	
	@Test
	public void updateNewMaxAuctions_greaterThanExsitingNumberOfAuctions_true() {
		int temp = calendar.getActiveAuctions().size();
		staff.updateNewMaxAuctions("" + temp);
	}

}
