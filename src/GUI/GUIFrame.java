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

public class GUIFrame extends JFrame implements Observer {

	public JPanel currentPanel;
	public JPanel basePanel;
	public JPanel bidderPanel;
	private DataHandler myData;
	private User activeUser;
	public GUIFrame(){

		//Sets logout/default close operations
		addWindowListener(new WindowAdapter() {
			/**
			 * Invoked when a window is in the process of being closed.
			 * The close operation can be overridden at this point.
			 *
			 * @param e
			 */
			@Override
			public void windowClosing(WindowEvent e) {
				int confirm = JOptionPane.showOptionDialog(
						null, "Are you sure you want to exit Auction Central?",
						"Exit Confirmation", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, null, null);
				if(confirm == 0){
					JOptionPane.showMessageDialog(null, "Thank you for using Auction Central. \n GoodBye!");
					logout();
				}
			}
		});

		myData = new DataHandler();
		basePanel = new JPanel(new BorderLayout());

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(basePanel);


		LoginPanel login = new LoginPanel(myData, this);
		currentPanel = login;
		basePanel.setLayout(new BorderLayout());
		basePanel.add(currentPanel, BorderLayout.CENTER);

		setVisible(true);
		pack();
	}

	public void loginBidder(Bidder theBidder){
		activeUser = theBidder;
		BidderGUI bidderGUI = new BidderGUI(theBidder, myData);
		bidderGUI.addObserver(this);
		changePanel(bidderGUI.getPanel());
		bidderPanel = bidderGUI.getPanel();

	}
	public void loginContact(ContactPerson theContact){
		activeUser = theContact;
		ContactGUI contactGUI = new ContactGUI(theContact, myData);
		contactGUI.addObserver(this);
		changePanel(contactGUI.getPanel());
	}
	public void loginStaff(AuctionStaff theStaff){
		activeUser = theStaff;
		StaffGUI staffGUI = new StaffGUI(myData, myData.getMyAuctionCalendar());
		changePanel(staffGUI);
	}

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
				AuctionGUI gui = new AuctionGUI((Auction) arg, myData);
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

	private void logout(){
		myData.serialize();
		System.out.println("Thank you for using Auction Central!");
		System.out.println("You have sucessfully logged out. Goodbye! ");
		System.exit(0);
	}
}
