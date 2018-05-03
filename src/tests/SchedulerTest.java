package tests;

import auctiondata.Scheduler;
import org.junit.*;
import java.time.LocalDate;
import static org.junit.Assert.*;

/**
 * A JUnit4 class to test the Scheduler class to ensure the date
 * checks for new auctions work properly.
 *
 * @Author Tanner Brown
 */
public class SchedulerTest {




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
				LocalDate.now().plusDays(15)));
	}

	@Test
	public void isMinDaysOut_equalToMin_True() {
		/**
		 * Tests to ensure that an auction can be scheduled
		 * if it is exactly the min days from current date.
		 * (Min = 14)
		 */
		assertTrue(Scheduler.isMinDaysOut(
				LocalDate.now().plusDays(14)));
	}

	@Test
	public void isMinDaysOut_lessThanMin_False() {
		/**
		 * Tests to ensure that an auction can NOT be scheduled
		 * if it is the min-1 days from current date.
		 * (Min = 14)
		 */
		assertFalse(Scheduler.isMinDaysOut(
				LocalDate.now().plusDays(13)));
	}

}