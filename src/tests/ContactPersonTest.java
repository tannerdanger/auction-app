/**
 *
 */
package tests;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import auctiondata.Auction;
import auctiondata.AuctionItem;
import users.ContactPerson;

/**
 * @author wen
 * @version May 7th 2018
 */
public class ContactPersonTest {
	ContactPerson contact;
	Auction auction;
	AuctionItem item1;
	AuctionItem item2;
	AuctionItem item3;
	AuctionItem item4;
	AuctionItem item5;
	AuctionItem item6;
	AuctionItem item7;
	AuctionItem item8;
	AuctionItem item9;
	AuctionItem item10;


	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		contact = new ContactPerson("wen", "shu", "shuwen@contact.com");
		auction = new Auction("UWTacoma", 12345, LocalDateTime.of(2018, 5, 30, 11, 59), null);
		item1 = new AuctionItem(1, "pencil", auction);
		auction.addInventoryItem(item1);
		item2 = new AuctionItem(2, "marker", auction);
		auction.addInventoryItem(item2);
		item3 = new AuctionItem(5, "fresh sandwitch", auction);
		auction.addInventoryItem(item3);
		item4 = new AuctionItem(999.99, "race bicycle", auction);
		auction.addInventoryItem(item4);
		item5 = new AuctionItem(99.99, "24-inch monitor", auction);
		auction.addInventoryItem(item5);
		item6 = new AuctionItem(199.99, "40-inch TV", auction);
		auction.addInventoryItem(item6);
		item7 = new AuctionItem(50, "home run baseball", auction);
		auction.addInventoryItem(item7);
		item8 = new AuctionItem(10, "sunglasses", auction);
		auction.addInventoryItem(item8);
		item9 = new AuctionItem(69.99, "iphone 4", auction);
		auction.addInventoryItem(item9);
		item10 = new AuctionItem(599.99, "iphone 8", auction);
		auction.addInventoryItem(item10);
	}

	@Test
	public void addInventoryItem_LessThanMaxNumberOfItemForSale_True() {
		auction.removeInventoryItem(item1.getUniqueID());
		auction.removeInventoryItem(item2.getUniqueID());
		auction.removeInventoryItem(item3.getUniqueID());
		auction.removeInventoryItem(item4.getUniqueID());
		auction.removeInventoryItem(item5.getUniqueID());

		AuctionItem item11 = new AuctionItem(799.99, "iphone x", auction);
		assertTrue(auction.addInventoryItem(item11));
	}

	@Test
	public void addInventoryItem_OneLessThanMaxNumberOfItemForSale_True() {
		auction.removeInventoryItem(item1.getUniqueID());

		AuctionItem item11 = new AuctionItem(799.99, "iphone x", auction);
		assertTrue(auction.addInventoryItem(item11));
	}

	@Test
	public void addInventoryItem_OneMoreThanMaxNumberOfItemForSale_True() {
		AuctionItem item11 = new AuctionItem(799.99, "iphone x", auction);
		assertFalse(auction.addInventoryItem(item11));
	}

}
