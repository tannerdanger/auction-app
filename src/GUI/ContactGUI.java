package GUI;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import auctiondata.Auction;
import auctiondata.AuctionItem;
import storage.DataHandler;
import users.ContactPerson;

public class ContactGUI extends Observable implements Observer {

	private Auction myActiveAuction;
	private Auction mySelectedAuction;
	private ArrayList<Auction> myAllAuctions;

	private final JPanel myPanel;
	private final JPanel myMainCenterPanel;
	private final JPanel myActiveAuctionsPanel;
	private final JPanel myAuctionsHistoryPanel;
	private JPanel mySubmitAuctionPanel;

	private JPanel myViewAllItemsPanel;
	private JPanel myAddNewAuctionItemPanel;
	private String myNewItemName;

	private final CardLayout myCardLayout;
	private final JButton myActiveAuctionButton;
	private final JButton myNewAuctionButton;
	private final ContactPerson myContactPerson;
	private DataHandler myData;
	private final JButton myHomeButton = new JButton("Home");
	private final JButton myAuctionHistoryButton = new JButton("View Auction History");
	private final JButton myNewAuctionRequestButton = new JButton("Submit New Auction Request");
	private final JButton mySubmitButton = new JButton("Submit");


	public ContactGUI(final ContactPerson theContactPerson, final DataHandler theData) {
		myData = theData;
		myContactPerson = theContactPerson;
		myAllAuctions = myData.getAuctionsByOrg(myContactPerson.getMyOrgName());
		myActiveAuctionButton = createActiveAuctionButton();
		myNewAuctionButton = createNewAuctionRequestButton();
		myActiveAuction = myContactPerson.getCurrentAuction();
		mySelectedAuction = null;
		myPanel = new JPanel();
		myMainCenterPanel = new JPanel();
		myActiveAuctionsPanel = createActiveAuctionsPanel();
		myAuctionsHistoryPanel = createAuctionHistoryPanel();
		mySubmitAuctionPanel = null;

		myViewAllItemsPanel = createViewAllItemsPanel();       
		myAddNewAuctionItemPanel = createAddNewAuctionItemPanel();

		myCardLayout = new CardLayout();
		myPanel.setLayout(new BorderLayout());
		myPanel.setMinimumSize(new Dimension(720, 720));
		final JLabel label = new JLabel("Welcome" + myContactPerson.getFirstName(), SwingConstants.CENTER);
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

		JLabel activeAuctionsLabel;
		if(myActiveAuction != null) {
			activeAuctionsLabel = new JLabel(myActiveAuction + "");
		} else {
			activeAuctionsLabel = new JLabel("You have no active auction.");
		}
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

		Object[] abc = myAllAuctions.toArray();
		JList<Object> list = new JList<Object>(abc);
		panel.add(list);
		panel.setBackground(Color.PINK);
		return panel;

	}

	private JPanel createViewAllItemsPanel() {
		final JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(0, 0));
		JLabel allItemsLabel = new JLabel("Items Available in Auction");
		allItemsLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		panel.add(allItemsLabel, BorderLayout.NORTH);
		panel.setBackground(Color.YELLOW);
		Object[] itemsArray = null;
		//TODO: Error handling... this returns void and crashes if no current auctions exist
		//Note: The below method doesn't work
		if(null != myContactPerson.getCurrentAuction()) {
			Set<AuctionItem> auctionItems = myData.getAuctionItemsByAuction(myActiveAuction);
			itemsArray = auctionItems.toArray();

		}else {
			itemsArray = new Object[1];
			itemsArray[0] = new JLabel("No auctions scheduled");
		}
		JList <Object> list = new JList<Object>(itemsArray);
		panel.add(list, BorderLayout.CENTER);

		return panel;
	}
	private JPanel createViewAllItemsPanel1(AuctionItem theItem) {
		final JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(0, 0));
		JLabel allItemsLabel = new JLabel("Items Available in Auction");
		allItemsLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		panel.add(allItemsLabel, BorderLayout.NORTH);
		panel.setBackground(Color.YELLOW);
		Object[] itemsArray = null;
		//TODO: Error handling... this returns void and crashes if no current auctions exist
		//Note: The below method doesn't work
		if(null != myContactPerson.getCurrentAuction()) {
			Set<AuctionItem> auctionItems = myData.getAuctionItemsByAuction(myActiveAuction);
			auctionItems.add(theItem);
			itemsArray = auctionItems.toArray();
			

		}else {
			itemsArray = new Object[1];
			itemsArray[0] = new JLabel("No auctions scheduled");
		}
		JList <Object> list = new JList<Object>(itemsArray);
		panel.add(list, BorderLayout.CENTER);

		return panel;
	}

	private JPanel createAddNewAuctionItemPanel() {
		final JPanel panel = new JPanel();

		panel.setLayout(new BorderLayout(0, 0));

		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.CENTER);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{0, 0, 0};
		gbl_panel_1.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_panel_1.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);

		JLabel lblAuctionItemDetails = new JLabel("Auction Item Details");
		GridBagConstraints gbc_lblAuctionItemDetails = new GridBagConstraints();
		gbc_lblAuctionItemDetails.insets = new Insets(0, 0, 5, 0);
		gbc_lblAuctionItemDetails.gridx = 1;
		gbc_lblAuctionItemDetails.gridy = 0;
		panel_1.add(lblAuctionItemDetails, gbc_lblAuctionItemDetails);

		JLabel lblItemName = new JLabel("Item Name");
		GridBagConstraints gbc_lblItemName = new GridBagConstraints();
		gbc_lblItemName.anchor = GridBagConstraints.EAST;
		gbc_lblItemName.insets = new Insets(0, 0, 5, 5);
		gbc_lblItemName.gridx = 0;
		gbc_lblItemName.gridy = 1;
		panel_1.add(lblItemName, gbc_lblItemName);

		JTextField itemName = new JTextField();
		GridBagConstraints gbc_textField_2 = new GridBagConstraints();
		gbc_textField_2.insets = new Insets(0, 0, 5, 0);
		gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_2.gridx = 1;
		gbc_textField_2.gridy = 1;
		panel_1.add(itemName, gbc_textField_2);
		itemName.setColumns(10);

		JLabel lblNewLabel = new JLabel("Minimum Bid");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 2;
		panel_1.add(lblNewLabel, gbc_lblNewLabel);

		JTextField minimumBid = new JTextField();
		GridBagConstraints gbc_textField_3 = new GridBagConstraints();
		gbc_textField_3.insets = new Insets(0, 0, 5, 0);
		gbc_textField_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_3.gridx = 1;
		gbc_textField_3.gridy = 2;
		panel_1.add(minimumBid, gbc_textField_3);
		minimumBid.setColumns(10);


		GridBagConstraints gbc_mySubmitButton = new GridBagConstraints();
		gbc_mySubmitButton.gridx = 1;
		gbc_mySubmitButton.gridy = 3;
		panel_1.add(mySubmitButton, gbc_mySubmitButton);

		mySubmitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent theEvent) {

				JOptionPane.showMessageDialog(null, "Your Item \"" +itemName.getText() + "\" with minimum bid of $" + minimumBid.getText() + " Has Been Saved!");
				System.out.println(minimumBid.getText());
				
				
				double bidPrice = Double.parseDouble(minimumBid.getText());
				String strItemName = itemName.getText();
				
				AuctionItem newAuctionItem = new AuctionItem(bidPrice, strItemName, myActiveAuction);
				myViewAllItemsPanel = createViewAllItemsPanel1(newAuctionItem);   
				myActiveAuction.addInventoryItem(newAuctionItem);
				
				itemName.setText("");
				minimumBid.setText("");
			}

		});

		panel.setBackground(Color.CYAN);
		return panel;
	}


	private JPanel createAuctionSubmitPanel() {
		final JPanel panel = new JPanel();
		JLabel confirmation = new JLabel("Your new auction request has been submitted!");
		panel.add(confirmation);

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
		myNewAuctionRequestButton.setEnabled(null == myContactPerson.getCurrentAuction()
				|| myContactPerson.getCurrentAuction().getAuctionDate().isBefore(LocalDate.now()));

		if(!myNewAuctionRequestButton.isEnabled()){
			myNewAuctionRequestButton.setToolTipText("Cannot submit an auction while you currently have an active auction");
		}
		myNewAuctionRequestButton.addActionListener(e ->{

		new MultiDateSelector(this);
		mySubmitAuctionPanel = createAuctionSubmitPanel();
	});

		return myNewAuctionRequestButton;
}

	private JButton createHomeButton() {
		myHomeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent theEvent) {
				myCardLayout.show(myMainCenterPanel, "#1active");
				myAuctionHistoryButton.setEnabled(true);
				//myNewAuctionRequestButton.setEnabled(true);
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
		if(theObservable instanceof DataHandler && theObject instanceof Auction) {
			createActiveAuctionsPanel();
			createAddNewAuctionItemPanel();
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
