package GUI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import auctiondata.Auction;
import auctiondata.AuctionItem;
import auctiondata.Bid;
import storage.AuctionCalendar;
import users.Bidder;

public final class BidderGUI extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3761509683757395546L;
	private final AuctionCalendar myCalendar;
	private final Bidder myBidder;
	private final BidBar myBidBar;
	private Auction myCurrentAuction;
	private AuctionItem myCurrentItem;
	private JPanel myViewAllItemsInAuction;
	private JPanel myViewAllItemsinAuctionWithMyBids;
	private JPanel myCurrentAuctionItemsPanel;
	private JPanel myRadioPanel;

	public BidderGUI(final Bidder theBidder, final AuctionCalendar theCalendar) {	
		myCalendar = theCalendar;
		myBidder = theBidder;
		myCurrentAuction = null;
		myCurrentItem = null;
		myRadioPanel = createRadioPanel();
		myCurrentAuctionItemsPanel = new JPanel();
		myViewAllItemsInAuction = new JPanel();
		myViewAllItemsinAuctionWithMyBids = new JPanel();
		final JTabbedPane tabs = setupTabs();
		myBidBar = new BidBar();
		this.add(tabs, BorderLayout.CENTER);
		this.add(myBidBar, BorderLayout.SOUTH);
		this.setVisible(true);
	}	
	
	private JPanel createRadioPanel() {
		final JPanel radioPanel = new JPanel();
		final JRadioButton allItemsAvailableInAuction = new JRadioButton();
		final JRadioButton allItemsInAuctionWithMybids = new JRadioButton();
		final ButtonGroup radioButtonGroup = new ButtonGroup();
		radioButtonGroup.add(allItemsInAuctionWithMybids);
		radioButtonGroup.add(allItemsAvailableInAuction);
		radioPanel.add(allItemsInAuctionWithMybids);
		radioPanel.add(allItemsAvailableInAuction);
		
		return radioPanel;
	}
	
	private JTabbedPane setupTabs() {
		final JTabbedPane tabs = new JTabbedPane();
		
		final JSplitPane viewAllBiddableAuctions = createViewBiddableAuctionsPanel();
		final JPanel viewAllItemsWithMyBids = createViewAllItemsWithMyBids();
		tabs.addTab("View auctions you can bid in", viewAllBiddableAuctions);
		tabs.addTab("View your bids", viewAllItemsWithMyBids);
		
		return tabs;
	}
	
	private JPanel createViewAllItemsWithMyBids() {
		final JPanel panel = new JPanel();
		final String[] bids = new String[myBidder.getBids().size()];
		int i = 0;
		for(final Bid b : myBidder.getBids()) {
			bids[i] = getBidString(b);
			i++;
		}
		final JList<String> bidsList = new JList<>(bids);
		panel.add(bidsList);
		return panel;
	}
	
	private String getBidString(final Bid theBid) {
		final StringBuilder sb = new StringBuilder();
		sb.append(theBid.getItem().getName() + " from " + theBid.getAuction().getOrgName() 
				  + " | Bid: $" + theBid.getBidAmount());
		return sb.toString();

	}
	
	private String getAuctionItemString(final AuctionItem theItem) {
		final StringBuilder sb = new StringBuilder();
		sb.append("ID: " + theItem.getUniqueID() + " | " + theItem.getName() 
		  + " | Min Price: $" + theItem.getMinPrice());
		return sb.toString();
	}

	private JSplitPane createViewBiddableAuctionsPanel() {		
		final JSplitPane pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		pane.setLeftComponent(createAuctionsList());
		pane.setRightComponent(myCurrentAuctionItemsPanel);
		return pane;
	}

	private void updateAuctionItemsList() {
		final Map<Integer, AuctionItem> items = myCurrentAuction.getInventorySheet();
		final String[] itemStrings = new String[items.size()];
		for(final Integer i : items.keySet()) {
			final AuctionItem item = items.get(i);
			itemStrings[i] = getAuctionItemString(item);
		}
		final JList<String> itemList = new JList<>(itemStrings);
		myCurrentAuctionItemsPanel.setLayout(new BorderLayout());
		myCurrentAuctionItemsPanel.add(itemList, BorderLayout.CENTER);
		itemList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(final ListSelectionEvent theEvent) {
				final int index = theEvent.getLastIndex();
				myCurrentItem = myCurrentAuction.getInventorySheet().get(index);
				myBidBar.setBidItem();
			}
		});		
		myCurrentAuctionItemsPanel.add(myRadioPanel, BorderLayout.SOUTH);
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
				myCurrentAuction = myCalendar.getAuction(theArg.getLastIndex());
				updateAuctionItemsList();
			}
		});
		
		return auctions;
	}

	public class BidBar extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1450666832316412151L;
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
		
		public void setBidItem() {
			myLabel.setText(myCurrentItem.getName() + " $");
			final BigDecimal bid = new BigDecimal(((Number)myBidAmount.getValue()).doubleValue());
			setBidBarStatus(myBidder.isBidPlaceable(myCurrentAuction, myCurrentItem, bid));
		}
		
		private void setBidBarStatus(final boolean theStatus) {
			myBidButton.setEnabled(theStatus);
		}
	}
}
