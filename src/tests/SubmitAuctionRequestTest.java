package tests;

import static org.junit.Assert.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import auctiondata.Auction;
import auctiondata.AuctionItem;
import auctiondata.Scheduler;
import storage.AuctionCalendar;
import storage.DataHandler;
import users.ContactPerson;
import users.User;

/**
 * JUnit 5 test class that deals with submitting an auction request form.
 * This test class will check if the requested auction has a date that has
 * elapsed a year since the last requester's last auction.
 *
 * @Author David Perez
 * @version 5/3/2018
 */


public class SubmitAuctionRequestTest {

	//The following tests business rule 7e: No auction can be scheduled
	// less than a set # of days from the current date, default of 14.
	DataHandler myData = new DataHandler();
    ContactPerson contactUser = new ContactPerson("Contact", "McContact", "contact@contact.com", myData.getMyAuctionCalendar());

	@Test
	public void isThereNoPriorAuction_noPriorAuction_True() {
		/**
		 * Tests to ensure that an auction date is valid for request if
		 * the contact person has no previous auction or current auction.
		 */
		Auction priorAuction = null;
		contactUser.setMyCurrentAuction(priorAuction);
		
		assertTrue(contactUser.isThereNoPriorAuction());
	}

	@Test
	public void isRequiredTimeElapsedBetweenPriorAndNewAuctionMet_priorAuctionDateLessThanOneYear_False() {
		/**
		 *  Tests to ensure that an auction date is invalid for request if
		 * the contact person's previous action occurred less than one year ago.
		*/

		LocalDateTime priorDate = LocalDateTime.of(2017, 05, 26, 0, 0);

		LocalDate newDate = LocalDate.of(2018, 05, 25);
		Map<Integer, AuctionItem> inventorySheet = new HashMap<Integer, AuctionItem>();

		Auction priorAuction = new Auction("ContactOrg", 5, priorDate, inventorySheet);
		priorAuction.setAuctionDate(priorDate.toLocalDate());
		contactUser.setMyCurrentAuction(priorAuction);

		assertFalse(contactUser.isRequiredTimeElapsedBetweenPriorAndNewAuctionMet(newDate));
	}

	@Test
	public void  isRequiredTimeElapsedBetweenPriorAndNewAuctionMet_priorAuctionDateEqualToOneYear_True() {
		/**
		 *  Tests to ensure that an auction date is valid for request if
		 * the contact person's previous action occurred exactly one year ago.
		 */

		LocalDateTime priorDate = LocalDateTime.of(2017, 05 ,25, 0, 0);
		LocalDate newDate = LocalDate.of(2018, 05, 25);

		Map<Integer, AuctionItem> inventorySheet = new HashMap<Integer, AuctionItem>();

		Auction priorAuction = new Auction("ContactOrg", 5, priorDate, inventorySheet);
		priorAuction.setAuctionDate(priorDate.toLocalDate());
		contactUser.setMyCurrentAuction(priorAuction);
		
		assertTrue(contactUser.isRequiredTimeElapsedBetweenPriorAndNewAuctionMet(newDate));
	}

	@Test
	public void isRequiredTimeElapsedBetweenPriorAndNewAuctionMet_priorAuctionDateIsOneYearAndOneDay_True() {
		/**
		 *  Tests to ensure that an auction date is valid for request if
		 * the contact person's previous action occurred exactly one year plus one day ago.
		 */
		LocalDateTime priorDate = LocalDateTime.of(2017, 05 ,25, 0, 0);
		LocalDate newDate = LocalDate.of(2018, 05, 26);

		Map<Integer, AuctionItem> inventorySheet = new HashMap<Integer, AuctionItem>();

		Auction priorAuction = new Auction("ContactOrg", 5, priorDate, inventorySheet);
		priorAuction.setAuctionDate(priorDate.toLocalDate());
		contactUser.setMyCurrentAuction(priorAuction);
		
		assertTrue(contactUser.isRequiredTimeElapsedBetweenPriorAndNewAuctionMet(newDate));
	}

	@Test
	public void isMaxUpcomingAuctionsExceeded_lessThanMaxCap_false() {
		DataHandler newData = new DataHandler();

		Map<Integer, AuctionItem> inventorySheet = new HashMap<Integer, AuctionItem>();
		LocalDateTime priorDate = LocalDateTime.of(2017, 05 ,25, 0, 0);
		for (int i = 0; i < 22; i++) {
			Auction priorAuction = new Auction("ContactOrg", i, priorDate, inventorySheet);
			newData.addAuction(priorAuction);
		}

		assertFalse(Scheduler.isMaxUpcomingAuctionsExceeded(newData.getActiveAuctions().size()));
	}



	@Test
	public void isMaxUpcomingAuctionsExceeded_atMaxCapacity_true() {
		DataHandler newData = new DataHandler();

		Map<Integer, AuctionItem> inventorySheet = new HashMap<Integer, AuctionItem>();
		LocalDateTime priorDate = LocalDateTime.of(2017, 05 ,25, 0, 0);
		for (int i = 0; i < 23; i++) {
		Auction priorAuction = new Auction("ContactOrg", i, priorDate, inventorySheet);
			newData.getMyAuctionCalendar().getActiveAuctions().add(priorAuction);
		}

		assertTrue(Scheduler.isMaxUpcomingAuctionsExceeded(newData.getActiveAuctions().size()));
	}
	@Test
	public void isMaxDailyAuctionsExceeded_noAuctionsOnThisDay_False() {
		DataHandler newData = new DataHandler();
		LocalDateTime priorDate = LocalDateTime.of(2017, 05 ,25, 0, 0);

		assertFalse(Scheduler.isMaxUpcomingAuctionsExceeded(newData.getActiveAuctions().size()));
	}
	@Test
	public void isMaxDailyAuctionsExceeded_lessThanMaxAuctions_False() {
		DataHandler newData = new DataHandler();

	Map<Integer, AuctionItem> inventorySheet = new HashMap<Integer, AuctionItem>();
		LocalDateTime priorDate = LocalDateTime.of(2017, 05 ,25, 0, 0);
		for (int i = 0; i < 1; i++) {
		Auction priorAuction = new Auction("ContactOrg", i, priorDate, inventorySheet);
			newData.getMyAuctionCalendar().getActiveAuctions().add(priorAuction);

		}


		assertFalse(Scheduler.isMaxDailyAuctionsExceeded(priorDate, newData.getActiveAuctions()));
	}
	@Test
	public void isMaxDailyAuctionsExceeded_atMaxCapacityDailyAuctions_True() {
		DataHandler newData = new DataHandler();

		Map<Integer, AuctionItem> inventorySheet = new HashMap<Integer, AuctionItem>();
		LocalDateTime priorDate = LocalDateTime.of(2017, 05 ,25, 0, 0);
		for (int i = 0; i < 2; i++) {
			Auction priorAuction = new Auction("ContactOrg", i, priorDate, inventorySheet);
		newData.getMyAuctionCalendar().getActiveAuctions().add(priorAuction);


		}

		assertTrue(Scheduler.isMaxDailyAuctionsExceeded(priorDate, newData.getActiveAuctions()));
	}
} 
