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
import users.Bidder;

/**
 * @author wen
 *
 */
public class AuctionItemGUI extends Observable implements Observer{
	
	private JPanel myBidPanel;
	private AuctionItem mySelectedItem;
	private Bidder bidder;
	
	public AuctionItemGUI(AuctionItem theItem, Bidder theBidder) {
		myBidPanel = new JPanel();
		mySelectedItem = theItem;
		bidder = theBidder;
		setupComponents();
	}

	private void setupComponents() {
		myBidPanel.setLayout(new BorderLayout());
		createBottomPanel();
		createDetailPanel();
	}

	private void createDetailPanel() {
		JPanel detailPanel = new JPanel();
		JLabel itemLabel = new JLabel("ID: " + mySelectedItem.getUniqueID() + " | Name: " + mySelectedItem.getName() + " | Price: " + mySelectedItem.getMinPrice(), SwingConstants.CENTER);
		detailPanel.add(itemLabel);
		myBidPanel.add(detailPanel, BorderLayout.CENTER);
	}

	private void createBottomPanel() {
		JPanel bottomPanel = new JPanel();
		JButton bidButton = new JButton("place bid");
		bidButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String inputAmount = JOptionPane.showInputDialog("How much do you want to bid on this item?");
				Bid bidAmount = new Bid(mySelectedItem, new BigDecimal(inputAmount));
				bidder.placeBid(bidAmount);
			}
		});
		 bottomPanel.add(bidButton);
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
	
	public JPanel getPanel() {
		return myBidPanel;
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}
	
}
