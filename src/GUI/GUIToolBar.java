package GUI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class GUIToolBar extends JMenuBar{

	public JMenu myFileMenu;
	public GUIFrame myFrame;
	
	
	
	public GUIToolBar(GUIFrame theFrame) {
		super();
		myFrame = theFrame;
		build();
	}

	private void build() {
		myFileMenu = new JMenu("Options");
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
