package GUI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import auctiondata.Auction;
import auctiondata.AuctionItem;
import storage.AuctionCalendar;
import users.Bidder;

public final class BidderGUI extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3761509683757395546L;
	private final AuctionCalendar myCalendar;
	private final Bidder myBidder;
	private final JPanel myBidBar;
	private JList<String> myCurrentAuctionItemsPanel;

	public BidderGUI(final Bidder theBidder, final AuctionCalendar theCalendar) {	
		myCalendar = theCalendar;
		myBidder = theBidder;
		myCurrentAuctionItemsPanel = new JList<String>();
		final JTabbedPane tabs = setupTabs();
		myBidBar = new BidBar();
		this.add(tabs, BorderLayout.CENTER);
		this.add(myBidBar, BorderLayout.SOUTH);
		this.setVisible(true);
	}	
	
	private JTabbedPane setupTabs() {
		final JTabbedPane tabs = new JTabbedPane();
		
		final JSplitPane viewAllBiddableAuctions = createViewBiddableAuctionsPanel();
		final JPanel viewAllItemsWithMyBids = new JPanel();
		tabs.addTab("View auctions you can bid in", viewAllBiddableAuctions);
		tabs.addTab("View your bids", viewAllItemsWithMyBids);
		
		final JPanel viewAllItemsInAuction = new JPanel();
		final JPanel viewAllItemsInAuctionWithMyBids = new JPanel();
		tabs.add(viewAllItemsInAuction);
		tabs.add(viewAllItemsInAuctionWithMyBids);
		
		return tabs;
	}

	private JSplitPane createViewBiddableAuctionsPanel() {		
		final JSplitPane pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		pane.setLeftComponent(createAuctionsList());
		pane.setRightComponent(myCurrentAuctionItemsPanel);
		return pane;
	}
	
	private void updateAuctionItemsList(final int theAuctionIndex) {
		final Auction auction = myCalendar.getActiveAuctions().get(theAuctionIndex);
		final Map<Integer, AuctionItem> items = auction.getInventorySheet();
		final String[] itemStrings = new String[items.size()];
		for(final Integer i : items.keySet()) {
			final StringBuilder sb = new StringBuilder();
			final AuctionItem item = items.get(i);
			sb.append("ID: " + item.getUniqueID() + " | " + item.getName() 
					  + " | Min Price: $" + item.getMinPrice());
			itemStrings[i] = sb.toString();
		}
		myCurrentAuctionItemsPanel = new JList<String>(itemStrings);
	}
	
	private JList<String> createAuctionsList() {
		final String[] auctionStrings = new String[myCalendar.getActiveAuctions().size()];
		int i = 0;
		for(final Auction a : myCalendar.getActiveAuctions()) {
			final StringBuilder sb = new StringBuilder();
			sb.append("ID: " + + a.getauctionID() + " | Date: " 
					  + a.getAuctionDate().toString() + " | " + a.getOrgName());
			auctionStrings[i] = sb.toString();
			i++;
		}
		final JList<String> auctions = new JList<>(auctionStrings);
		
		auctions.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(final ListSelectionEvent theArg) {
				updateAuctionItemsList(theArg.getLastIndex());
			}
		});
		
		return auctions;
	}

	public class BidBar extends JPanel {
		private JLabel myLabel;
		private JFormattedTextField myBidAmount;
		private JButton myBidButton;
		
		public BidBar() {
			myLabel = new JLabel();
			myBidAmount = new JFormattedTextField(NumberFormat.getCurrencyInstance());
			myBidButton = new JButton("Place bid");
			myBidButton.addActionListener(new ActionListener() {
				public void actionPerformed(final ActionEvent theEvent) { 
					// place bid
				}
			});
		}
		
		public void setBidItem(final Auction theAuction, final AuctionItem theItem) {
			myLabel.setText(theItem.getName() + " $");
			final BigDecimal bid = new BigDecimal(((Number)myBidAmount.getValue()).doubleValue());
			setBidBarStatus(myBidder.isBidPlaceable(theAuction, theItem, bid));
		}
		
		private void setBidBarStatus(final boolean theStatus) {
			myBidButton.setEnabled(theStatus);
		}
	}
}
