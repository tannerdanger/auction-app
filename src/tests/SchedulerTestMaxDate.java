package tests;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Test;

import auctiondata.Scheduler;


/**
 * JUnit 4 test class that checks if requested auction date
 * is too far away.
 *
 * @Author Charlie Grumer
 */
public class SchedulerTestMaxDate {

	private static final int MAX_DAYS_OUT = 60;

	@Test
	public void isAuctionDateLessThanOrEqualToMaxDaysOut_lessThanMaxDaysOut_True() {
		assertTrue(Scheduler
				  .isAuctionDateLessThanEqualToMaxDaysOut
				  (LocalDate.now().plusDays(
				  MAX_DAYS_OUT - 1)));
	}

	@Test
	public void isAuctionDateLessThanOrEqualToMaxDaysOut_equalToMaxDaysOut_True() {
		assertTrue(Scheduler
				  .isAuctionDateLessThanEqualToMaxDaysOut
				  (LocalDate.now().plusDays(
				  MAX_DAYS_OUT)));
	}
	
	@Test
	public void isAuctionDateLessThanOrEqualToMaxDaysOut_greaterThanMaxDaysOut_False() {
		assertFalse(Scheduler
					.isAuctionDateLessThanEqualToMaxDaysOut
					(LocalDate.now().plusDays(
					MAX_DAYS_OUT + 1)));
	}
	
}
