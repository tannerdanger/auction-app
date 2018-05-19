package GUI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

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
import users.Bidder;

public class NewBidderGui extends JPanel  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1755050314040399620L;
	private final ObservableBidderGui myObservable;
	private Auction mySelectedAuction;
	private AuctionItem mySelectedItem;
	private final JButton myLoadAuctionButton;
	private final JButton myLoadItemButton;
	private final Bidder myBidder;
	private final AuctionCalendar myCalendar;
	private JList<Auction> myAuctionsList;
	private JList<Bid> myBidsList;

	
	public NewBidderGui(final Bidder theBidder, final AuctionCalendar theCalendar) {	
		myCalendar = theCalendar;
		myBidder = theBidder;
		myObservable = new ObservableBidderGui();
		mySelectedAuction = null;
		mySelectedItem = null;
		myLoadAuctionButton = createLoadAuctionButton();
		myLoadItemButton = createLoadItemButton();
		this.setLayout(new GridLayout(1, 2));
		this.add(createAuctionsPanel());
		this.add(createBidsPanel());
	}
	
	private JPanel createAuctionsPanel() {
		final JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		final JLabel label = new JLabel("Open auctions", SwingConstants.CENTER);

		final DefaultListModel<Auction> auctionsList = createAuctionsList();
		myAuctionsList = new JList<>(auctionsList);
		myAuctionsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		myAuctionsList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(final ListSelectionEvent theEvent) {
				final JList<Auction> list = (JList<Auction>) theEvent.getSource();
				mySelectedAuction = list.getSelectedValue();
			}
		});

		panel.add(label, BorderLayout.NORTH);
		panel.add(new JScrollPane(myAuctionsList), BorderLayout.CENTER);
		panel.add(myLoadAuctionButton, BorderLayout.SOUTH);
		return panel;
	}
	
	private JPanel createBidsPanel() {
		final JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		final JLabel label = new JLabel("Your bids", SwingConstants.CENTER);
		
		final DefaultListModel<Bid> bidsList = createBidsList();
		myBidsList = new JList<>(bidsList);
		myBidsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		myBidsList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(final ListSelectionEvent theEvent) {
				final JList<Bid> list = (JList<Bid>) theEvent.getSource();
				mySelectedItem = list.getSelectedValue().getItem();
			}
		});
		
		panel.add(label, BorderLayout.NORTH);
		panel.add(new JScrollPane(myBidsList), BorderLayout.CENTER);
		panel.add(myLoadItemButton, BorderLayout.SOUTH);

		return panel;
	}
	
	private DefaultListModel<Bid> createBidsList() {
		final DefaultListModel<Bid> list = new DefaultListModel<>();
		for(final Bid b : myBidder.getBids()) {
			System.out.println(b.toString());
			list.addElement(b);
		}
		return list;
	}
	
	private DefaultListModel<Auction> createAuctionsList() {
		final DefaultListModel<Auction> list = new DefaultListModel<>();
		for(final Auction a : myCalendar.getActiveAuctions()) {
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
				myObservable.setChangedTrue();
				myObservable.notifyObservers(mySelectedAuction);
			}
			
		});
		return button;	
	}
	
	private JButton createLoadItemButton() {
		final JButton button = new JButton("View item");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent theEvent) {
				myObservable.setChangedTrue();
				myObservable.notifyObservers(mySelectedItem);
			}
			
		});
		return button;	
	}
	
	public ObservableBidderGui getObservableBidderGui() {
		return myObservable;
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
	
	public class ObservableBidderGui extends Observable {
		public ObservableBidderGui() {
			
		}
		
		public void setChangedTrue() {
			this.setChanged();
		}
	}
}
