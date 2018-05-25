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
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import auctiondata.Auction;
import auctiondata.AuctionItem;

/**
 *
 */
public class AuctionGUI extends Observable implements Observer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 693948431228885474L;
	private Auction myAuction;
	private final JPanel myListPanel;
	private final JPanel myAuctionItemPanel;
	private final JPanel myBottomPanel;
	
	public AuctionGUI(final Auction theAuction) {
		myAuction = theAuction;
		myListPanel = new JPanel();
		myBottomPanel = new JPanel();
		myListPanel.setLayout(new GridLayout(0,1));
		myAuctionItemPanel = new JPanel();
		myAuctionItemPanel.setLayout(new BorderLayout());
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
		for(AuctionItem ai : myAuction.getInventorySheet().values()) {
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

	@Override
	public void update(Observable arg0, Object arg1) {
		
	}	
}
