package GUI;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class MultiDateSelector extends JFXPanel {

	private static LocalDate[] selectedDates;

	public static LocalDate[] init() {
		// This method is invoked on Swing thread
		selectedDates = new LocalDate[2];
		JFrame frame = new JFrame("FX");
		final JFXPanel fxPanel = new JFXPanel();
		frame.add(fxPanel);
		frame.setVisible(true);

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				initFX(fxPanel);
			}
		});

		return selectedDates;
	}



	private static void initFX(JFXPanel fxPanel) {
		// This method is invoked on JavaFX thread
		Scene scene = createScene();
		fxPanel.setScene(scene);
	}

	private static Scene createScene() {
		DatePicker start = new DatePicker();
		DatePicker end = new DatePicker();

		//ObservableList<LocalDate> selectedDates = FXCollections.observableArrayList();
		start.setShowWeekNumbers(true);

		start.setOnAction(event -> {
			System.out.println("Selected date: " + start.getValue());
			selectedDates[0]=start.getValue();
		} );
		end.setOnAction(event -> {
			System.out.println("Selected date: " + end.getValue());
			selectedDates[1]=end.getValue();
		} );

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		grid.add(new Label("Start Date:"), 0, 0);
		grid.add(start, 1, 0);
		grid.add(new Label("End Date:"), 0, 1);
		grid.add(end, 1, 1);

		Scene scene = new Scene(grid);

		return scene;


	}

//	public static void main(String[] args) {
//		SwingUtilities.invokeLater(new Runnable() {
//			@Override
//			public void run() {
//				initAndShowGUI();
//			}
//		});
//	}
}