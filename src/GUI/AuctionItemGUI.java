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
import auctiondata.Auction;
import auctiondata.AuctionItem;

/**
 *
 */
public class AuctionItemGUI extends Observable implements Observer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 693948431228885474L;
	private Auction myAuction;
	private final JPanel myListPanel;
	private final JPanel myAuctionItemPanel;
	
	public AuctionItemGUI(final Auction theAuction) {
		myAuction = theAuction;
		myListPanel = new JPanel();
		myListPanel.setLayout(new GridLayout(0,1));
		myAuctionItemPanel = new JPanel();
		myAuctionItemPanel.setLayout(new BorderLayout());
		setupComponents();
	}
	
	private void setupComponents() {
		createLabels();
		myAuctionItemPanel.add(myListPanel, BorderLayout.CENTER);
		myAuctionItemPanel.add(createBackButton(), BorderLayout.NORTH);
	}
	
	private JButton createBackButton() {
		final JButton backButton = new JButton("Back");
		backButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(final ActionEvent theEvent) {
				
			}
		});
		return backButton;
	}
	
	private void createLabels() {
		for(AuctionItem ai : myAuction.getInventorySheet().values()) {
			final JLabel itemLabel = new JLabel(ai.getName());
			myListPanel.add(itemLabel);
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}	
}
