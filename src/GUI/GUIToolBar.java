package GUI;

import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * Creates a toolbar for the Auction Central program.
 * Used to allow users to logout or exit the program.
 */
class GUIToolBar extends JMenuBar{

	private GUIFrame myFrame;
	
	
	
	GUIToolBar(GUIFrame theFrame) {
		super();
		myFrame = theFrame;
		build();
	}

	private void build() {
		JMenu myFileMenu = new JMenu("Options");
		
		myFileMenu.setMnemonic(KeyEvent.VK_O);
		
		JMenuItem logoutItem = new JMenuItem("Logout");
		logoutItem.setMnemonic(KeyEvent.VK_L);
		
		logoutItem.addActionListener(e->{
			myFrame.logoutPrompt();
		});

		
		JMenuItem exitItem = new JMenuItem("Quit Auction Central");
		exitItem.setMnemonic(KeyEvent.VK_Q);

		exitItem.addActionListener(e->{
			myFrame.quitProgram();
		});
		

		myFileMenu.add(logoutItem);
		myFileMenu.add(exitItem);

		add(myFileMenu);

	}
}
