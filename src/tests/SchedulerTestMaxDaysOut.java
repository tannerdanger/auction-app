package tests;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Test;

import auctiondata.Scheduler;


/**
 * JUnit 4 test class that checks if requested auction date
 * is too far away.
 *
 * @Author Charlie Grumer
 * @version 05/07/2018
 */
public class SchedulerTestMaxDaysOut {
	
	@Test
	public void isMaxDaysOutExceeded_lessThanMaxDaysOut_True() {
		assertTrue(Scheduler.isMaxDaysOutExceeded
				  (LocalDateTime.now().plusDays(Scheduler.getMaxDaysOut() - 1)));
	}

	@Test
	public void isMaxDaysOutExceeded_equalToMaxDaysOut_True() {
		assertTrue(Scheduler.isMaxDaysOutExceeded
				  (LocalDateTime.now().plusDays(Scheduler.getMaxDaysOut())));
	}
	
	@Test
	public void isMaxDaysOutExceeded_greaterThanToMaxDaysOut_False() {
		assertFalse(Scheduler.isMaxDaysOutExceeded
				  (LocalDateTime.now().plusDays(Scheduler.getMaxDaysOut() + 1)));
	}
	
}
