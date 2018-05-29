package GUI;

import storage.DataHandler;
import users.AuctionStaff;
import users.Bidder;
import users.ContactPerson;
import users.User;

import javax.swing.*;

import auctiondata.Auction;
import auctiondata.AuctionItem;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;

/**
 * Creates the frame for the auction central program.
 * @Author Tanner Brown
 * @Version 26 May 2018
 */
public class GUIFrame extends JFrame implements Observer {

	private JPanel currentPanel;
	private JPanel basePanel;
	private JPanel bidderPanel;
	private DataHandler myData;
	private User activeUser;

	public GUIFrame(){

		setName("TCSS 305 Tetris");
		setLocationRelativeTo(null);

		//Properly logs out if user hits the 'x' on the frame.
		addWindowListener(new WindowAdapter() {
			/**
			 * Invoked when a window is in the process of being closed.
			 * The close operation can be overridden at this point.
			 *
			 * @param e
			 */
			@Override
			public void windowClosing(WindowEvent e) {
					quitProgram();
			}
		});

		GUIToolBar myToolBar = new GUIToolBar(this);
		setJMenuBar(myToolBar);

		myData = new DataHandler();
		basePanel = new JPanel(new BorderLayout());

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(basePanel);


		LoginPanel login = new LoginPanel(myData, this);
		currentPanel = login;
		basePanel.setLayout(new BorderLayout());
		basePanel.add(currentPanel, BorderLayout.CENTER);
		
		pack();
		
		final Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
		final int x = (int) ((screenDim.getWidth() - this.getWidth()) / 2);
		final int y = (int) ((screenDim.getHeight() - this.getHeight()) / 2);
		this.setLocation(x, y);
		
		setVisible(true);
	}

	/**
	 * Logs in a bidder user.
	 * @param theBidder the user being logged in.
	 */
	public void loginBidder(Bidder theBidder){
		activeUser = theBidder;
		BidderGUI bidderGUI = new BidderGUI(theBidder, myData);
		bidderGUI.addObserver(this);
		changePanel(bidderGUI.getPanel());
		bidderPanel = bidderGUI.getPanel();

	}

	/**
	 * Logs in a contact user.
	 * @param theContact the user logging in.
	 */
	public void loginContact(ContactPerson theContact){
		activeUser = theContact;
		ContactGUI contactGUI = new ContactGUI(theContact, myData);
		contactGUI.addObserver(this);
		changePanel(contactGUI.getPanel());
	}

	/**
	 * Logs in a staff user.
	 * @param theStaff the user logging in.
	 */
	public void loginStaff(AuctionStaff theStaff){
		activeUser = theStaff;
		StaffGUI staffGUI = new StaffGUI(theStaff, myData, myData.getMyAuctionCalendar());
		changePanel(staffGUI);
	}

	/**
	 * Changes the main panel within the frame.
	 * @param theNewPanel the panel that will take over the previous one.
	 */
	public void changePanel(JPanel theNewPanel){
		basePanel.remove(currentPanel);
		currentPanel = theNewPanel;
		basePanel.add(theNewPanel, BorderLayout.CENTER);
		pack();
	}


	/**
	 * This method is called whenever the observed object is changed. An
	 * application calls an <tt>Observable</tt> object's
	 * <code>notifyObservers</code> method to have all the object's
	 * observers notified of the change.
	 *
	 * @param o   the observable object.
	 * @param arg an argument passed to the <code>notifyObservers</code>
	 */
	@Override
	public void update(Observable o, Object arg) {
		if(o instanceof BidderGUI) {
			if(arg instanceof Auction) {
				AuctionGUI gui = new AuctionGUI((Auction) arg, myData, (Bidder) activeUser);
				changePanel(gui.getPanel());
				gui.addObserver(this);
			} else if (arg instanceof AuctionItem) {
				AuctionItemGUI gui = new AuctionItemGUI((AuctionItem) arg, (Bidder) activeUser, myData);
				changePanel(gui.getPanel());
				gui.addObserver(this);
			}
		} else if(o instanceof ContactGUI) {

		} else if(o instanceof AuctionGUI) {
			if(arg instanceof AuctionItem) {
				final AuctionItemGUI gui = new AuctionItemGUI((AuctionItem) arg, (Bidder) activeUser, myData);
				changePanel(gui.getPanel());
				gui.addObserver(this);
			} else {
				BidderGUI bidderGUI = new BidderGUI((Bidder) activeUser, myData);
				bidderGUI.addObserver(this);
				changePanel(bidderGUI.getPanel());
			}
		} else if(o instanceof AuctionItemGUI) {
			BidderGUI bidderGUI = new BidderGUI((Bidder) activeUser, myData);
			bidderGUI.addObserver(this);
			changePanel(bidderGUI.getPanel());

		}
	}

	/**
	 * Prompts the user to ensure they want to log out.
	 */
	protected void logoutPrompt(){
		int confirm = JOptionPane.showOptionDialog(
				null, "Are you sure you want to logout of Auction Central?",
				"Exit Confirmation", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, null, null);
		if(confirm == 0){
			JOptionPane.showMessageDialog(null, "Thank you for using Auction Central. \n GoodBye!");
			logout();
		}

	}

	/**
	 * Logs out the current user and returns to login panel.
	 */
	private void logout(){
		myData.serialize();
		activeUser = null;
		LoginPanel login = new LoginPanel(myData, this);
		changePanel(login);
	}

	/**
	 * Logs out current user and exits program.
	 */
	protected void quitProgram(){
		int confirm = JOptionPane.showOptionDialog(
				null, "Are you sure you want to quit Auction Central?",
				"Exit Confirmation", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, null, null);
		if(confirm == 0){
			JOptionPane.showMessageDialog(null, "Thank you for using Auction Central. \n GoodBye!");
			myData.serialize();
			System.exit(0);
		}
	}
}
