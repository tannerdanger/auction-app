package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import storage.DataHandler;
import users.Bidder;

public class BidderGUI extends Observable implements Observer {

	private Auction mySelectedAuction;
	private AuctionItem mySelectedItem;
	private final JPanel myPanel;
	private final JButton myLoadAuctionButton;
	private final JButton myLoadItemButton;
	private final Bidder myBidder;
	private DataHandler myData;
	private JList<Auction> myAuctionsList;
	private JList<Bid> myBidsList;

	
	public BidderGUI(final Bidder theBidder, final DataHandler theData) {
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
	}
	
	private JPanel createAuctionsPanel() {
		final JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		final JLabel label = new JLabel("Open auctions", SwingConstants.CENTER);

		final DefaultListModel<Auction> auctionsList = createAuctionListModel();
		myAuctionsList = new JList<>(auctionsList);
		setupAuctionJList();
		myAuctionsList.setCellRenderer(new AuctionJListRenderer());

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
		myBidsList = new JList<>(bidsList);
		setupBidsJList();
		myBidsList.setCellRenderer(new BidJListRenderer());
		
		panel.add(label, BorderLayout.NORTH);
		panel.add(new JScrollPane(myBidsList), BorderLayout.CENTER);
		panel.add(myLoadItemButton, BorderLayout.SOUTH);

		return panel;
	}

	private void setupBidsJList() {
		myBidsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		myBidsList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(final ListSelectionEvent theEvent) {
				final JList<Bid> list = (JList<Bid>) theEvent.getSource();
				mySelectedItem = list.getSelectedValue().getItem();
				if(list.isSelectionEmpty())	 {
					myLoadItemButton.setEnabled(false);
				} else {
					myLoadItemButton.setEnabled(true);
				}
			}
		});
	}
	
	private DefaultListModel<Bid> createBidsListModel() {
		final DefaultListModel<Bid> list = new DefaultListModel<>();

		for(final Bid b : myBidder.getBids()) {
			list.addElement(b);
		}
		return list;
	}
	
	private void setupAuctionJList() {
		myAuctionsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		myAuctionsList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(final ListSelectionEvent theEvent) {
				final JList<Auction> list = (JList<Auction>) theEvent.getSource();
				mySelectedAuction = list.getSelectedValue();
				if(list.isSelectionEmpty())	 {
					myLoadAuctionButton.setEnabled(false);
				} else {
					myLoadAuctionButton.setEnabled(true);
				}
			}
		});
	}
	
	private DefaultListModel<Auction> createAuctionListModel() {
		final DefaultListModel<Auction> list = new DefaultListModel<>();
		for(final Auction a : myData.getActiveAuctions()) { //<--Tanner's Change
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
		button.setEnabled(false);
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
		button.setEnabled(false);
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
			this.setText(theAuction.getAuctionDate() + " | " + theAuction.getOrgName());
			this.setOpaque(true);
			if(theIsSelected) {
				this.setForeground(Color.BLACK);
				this.setBackground(new Color(173,216,230));
			} else {
				this.setForeground(Color.BLACK);
				this.setBackground(Color.WHITE);
			}
			return this;
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
			this.setText("$" + theBid.getBidAmount() + " | " + theBid.getItem().getName());
			this.setOpaque(true);
			if(theIsSelected) {
				this.setForeground(Color.BLACK);
				this.setBackground(new Color(173,216,230));
			} else {
				this.setForeground(Color.BLACK);
				this.setBackground(Color.WHITE);
			}
			return this;
		}	
	}

	@Override
	public void update(final Observable theObservable, final Object theObject) {
		if(theObservable instanceof DataHandler) {
			if(theObject instanceof Bid) {
				myBidsList = new JList<>(createBidsListModel());
				setupBidsJList();
			} else if (theObject instanceof Auction) {
				myAuctionsList = new JList<>(createAuctionListModel());
				setupAuctionJList();
			} else if(theObject instanceof AuctionItem) {
				// do nothing
			}
		}
	}
}
