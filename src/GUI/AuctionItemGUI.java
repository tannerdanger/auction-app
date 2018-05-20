/*
 * 
 */
package GUI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import auctiondata.Auction;
import auctiondata.AuctionItem;

/**
 *
 */
public class AuctionItemGUI extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 693948431228885474L;
	private final ObservableAuctionItemGui myObservable;
	private Auction myAuction;
	private final JPanel myListPanel;
	
	public AuctionItemGUI(final Auction theAuction) {
		myObservable = new ObservableAuctionItemGui();
		myAuction = theAuction;
		myListPanel = new JPanel();
		myListPanel.setLayout(new GridLayout(0,1));
		this.setLayout(new BorderLayout());
		setupComponents();
	}
	
	private void setupComponents() {
		createLabels();
		this.add(myListPanel, BorderLayout.CENTER);
		this.add(createBackButton(), BorderLayout.NORTH);
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
	
	public class ObservableAuctionItemGui extends Observable {
		public ObservableAuctionItemGui() {
			
		}
		
		public void setChangedTrue() {
			this.setChanged();
		}
	}
}
