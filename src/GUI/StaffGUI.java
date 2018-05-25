package GUI;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import auctiondata.Auction;
import storage.AuctionCalendar;
import storage.DataHandler;

import java.awt.*;
import java.awt.event.ActionListener;

public class StaffGUI extends JPanel {

	private AuctionCalendar myCalendar;
	private DataHandler myData;

	private JTextField txtUpdateMaxNumber;
	private JTextField txtNextAuction;

	private JButton updateTotalNumBtn;
	private JButton updateMaxDayBtn;
	private JButton btnStartDate;
	private JButton btnEndDate;
	private JButton btnViewAll;

	private JList<Auction> myAuctionsList;


	/** A textpane that displays number of scheduled auctions */
	private JLabel numOfAuctionsLabel;

	/** A textpane that displays the details of the next auction */
	private JTextPane nextAuctionDetailsPane;


	/** A progress bar showing how close to max auctions we are */
	private JProgressBar auctionsProgBar;

	public StaffGUI(DataHandler theData, AuctionCalendar theCalendar){

		myCalendar = theCalendar;
		myData = theData;

		build();
		updateAll();
	}

	private void updateAll() {
		updateNumOfAuctionsPane();
		updateNextAuctionPane();
	}

	private void updateNextAuctionPane() {
		StringBuilder sb = new StringBuilder();
		Auction nextAuction = myData.getNextAuction();
		sb.append("Date: " +nextAuction.getAuctionDate().toString() + "  ID:" + nextAuction.getauctionID());
		sb.append("\nOrg: "+nextAuction.getOrgName()+"   Contact:  "+ myData.getContactForOrg(nextAuction.getOrgName()));
		nextAuctionDetailsPane.setText(sb.toString());
	}

	private void updateNumOfAuctionsPane() {
		String lblString = String.valueOf(myData.getActiveAuctions().size());
		lblString+="/";
		lblString+= myCalendar.getMAX_UPCOMING_AUCTIONS();
		numOfAuctionsLabel.setText(lblString);
		numOfAuctionsLabel.setHorizontalAlignment(SwingConstants.CENTER);
	}

	private void build() {

		setLayout(new BorderLayout(0, 0));

		buildButtons();
		buildTopPanel();
		buildBotPanel();
		JPanel leftBuffer = new JPanel();
		JPanel rightBuffer = new JPanel();
		//TODO: Make this work to add a buffer on each side of the panel
		//leftBuffer.setMinimumSize(new Dimension(50,0));
		//rightBuffer.setSize(50,0);
		add(leftBuffer, BorderLayout.WEST);
		add(rightBuffer, BorderLayout.EAST);

		
	}

	private void buildButtons() {
		btnStartDate = new JButton("Start Date");
		btnEndDate = new JButton("End Date");
		btnViewAll = new JButton("View All");
		updateTotalNumBtn = new JButton("Update Total Cap");
		updateMaxDayBtn = new JButton("Update Day Cap");

		btnStartDate.setVisible(true);
		btnEndDate.setVisible(true);
		btnViewAll.setVisible(true);
		updateTotalNumBtn.setVisible(true);
		updateMaxDayBtn.setVisible(true);

		btnStartDate.addActionListener(e -> {

		});
		btnEndDate.addActionListener(e -> {

		});

		btnViewAll.addActionListener(e -> {

		});

		updateTotalNumBtn.addActionListener(e ->{
			String response = JOptionPane.showInputDialog(null, "Set the new total number of auctions Auction Central will process" +
					"\n the current max is "+myCalendar.getMAX_UPCOMING_AUCTIONS());

			if(!(null==response) && !("".compareTo(response)==0)) {
				int newMax = Integer.parseInt(response);
				myCalendar.setMAX_UPCOMING_AUCTIONS(newMax);
				updateAll();
			}
		});

		updateMaxDayBtn.addActionListener(e -> {
			String response = JOptionPane.showInputDialog(null,
					"Set the new total number of auctions in a day Auction Central will process" +
					"\n the current max is "+myCalendar.getMAX_AUCTIONS_DAY());
			if(!(null==response) && !("".compareTo(response)==0)) {
				int newMax = Integer.parseInt(response);
				myCalendar.setMAX_AUCTIONS_DAY(newMax);
				updateAll();
			}
		});
	}

	/**
	 * Creates the top panel that goes in the north borderlayout of the base panel
	 */
	private void buildTopPanel() {

		//Create Panel
		JPanel topPanel = new JPanel(new GridLayout(1,3));
		add(topPanel, BorderLayout.NORTH);

		//Create panel to display auction count (left side)
		JPanel scheduledAuctionNumPanel = new JPanel(new GridLayout(1, 0, 0, 0));
		numOfAuctionsLabel = new JLabel(String.valueOf(myData.getActiveAuctions().size()) + "/" + myCalendar.getMAX_UPCOMING_AUCTIONS());
		//Font font = new Font("Helvetica", Font.BOLD, 18);
		numOfAuctionsLabel.setFont(new Font("Helvetica", Font.BOLD, 64));
		numOfAuctionsLabel.setForeground(Color.RED);

//		//Create progress bar below display auction count
//		JPanel progressbarPanel = new JPanel();
//		auctionsProgBar = new JProgressBar();

		//Create active auction panel (center)
		JPanel activeAuctionPanel = new JPanel(new GridLayout(2,0));
		JTextField textPane = new JTextField();
		textPane.setText("-- Next Auction --");
		textPane.setHorizontalAlignment(SwingConstants.CENTER);
		nextAuctionDetailsPane = new JTextPane();

		//Create panel to display update buttons (right)
		JPanel updateCapacityPanel = new JPanel(new GridLayout(3,1));
		JLabel capPanelLabel = new JLabel("Update Auction Capacity", SwingConstants.CENTER);


		//Add panels to proper place
		scheduledAuctionNumPanel.add(numOfAuctionsLabel);
		//scheduledAuctionNumPanel.add(progressbarPanel);
		//progressbarPanel.add(auctionsProgBar);

		activeAuctionPanel.add(textPane);
		activeAuctionPanel.add(nextAuctionDetailsPane);

		updateCapacityPanel.add(capPanelLabel);
		updateCapacityPanel.add(updateTotalNumBtn);
		updateCapacityPanel.add(updateMaxDayBtn);

		topPanel.add(scheduledAuctionNumPanel);
		topPanel.add(activeAuctionPanel);
		topPanel.add(updateCapacityPanel);

	}

	private void buildBotPanel() {
		JPanel botPanel = new JPanel(new BorderLayout());

		//Create the panel that goes in the middle of the bottom panel
		JPanel midBotPanel = new JPanel(new GridLayout(2,0));
		JLabel textField = new JLabel("-- Auction Data --", SwingConstants.CENTER);
		JPanel buttonPanel = new JPanel(new GridLayout(1,5));


		buttonPanel.add(new JPanel()); //buffer panel
		buttonPanel.add(btnStartDate);
		buttonPanel.add(btnEndDate);
		buttonPanel.add(btnViewAll);
		buttonPanel.add(new JPanel()); //buffer panel


		JPanel mainBotPanel = new JPanel(new BorderLayout());
		
		DefaultListModel<Auction> auctionList = createAuctionListModel();
		myAuctionsList = new JList<>(auctionList);
		setupAuctionJList();
		myAuctionsList.setCellRenderer(new AuctionJListRenderer());
		mainBotPanel.add(new JScrollPane(myAuctionsList), BorderLayout.CENTER);

		add(botPanel, BorderLayout.SOUTH);
		midBotPanel.add(textField);
		midBotPanel.add(buttonPanel);
		botPanel.add(midBotPanel, BorderLayout.NORTH);
		botPanel.add(mainBotPanel, BorderLayout.CENTER);

	}

	/**
	 * @author Charlie
	 */
	private void setupAuctionJList() {
		myAuctionsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//below commented out unless it is determined that a staffer needs to see auction data specifically

//		myAuctionsList.addListSelectionListener(new ListSelectionListener() {
//			@Override
//			public void valueChanged(ListSelectionEvent theEvent) {
//				final JList<Auction> list = (JList<Auction>) theEvent.getSource();
//				mySelectedAuction = list.getSelectedValue();
//				if(list.isSelectionEmpty())	 {
//					myLoadAuctionButton.setEnabled(false);
//				} else {
//					myLoadAuctionButton.setEnabled(true);
//				}
//			}
//		});
	}

	private DefaultListModel<Auction> createAuctionListModel() {
		final DefaultListModel<Auction> list = new DefaultListModel<>();
		for(final Auction a : myData.getActiveAuctions()) { //<--Tanner's Change
			list.addElement(a);
		}
		return list;
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
}
