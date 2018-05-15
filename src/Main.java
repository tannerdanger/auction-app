import GUI.BidderGUI;
import auctiondata.Auction;
import storage.*;
import users.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 *
 */
public class Main {

    public static void main(String[] args) {
		//ConsoleUI.start();
	   runAuctionCentral();
    }

	private static void runAuctionCentral() {

		DataHandler data = new DataHandler();
		AuctionCalendar calendar = new AuctionCalendar();
		UserDB userData = data.getMyUserDB();
		User theUser = login(userData);


		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run(){

				if(Bidder.class.equals(theUser.getClass()))
					new BidderGUI((Bidder)theUser, calendar);
				else if(ContactPerson.class.equals(theUser.getClass())) {
					//Do other stuff
				}
					//new ContactPersonGUI((ContactPerson)theUser, calendar);
			}
		});
	}

	/**
	 * This method creates a login prompt allows a user to log in.
	 *
	 * @param userData a list of acceptable users who can login.
	 * @return the user that is logged in.
	 */
	private static User login(UserDB userData) {

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel panel = new JPanel(new BorderLayout(5,5));

		JPanel label = new JPanel(new GridLayout(0,1,2,3));

		label.add(new JLabel("Please enter your e-mail to login", SwingConstants.CENTER));
		//label.add(new JLabel("Email", SwingConstants.RIGHT));

		panel.add(label, BorderLayout.WEST);

		JPanel controls = new JPanel(new GridLayout(0,1,2,3));
		JTextField username = new JTextField();
		controls.add(username, 0,0);
		//controls.add(new JLabel("Email", SwingConstants.LEFT), 0,0);



		panel.add(controls, BorderLayout.SOUTH);

		User returnUser;

		//TODO: Change the question message with a icon for auction central
		JOptionPane.showMessageDialog(frame, panel, "Auction Central Login", JOptionPane.QUESTION_MESSAGE);

		returnUser = userData.getUser(username.getText());
		//TODO: While loop to verify data



    	return returnUser;
	}
}
