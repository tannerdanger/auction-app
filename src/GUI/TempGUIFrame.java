package GUI;

import storage.DataHandler;
import users.User;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;

public class TempGUIFrame extends JFrame implements Observer {

	public JPanel currentPanel;
	private DataHandler myData;
	private User activeUser;
	public TempGUIFrame(){

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

		LoginPanel login = new LoginPanel(myData, this);
		currentPanel = login;

		add(currentPanel);
		setVisible(true);
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

	}

	private void logout(){
		myData.deserialize();
		System.out.println("Thank you for using Auction Central!");
		System.out.println("You have sucessfully logged out. Goodbye! ");
		System.exit(0);

	}
}
