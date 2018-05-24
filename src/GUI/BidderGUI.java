package GUI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

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
import storage.AuctionCalendar;
import storage.DataHandler;
import users.Bidder;

public class BidderGUI extends Observable implements Observer {

	private Auction mySelectedAuction;
	private AuctionItem mySelectedItem;
	private final JPanel myPanel;
	private final JButton myLoadAuctionButton;
	private final JButton myLoadItemButton;
	private final Bidder myBidder;
	//private final AuctionCalendar myCalendar;
	private DataHandler myData;
	private JList<Auction> myAuctionsList;
	private JList<Bid> myBidsList;

	
	public BidderGUI(final Bidder theBidder, final DataHandler theData) {
		//myCalendar = theCalendar;
		myData = theData;
		myBidder = theBidder;
		mySelectedAuction = null;
		mySelectedItem = null;
		myLoadAuctionButton = createLoadAuctionButton();
		myLoadItemButton = createLoadItemButton();
		myPanel = new JPanel();
		myPanel.setLayout(new GridLayout(1, 2));
		myPanel.add(createAuctionsPanel());
		myPanel.add(createBidsPanel());
		//System.out.println("Auction count: " + myCalendar.getActiveAuctions().size());
		System.out.println("Auction count: " + myData.getActiveAuctions().size());

		System.out.println("Bid count: " + myBidder.getBids().size());
	}
	
	private JPanel createAuctionsPanel() {
		final JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		final JLabel label = new JLabel("Open auctions", SwingConstants.CENTER);

		final DefaultListModel<Auction> auctionsList = createAuctionListModel();
		myAuctionsList = createAuctionJList(auctionsList);
		
		panel.add(label, BorderLayout.NORTH);
		panel.add(new JScrollPane(myAuctionsList), BorderLayout.CENTER);
		panel.add(myLoadAuctionButton, BorderLayout.SOUTH);
		return panel;
	}
	
	private JPanel createBidsPanel() {
		final JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		final JLabel label = new JLabel("Your bids", SwingConstants.CENTER);
		
		final DefaultListModel<Bid> bidsList = createBidsListModel();
		myBidsList = createBidsJList(bidsList);
		panel.add(label, BorderLayout.NORTH);
		panel.add(new JScrollPane(myBidsList), BorderLayout.CENTER);
		panel.add(myLoadItemButton, BorderLayout.SOUTH);

		return panel;
	}

	private JList<Bid> createBidsJList(final DefaultListModel<Bid> theList) {
		JList<Bid> list = new JList<>(theList);

		//TODO: Tanner change
		//Added this because I think an error was caused due to this list initially being null? (same with the auction list)
		//No original code changed in this block.
		if(null == myBidsList)
			myBidsList = new JList<>();
		//END CHANGE

		myBidsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		myBidsList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(final ListSelectionEvent theEvent) {
				final JList<Bid> list = (JList<Bid>) theEvent.getSource();
				mySelectedItem = list.getSelectedValue().getItem();
			}
		});
		return list;
	}
	
	private DefaultListModel<Bid> createBidsListModel() {
		final DefaultListModel<Bid> list = new DefaultListModel<>();

		for(final Bid b : myBidder.getBids()) {

			System.out.println(b.toString());
			list.addElement(b);
		}
		return list;
	}
	
	private JList<Auction> createAuctionJList(final DefaultListModel<Auction> theList) {
		final JList<Auction> list = new JList<>(theList);

		//TODO: Tanner change
		//Added this because I think an error was caused due to this list initially being null?
		//No original code changed in this block.
		if(null==myAuctionsList)
			myAuctionsList = new JList<>();
		//END CHANGE

		myAuctionsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		myAuctionsList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(final ListSelectionEvent theEvent) {
				final JList<Auction> list = (JList<Auction>) theEvent.getSource();
				mySelectedAuction = list.getSelectedValue();
			}
		});
		return list;
	}
	
	private DefaultListModel<Auction> createAuctionListModel() {
		final DefaultListModel<Auction> list = new DefaultListModel<>();

		//TODO: Tanner change
		//for(final Auction a : myCalendar.getActiveAuctions()) { <- Charlie Code
		for(final Auction a : myData.getActiveAuctions()) { //<--Tanner's Change
		// END CHANGE

			System.out.println(a.toString());
			list.addElement(a);
		}
		return list;
	}
	
	private JButton createLoadAuctionButton() {
		final JButton button = new JButton("View auction");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent theEvent) {
				setChanged();
				notifyObservers(mySelectedAuction);
			}
			
		});
		return button;	
	}
	
	private JButton createLoadItemButton() {
		final JButton button = new JButton("View item");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent theEvent) {
				setChanged();
				notifyObservers(mySelectedItem);
			}
			
		});
		return button;	
	}
	
	public JPanel getPanel() {
		return myPanel;
	}

	public class AuctionJListRenderer extends JLabel implements ListCellRenderer<Auction> {
		/**
		 * 
		 */
		private static final long serialVersionUID = -63431663198198825L;

		@Override
		public Component getListCellRendererComponent(JList<? extends Auction> theList, Auction theAuction, int theIndex,
				boolean theIsSelected, boolean theIsInFocus) {
			this.setText(theAuction.getOrgName() + " | " + theAuction.getAuctionDate());
			if(theIsSelected) {
				this.setForeground(theList.getSelectionForeground());
				this.setBackground(theList.getSelectionBackground());
			} else {
				this.setForeground(theList.getForeground());
				this.setBackground(theList.getBackground());
			}
			return null;
		}	
	}
	
	public class BidJListRenderer extends JLabel implements ListCellRenderer<Bid> {

		/**
		 * 
		 */
		private static final long serialVersionUID = -425852109094304125L;

		@Override
		public Component getListCellRendererComponent(JList<? extends Bid> theList, Bid theBid, int theIndex,
				boolean theIsSelected, boolean theIsInFocus) {
			this.setText(theBid.getItem().getName() + " | " + theBid.getBidAmount());
			if(theIsSelected) {
				this.setForeground(theList.getSelectionForeground());
				this.setBackground(theList.getSelectionBackground());
			} else {
				this.setForeground(theList.getForeground());
				this.setBackground(theList.getBackground());
			}
			return null;
		}	
	}

	@Override
	public void update(final Observable theObservable, final Object theObject) {
		if(theObservable instanceof DataHandler) {
			if(theObject instanceof Bid) {
				myBidsList = createBidsJList(createBidsListModel());
			} else if (theObject instanceof Auction) {
				myAuctionsList = createAuctionJList(createAuctionListModel());
			} else if(theObject instanceof AuctionItem) {
				// do nothing
			}
		}
	}
}
