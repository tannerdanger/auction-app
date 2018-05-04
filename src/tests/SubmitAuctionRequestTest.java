package tests;

import static org.junit.Assert.*;
import java.time.LocalDate;
import org.junit.Test;
import auctiondata.Auction;
import auctiondata.Scheduler;

/**
 * JUnit 5 test class that deals with submitting an auction request form.
 * This test class will check if the requested auction has a date that has
 * elapsed a year since the last requester's last auction.
 *
 * @Author David Perez
 * @version 5/3/2018
 */


public class SubmitAuctionRequestTest {

	private static final Scheduler myScheduler = new Scheduler();
	//The following tests business rule 7e: No auction can be scheduled
	// less than a set # of days from the current date, default of 14.
	@Test
	public void isThereNoPriorAuction_noPriorAuction_True() {
		/**
		 * Tests to ensure that an auction date is valid for request if
		 * the contact person has no previous auction or current auction.
		 */
		Auction priorAuction = null;
		Auction newAuction = null;
		assertTrue(Scheduler.isThereNoPriorAuction(priorAuction, newAuction));
	}

	@Test
	public void isRequiredTimeElapsedBetweenPriorAndNewAuctionMet_priorAuctionDateLessThanOneYear_False() {
		/**
		 *  Tests to ensure that an auction date is invalid for request if
		 * the contact person's previous action occurred less than one year ago.
		 */

		LocalDate priorDate = LocalDate.of(2017, 05,26);
		LocalDate newDate = LocalDate.of(2018, 05, 25);
		Auction priorAuction = new Auction();
		priorAuction.setAuctionDate(priorDate);
		
		assertFalse(Scheduler.isRequiredTimeElapsedBetweenPriorAndNewAuctionMet(priorAuction, 
																				newDate));
	}

	@Test
	public void  isRequiredTimeElapsedBetweenPriorAndNewAuctionMet_priorAuctionDateEqualToOneYear_True() {
		/**
		 *  Tests to ensure that an auction date is valid for request if
		 * the contact person's previous action occurred exactly one year ago.
		 */
		
		LocalDate priorDate = LocalDate.of(2017, 05,25);
		LocalDate newDate = LocalDate.of(2018, 05, 25);
		
		Auction priorAuction = new Auction();
		priorAuction.setAuctionDate(priorDate);
		
		assertTrue(Scheduler.isRequiredTimeElapsedBetweenPriorAndNewAuctionMet(priorAuction, 
																				newDate));
	}
	
	@Test
	public void isRequiredTimeElapsedBetweenPriorAndNewAuctionMet_priorAuctionDateIsOneYearAndOneDay_True() {
		/**
		 *  Tests to ensure that an auction date is valid for request if
		 * the contact person's previous action occurred exactly one year plus one day ago.
		 */
		LocalDate priorDate = LocalDate.of(2017, 05,26);
		LocalDate newDate = LocalDate.of(2018, 05, 26);
		Auction priorAuction = new Auction();
		priorAuction.setAuctionDate(priorDate);
		
		assertTrue(Scheduler.isRequiredTimeElapsedBetweenPriorAndNewAuctionMet(priorAuction, 
																				newDate));
	}
	
	

}