/*
 * 
 */
package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import auctiondata.Auction;
import auctiondata.AuctionItem;
import auctiondata.Bid;
import storage.DataHandler;
import users.Bidder;

/**
 *
 */
public class AuctionGUI extends Observable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 693948431228885474L;
	private Auction myAuction;
	private final JPanel myListPanel;
	private final JPanel myCenterPanel;
	private final JPanel myAuctionItemPanel;
	private final JPanel myBottomPanel;
	private final JPanel myBidPanel;
	private final Bidder bidder;
	private Set<AuctionItem> auctionItems;
	
	public AuctionGUI(final Auction theAuction, DataHandler myData, Bidder theBidder) {
		myAuction = theAuction;
		bidder = theBidder;
		myListPanel = new JPanel();
		myCenterPanel = new JPanel();
		myCenterPanel.setLayout(new GridLayout(1,2));
		myBottomPanel = new JPanel();
		myBidPanel = new JPanel();
		myBidPanel.setLayout(new GridLayout(0,1));
		myListPanel.setLayout(new GridLayout(0,1));
		myAuctionItemPanel = new JPanel();
		myAuctionItemPanel.setLayout(new BorderLayout());
		auctionItems = myData.getAuctionItemsByAuction(theAuction);
		setupComponents();
	}
	
	private void setupComponents() {
		JLabel itemTitle = new JLabel("Items for this Auction", SwingConstants.CENTER);
		myListPanel.add(itemTitle);
		createButtons();
		myCenterPanel.add(myListPanel);
		JLabel bidTitle = new JLabel("Bids for this Auction", SwingConstants.CENTER);
		myBidPanel.add(bidTitle);
		myBidPanel.setBackground(Color.WHITE);
		createLabels();
		myCenterPanel.add(myBidPanel);
		myAuctionItemPanel.add(myCenterPanel, BorderLayout.CENTER);	
		myBottomPanel.add(createBackButton());
		myBottomPanel.setBackground(Color.WHITE);
		myAuctionItemPanel.add(myBottomPanel, BorderLayout.SOUTH);
		
	}
	
	private void createLabels() {
		for(Bid b : bidder.getBids()) {
			if(b.getAuction().equals(myAuction)) {
				JLabel bidLabel = new JLabel("  ItemID: " + b.getItem().getUniqueID() + " | Item Name: " + b.getItem().getName()
						+ " | Your Bid Amount: " + b.getBidAmount() + "  ", SwingConstants.CENTER);
				myBidPanel.add(bidLabel);
			}
		}
	}

	private JButton createBackButton() {
		final JButton backButton = new JButton("Back");
		backButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(final ActionEvent theEvent) {
				setChanged();
				notifyObservers();
			}
		});
		return backButton;
	}
	
	private void createButtons() {

		//for(AuctionItem ai : myAuction.getInventorySheet().values()) {
		for(AuctionItem ai : auctionItems) {
			final JButton itemButton = new JButton(ai.getName());
			itemButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(final ActionEvent theEvent) {
					setChanged();
					notifyObservers(ai);
				}
				
			});
			myListPanel.add(itemButton);
		}
	}
	
	public JPanel getPanel() {
		return myAuctionItemPanel;
	}
}
