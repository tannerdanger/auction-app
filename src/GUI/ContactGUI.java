package GUI;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
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
import javax.swing.JTextPane;
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

	private Auction myActiveAuction;
	private Auction mySelectedAuction;
	private AuctionItem mySelectedItem;
	private final JPanel myPanel;
	private final JPanel myMainCenterPanel;
	private final JPanel myActiveAuctionsPanel;
	private final JPanel myAuctionsHistoryPanel;
	private final JPanel mySubmitAuctionPanel;

	private final JPanel myViewAllItemsPanel;
	private final JPanel myAddNewAuctionItemPanel;

	private final CardLayout myCardLayout;
	private final JButton myActiveAuctionButton;
	private final JButton myNewAuctionButton;
	private final ContactPerson myContactPerson;
	private DataHandler myData;
	private final JButton myHomeButton = new JButton("Home");
	private final JButton myAuctionHistoryButton = new JButton("View Auction History");
	private final JButton myNewAuctionRequestButton = new JButton("Submit New Auction Request");


	public ContactGUI(final ContactPerson theContactPerson, final DataHandler theData) {
		myData = theData;
		myContactPerson = theContactPerson;
		myActiveAuctionButton = createActiveAuctionButton();
		myNewAuctionButton = createNewAuctionRequestButton();
		myActiveAuction = myContactPerson.getCurrentAuction();
		mySelectedAuction = null;
		mySelectedItem = null;
		myPanel = new JPanel();
		myMainCenterPanel = new JPanel();
		myActiveAuctionsPanel = createActiveAuctionsPanel();
		myAuctionsHistoryPanel = createAuctionHistoryPanel();
		mySubmitAuctionPanel = createAuctionSubmitPanel();

		myViewAllItemsPanel = createViewAllItemsPanel();
		myAddNewAuctionItemPanel = createAddNewAuctionItemPanel();

		myCardLayout = new CardLayout();
		myPanel.setLayout(new BorderLayout());
		myPanel.setMinimumSize(new Dimension(720, 720));
		final JLabel label = new JLabel("Welcome " + myContactPerson.getFirstName() + " "+ myContactPerson.getLastName(), SwingConstants.CENTER);
		myPanel.add(label, BorderLayout.NORTH);
		myPanel.add(createHomeButton(), BorderLayout.EAST);
		myPanel.add(createCenterPanel(), BorderLayout.CENTER);
		myPanel.add(createButtonsPanel(), BorderLayout.SOUTH);
		myPanel.setVisible(true);




	}

	private JPanel createCenterPanel() {


		myMainCenterPanel.setLayout(myCardLayout);
		myMainCenterPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		myMainCenterPanel.setMinimumSize(new Dimension(720, 720));

		myMainCenterPanel.add(myActiveAuctionsPanel,  "#1active");
		myMainCenterPanel.add(myAuctionsHistoryPanel, "#2history");
		myMainCenterPanel.add(mySubmitAuctionPanel, "#3submit");

		myMainCenterPanel.add(myViewAllItemsPanel, "#11viewAllItems");
		myMainCenterPanel.add(myAddNewAuctionItemPanel, "#12addNewAuctionItem");

		myCardLayout.show(myMainCenterPanel, "#1active"); // show first panel(active auctions)

		return myMainCenterPanel;
	}

	private JPanel createActiveAuctionsPanel() {
		final JPanel panel = new JPanel();

		panel.setLayout(new BorderLayout(0, 0));

		JLabel lblActiveAuctions = new JLabel("Active Auctions");
		lblActiveAuctions.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		lblActiveAuctions.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblActiveAuctions, BorderLayout.NORTH);

		JLabel activeAuctionsLabel = new JLabel(myActiveAuction + "");
		activeAuctionsLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 14));

		final JPanel buttonsPanel = new JPanel();
		buttonsPanel.setBackground(Color.GREEN);

		JButton btnViewAllItems = new JButton("View All Items in Auction");
		buttonsPanel.add(btnViewAllItems);
		JButton btnAddNewAuctionItem = new JButton("Add New Auction Item");
		buttonsPanel.add(btnAddNewAuctionItem);
		panel.add(buttonsPanel, BorderLayout.SOUTH);


		btnViewAllItems.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent theEvent) {
				myCardLayout.show(myMainCenterPanel, "#11viewAllItems");
			}

		});

		btnAddNewAuctionItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent theEvent) {
				myCardLayout.show(myMainCenterPanel, "#12addNewAuctionItem");
			}

		});

		panel.add(activeAuctionsLabel, BorderLayout.CENTER);
		panel.setBackground(Color.GREEN);
		return panel;

	}

	private JPanel createAuctionHistoryPanel() {
		final JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(0, 0));

		JLabel lblHistoryAuctions = new JLabel("Auction History");
		lblHistoryAuctions.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		lblHistoryAuctions.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblHistoryAuctions, BorderLayout.NORTH);

		// acess submitted auctions and display on gui
		myContactPerson.displaySubmittedAuctions();


		panel.setBackground(Color.PINK);
		return panel;

	}

	private JPanel createViewAllItemsPanel() {
		final JPanel panel = new JPanel();
		panel.setBackground(Color.YELLOW);
		Map myMap = myActiveAuction.getInventorySheet();
		System.out.println(myMap);

		String[] data = {"one", "two", "three", "four"};
		JList<String> list = new JList<String>(data);

		panel.add(list);

		return panel;
	}

	private JPanel createAddNewAuctionItemPanel() {
		final JPanel panel = new JPanel();

		panel.setBackground(Color.CYAN);
		return panel;
	}

	private JPanel createAuctionSubmitPanel() {
		final JPanel panel = new JPanel();

		panel.setBackground(Color.BLUE);
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

		myAuctionHistoryButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent theEvent) {

				myCardLayout.show(myMainCenterPanel, "#2history");
				myAuctionHistoryButton.setEnabled(false);
				setChanged();
				notifyObservers(mySelectedAuction);
			}

		});
		return myAuctionHistoryButton;
	}

	private JButton createNewAuctionRequestButton() {

		myNewAuctionRequestButton.addActionListener(e -> {
			new MultiDateSelector().init(this);
			myCardLayout.show(myMainCenterPanel, "#3submit");
			myAuctionHistoryButton.setEnabled(true);
			setChanged();
			notifyObservers(mySelectedAuction);
		});

		return myNewAuctionRequestButton;
	}

	private JButton createHomeButton() {
		myHomeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent theEvent) {
				myCardLayout.show(myMainCenterPanel, "#1active");
				myAuctionHistoryButton.setEnabled(true);
				myNewAuctionRequestButton.setEnabled(true);
				setChanged();
				notifyObservers(mySelectedAuction);
			}

		});
		return myHomeButton;
	}

	public JPanel getPanel() {
		return myPanel;
	}



	@Override
	public void update(final Observable theObservable, final Object theObject) {
		if(theObservable instanceof DataHandler) {

		}
	}


	public void recieveDate(LocalDate[] theDate){
		if(null != theDate[0]) {
			LocalDateTime submitDate = theDate[0].atStartOfDay();
			Auction newAuction = myContactPerson.createNewAuction(submitDate);
			if(!(null == newAuction)){
				myData.addAuction(newAuction);
			}
		}
	}
}