package GUI;

import javax.swing.*;

/**
 * Creates a universal error popup message.
 * To Use: Simply call
 * new ErrorPopup("<What the user was doing when it failed>", "<What caused the failure>");
 * @author Tanner Brown
 * @version 27 May 2018
 */
public class ErrorPopup {

	public ErrorPopup(String theErrorAction, String theErrorMessage){
		JPanel panel = new JPanel();
		panel.add(new JLabel(theErrorMessage));
		JOptionPane.showMessageDialog(null, panel, theErrorAction,JOptionPane.ERROR_MESSAGE);
	}

	private JPanel getPanel(String theMessage){
		JPanel panel = new JPanel();
		panel.add(new JLabel(theMessage));

		return panel;
	}
}
