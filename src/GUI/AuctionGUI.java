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
import storage.AuctionCalendar;
import storage.DataHandler;

/**
 *
 */
public class AuctionGUI extends Observable implements Observer{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6663684108079124777L;
	private final JPanel myListPanel;
	//private final AuctionCalendar myCalendar;
	private final JPanel myAuctionPanel;
	private DataHandler myData;
	
	//public AuctionGUI(final AuctionCalendar theCalendar) {
	public AuctionGUI(DataHandler theData){
		myListPanel = new JPanel();
		myAuctionPanel = new JPanel();
		//myCalendar = theCalendar;
		myData = myData;
		myAuctionPanel.setLayout(new BorderLayout());
		setupComponents();
	}
	
	private void setupComponents() {
		myListPanel.setLayout(new GridLayout(0,1));
		createAllAuctionLabels();
		myAuctionPanel.add(myListPanel, BorderLayout.CENTER);
		myAuctionPanel.add(createBackButton(), BorderLayout.NORTH);
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
	
	private void createAllAuctionLabels() {
		//if(myCalendar.getActiveAuctions().size() == 0) {
		if(myData.getActiveAuctions().size() == 0) {
			final JLabel auctionLabel = new JLabel("There are no auctions have been scheduled currently.");
			myListPanel.add(auctionLabel);
		} else {
			//for(Auction a : myCalendar.getActiveAuctions()) {
			for(Auction a : myData.getActiveAuctions()) {
				final JLabel auctionLabel = new JLabel("ActiveAuction : " + a.getOrgName());
				myListPanel.add(auctionLabel);
			}
		}
		
		//if(myCalendar.getPastAuctions().size() == 0) {
		if(myData.getPastAuctions().size() == 0) {
			final JLabel auctionLabel = new JLabel("There are no past auctions have been found.");
			myListPanel.add(auctionLabel);
		} else {
			//for(Auction a : myCalendar.getPastAuctions()) {
			for(Auction a : myData.getPastAuctions()) {
				final JLabel auctionLabel = new JLabel("PastAuction : " + a.getOrgName());
				myListPanel.add(auctionLabel);
			}
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}	
}
