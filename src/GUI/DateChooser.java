package GUI;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.awt.Window.Type;


public class DateChooser extends JFrame {
	
	
	public DateChooser() {
		setType(Type.UTILITY);
		setTitle("Date Chooser");
		setResizable(false);
		mainPanel = new JPanel(new GridLayout(3,0));
		getContentPane().add(mainPanel, BorderLayout.CENTER);
	}
	JPanel mainPanel;

	public LocalDate DateChooser(){

		

		return LocalDate.now();

	}
}
