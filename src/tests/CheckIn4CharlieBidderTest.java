package tests;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import users.Bidder;

public class CheckIn4CharlieBidderTest {

	@Test
	public void isDateValid_beforeAuctionDate_true() {
		Bidder.isDateValid(LocalDate.now().minusDays(1));
	}
	
	public void isDateValid_onAuctionDate_false() {
		Bidder.isDateValid(LocalDate.now());	
	}
	
	public void isDateValid_afterAuctionDate_false() {
		Bidder.isDateValid(LocalDate.now().plusDays(1));
	}

}
