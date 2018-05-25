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
	private JPanel myBottomPanel;
	
	//public AuctionGUI(final AuctionCalendar theCalendar) {
	public AuctionGUI(DataHandler theData){
		myListPanel = new JPanel();
		myAuctionPanel = new JPanel();
		myBottomPanel = new JPanel();
		//myCalendar = theCalendar;
		myData = myData;
		myAuctionPanel.setLayout(new BorderLayout());
		setupComponents();
	}
	
	private void setupComponents() {
		myListPanel.setLayout(new GridLayout(0,1));
		createAllAuctionButtons();
		myAuctionPanel.add(myListPanel, BorderLayout.CENTER);
		myBottomPanel.add(createBottomButton());
		myAuctionPanel.add(myBottomPanel, BorderLayout.SOUTH);
	}
	
	private JButton createBottomButton() {
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
	
	private void createAllAuctionButtons() {
		//if(myCalendar.getActiveAuctions().size() == 0) {
		if(myData.getActiveAuctions().size() == 0) {
			final JLabel auctionLabel = new JLabel("There are no auctions have been scheduled currently.", SwingConstants.CENTER);
			myListPanel.add(auctionLabel);
		} else {
			//for(Auction a : myCalendar.getActiveAuctions()) {
			for(Auction a : myData.getActiveAuctions()) {
				final JButton auctionButton = new JButton(a.getOrgName());
				auctionButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(final ActionEvent theEvent) {
						setChanged();
						notifyObservers();
					}
					
				});
				myListPanel.add(auctionButton);
			}
		}
		
		//if(myCalendar.getPastAuctions().size() == 0) {
		if(myData.getPastAuctions().size() == 0) {
			final JLabel auctionLabel = new JLabel("There are no past auctions have been found.", SwingConstants.CENTER);
			myListPanel.add(auctionLabel);
		} else {
			//for(Auction a : myCalendar.getPastAuctions()) {
			for(Auction a : myData.getPastAuctions()) {
				final JButton auctionButton = new JButton(a.getOrgName());
				auctionButton.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(final ActionEvent theEvent) {
						setChanged();
						notifyObservers();
					}
				});
				myListPanel.add(auctionButton);
			}
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}	
}
