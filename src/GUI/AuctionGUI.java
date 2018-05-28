/*
 * 
 */
package GUI;

import java.awt.BorderLayout;
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
import storage.DataHandler;

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
	private final JPanel myAuctionItemPanel;
	private final JPanel myBottomPanel;
	private Set<AuctionItem> auctionItems;
	
	public AuctionGUI(final Auction theAuction, DataHandler myData) {
		myAuction = theAuction;
		myListPanel = new JPanel();
		myBottomPanel = new JPanel();
		myListPanel.setLayout(new GridLayout(0,1));
		myAuctionItemPanel = new JPanel();
		myAuctionItemPanel.setLayout(new BorderLayout());
		auctionItems = myData.getAuctionItemsByAuction(theAuction);
		setupComponents();
	}
	
	private void setupComponents() {
		createButtons();
		JLabel title = new JLabel("Auction Items", SwingConstants.CENTER);
		myAuctionItemPanel.add(myListPanel, BorderLayout.CENTER);
		myAuctionItemPanel.add(title, BorderLayout.NORTH);
		myBottomPanel.add(createBackButton());
		myAuctionItemPanel.add(myBottomPanel, BorderLayout.SOUTH);
		
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
