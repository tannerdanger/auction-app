package GUI;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import auctiondata.Auction;
import auctiondata.AuctionItem;
import auctiondata.Bid;
import storage.DataHandler;
import users.Bidder;
import users.ContactPerson;

public class ContactGUI extends Observable implements Observer {

	private Auction mySelectedAuction;
	private AuctionItem mySelectedItem;
	private final JPanel myPanel;
	private final Container myContainer;
	private final JButton myActiveAuctionButton;
	private final JButton myNewAuctionButton;
	private final ContactPerson myContactPerson;
	private DataHandler myData;
	private JList<Auction> myAuctionsList;
	private JList<Bid> myBidsList;

	
	public ContactGUI(final ContactPerson theContactPerson, final DataHandler theData) {
		myData = theData;
		myContactPerson = theContactPerson;
		myActiveAuctionButton = createActiveAuctionButton();
		myNewAuctionButton = createNewAuctionRequestButton();
		mySelectedAuction = null;
		mySelectedItem = null;
		myPanel = new JPanel();
		myPanel.setLayout(new BorderLayout());
		myPanel.setMinimumSize(new Dimension(720, 720));
		myContainer = new Container();
		final JLabel label = new JLabel("Welcome" + myContactPerson.getMyOrgName(), SwingConstants.CENTER);
		myPanel.add(label, BorderLayout.NORTH);
		myPanel.add(createCenterPanel(), BorderLayout.CENTER);
		myPanel.add(createButtonsPanel(), BorderLayout.SOUTH);
		myPanel.setVisible(true);
		
		
	}
	
	private JPanel createCenterPanel() {
		
		final JPanel panel = new JPanel();
		panel.setLayout(new CardLayout());
		panel.setBorder(BorderFactory.createLineBorder(Color.black));
		final JLabel label = new JLabel("Something");
		panel.add(label);
		panel.setMinimumSize(new Dimension(720, 720));
		
		return panel;
	}
	
	private JPanel createButtonsPanel() {
		
		final JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		panel.add(myActiveAuctionButton, BorderLayout.CENTER);
		panel.add(myNewAuctionButton, BorderLayout.SOUTH);
		return panel;
	}
	
	
	


	
	
	
	private JButton createActiveAuctionButton() {
		final JButton button = new JButton("View Active Auction");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent theEvent) {
				
				setChanged();
				notifyObservers(mySelectedAuction);
			}
			
		});
		return button;	
	}
	
	private JButton createNewAuctionRequestButton() {
		final JButton button = new JButton("Submit New Auction Request");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent theEvent) {
				setChanged();
				notifyObservers(mySelectedAuction);
			}
			
		});
		return button;	
	}
	
	public JPanel getPanel() {
		return myPanel;
	}



	@Override
	public void update(final Observable theObservable, final Object theObject) {
		if(theObservable instanceof DataHandler) {
			
		}
	}
}
	
