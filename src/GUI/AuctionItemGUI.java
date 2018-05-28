/**
 * 
 */
package GUI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import auctiondata.Auction;
import auctiondata.AuctionItem;
import auctiondata.Bid;
import storage.DataHandler;
import users.Bidder;

/**
 * @author wen
 *
 */
public class AuctionItemGUI extends Observable {
	
	private JPanel myBidPanel;
	private AuctionItem mySelectedItem;
	private Bidder bidder;
	private JButton myBidButton;
	private DataHandler myData;
	
	public AuctionItemGUI(AuctionItem theItem, Bidder theBidder, DataHandler theData) {
		myData = theData;
		myBidPanel = new JPanel();
		mySelectedItem = theItem;
		bidder = theBidder;
		setupComponents();
//		boolean result = theBidder.isBidPlaceable(theItem.getAuction(), theItem, new BigDecimal("0"));
//		myBidButton.setEnabled(result);
	}

	private void setupComponents() {
		myBidPanel.setLayout(new BorderLayout());
		createBottomPanel();
		createDetailPanel();
	}

	private void createDetailPanel() {
		JPanel detailPanel = new JPanel();
		JLabel itemLabel = new JLabel("ID: " + mySelectedItem.getUniqueID() + " | Name: " 
									  + mySelectedItem.getName() + " | Price: " 
									  + mySelectedItem.getMinPrice(), SwingConstants.CENTER);
		detailPanel.add(itemLabel);
		myBidPanel.add(detailPanel, BorderLayout.CENTER);
	}

	private void createBottomPanel() {
		JPanel bottomPanel = new JPanel();
		myBidButton = new JButton("Place bid");
		myBidButton.setEnabled(isBidButtonEnabled());
		myBidButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String inputAmount = JOptionPane.showInputDialog("How much do you want to bid on this item?");

				//Tanner added auction, so there is a reference to the auction, just in case
				Bid bidAmount = new Bid(mySelectedItem.getAuction(), new BigDecimal(inputAmount), mySelectedItem);
				int confirmValue = JOptionPane.showConfirmDialog(null,  "Do you want place a bid for " +
																 inputAmount + "?");
				if(confirmValue == 0) {
					if(bidder.isBidPlaceable(mySelectedItem.getAuction(), mySelectedItem, BigDecimal.valueOf(bidAmount.getBidAmount()))){
						myData.placeBid(bidder, bidAmount);
					}
					//bidder.placeBid(bidAmount); //handled in datahandler
				}
			}
		});
		 bottomPanel.add(myBidButton);
		 JButton backButton = new JButton("Back");
		 backButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setChanged();
				notifyObservers();
			}
		 });
		 
		 bottomPanel.add(backButton);
		 myBidPanel.add(bottomPanel, BorderLayout.SOUTH);
	}
	
	private boolean isBidButtonEnabled() {
		final StringBuilder sb = new StringBuilder();
		final Auction a = mySelectedItem.getAuction();
		boolean result = true;
		myBidButton.setEnabled(bidder.isBidPlaceable(mySelectedItem.getAuction(), mySelectedItem, new BigDecimal("0")));

		if(!Bidder.isDateValid(a.getAuctionDate())) {
			result = false;
			sb.append("It is too late to bid on this item.\n");
		}
		if(!bidder.isBelowMaxBidsPerAuction(a)) {
			result = false;
			sb.append("You have too many bids in this auction.\n");
		}
		if(!bidder.isBelowMaxTotalBids()) {
			result = false;
			sb.append("You have too many total bids.\n");
		}
		if(!bidder.isItemNotBidOnByMe(mySelectedItem)) {
			result = false;
			sb.append("You have already bid on this item");
		}
		if(!result) {
			myBidButton.setToolTipText(sb.toString());
		}
		return result;
	}
	
	public JPanel getPanel() {
		return myBidPanel;
	}
}
