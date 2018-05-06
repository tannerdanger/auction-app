package tests;

import auctiondata.Scheduler;
import org.junit.*;
import java.time.LocalDateTime;
import static org.junit.Assert.*;

/**
 * A JUnit4 class to test the Scheduler class to ensure the date
 * checks for new auctions work properly.
 *
 * @Author Tanner Brown, Gurchetan Singh
 */
public class SchedulerTest {

	
	// Tests to ensure that no more than allowed maximum
	// number of auctions can occur on the same day 
	/**
	 * Tests to ensure that an auction can be scheduled
	 * if there are no(zero) auctions scheduled for the date.
	 */
	@Test
	public void isMaxDailyAuctionsExceeded_NoAuctionsScheduled_True(){
		Date date = new Date(2018, 05, 02);
        assertTrue(Scheduler.isMaxDailyAuctionsExceeded(date));
	}
	
	/**
	 * Tests to ensure that an auction can be scheduled
	 * if there are less than maximum allowed auctions scheduled for the date.
	 */
	@Test
	public void isMaxDailyAuctionsExceeded_LessThanMaxAuctionsScheduled_True(){
		Date date = new Date(2018, 05, 02);
        assertTrue(Scheduler.isMaxDailyAuctionsExceeded(date));
	}
	
	/**
	 * Tests to ensure that an auction can not be scheduled
	 * if there are max auctions scheduled for the date.
	 */
	@Test
	public void isMaxDailyAuctionsExceeded_MaxAuctionsScheduled_False(){
		Date date = new Date(2018, 05, 02);
        assertFalse(Scheduler.isMaxDailyAuctionsExceeded(date));
	}



	//The following tests business rule 7e: No auction can be scheduled
	// less than a set # of days from the current date, default of 14.
	@Test
	public void isMinDaysOut_moreThanMin_True() {
		/**
		 * Tests to ensure that an auction can be scheduled
		 * if it is the min+1 days from current date.
		 * (Min = 14)
		 */
		assertTrue(Scheduler.isMinDaysOut(
				LocalDateTime.now().plusDays(15)));
	}

	@Test
	public void isMinDaysOut_equalToMin_True() {
		/**
		 * Tests to ensure that an auction can be scheduled
		 * if it is exactly the min days from current date.
		 * (Min = 14)
		 */
		assertTrue(Scheduler.isMinDaysOut(
				LocalDateTime.now().plusDays(14)));
	}

	@Test
	public void isMinDaysOut_lessThanMin_False() {
		/**
		 * Tests to ensure that an auction can NOT be scheduled
		 * if it is the min-1 days from current date.
		 * (Min = 14)
		 */
		assertFalse(Scheduler.isMinDaysOut(
				LocalDateTime.now().plusDays(13)));
	}

}