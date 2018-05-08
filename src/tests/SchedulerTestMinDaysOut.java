package tests;

import auctiondata.Scheduler;
import org.junit.*;
import java.time.LocalDateTime;
import static org.junit.Assert.*;

/**
 * A JUnit4 class to test the Scheduler class to ensure the date
 * checks for new auctions work properly.
 *
 * @Author Tanner Brown
 */
public class SchedulerTestMinDaysOut {

	//The following tests business rule 7e: No auction can be scheduled
	// less than a set # of days from the current date, default of 14.
	@Test
	public void isMinDaysOut_moreThanMin_True() {
		/**
		 * Tests to ensure that an auction can be scheduled
		 * if it is the min+1 days from current date.
		 */
		assertTrue(Scheduler.isMinDaysOut(
				LocalDateTime.now().plusDays(Scheduler.getMinDaysOut() + 1)));
	}

	@Test
	public void isMinDaysOut_equalToMin_True() {
		/**
		 * Tests to ensure that an auction can be scheduled
		 * if it is exactly the min days from current date.
		 */
		assertTrue(Scheduler.isMinDaysOut(
				LocalDateTime.now().plusDays(Scheduler.getMinDaysOut())));
	}

	@Test
	public void isMinDaysOut_lessThanMin_False() {
		/**
		 * Tests to ensure that an auction can NOT be scheduled
		 * if it is the min-1 days from current date.
		 */
		assertFalse(Scheduler.isMinDaysOut(
				LocalDateTime.now().plusDays(Scheduler.getMinDaysOut() - 1)));
	}

}