package GUI;

import javafx.application.Platform;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import javax.swing.*;
import java.time.LocalDate;
import java.util.Observable;

public class MultiDateSelector extends Observable {

	private LocalDate[] selectedDates;
	StaffGUI myStaff;
	ContactGUI myContact;
	JFrame myFrame;
	private String activeClassName;
	Object myClass;
	Class myClassType;



	public void init(Object theClass) {


		// This method is invoked on Swing thread
		selectedDates = new LocalDate[2];
		myFrame = new JFrame("Date Chooser");
		final JFXPanel fxPanel = new JFXPanel();
		myFrame.add(fxPanel);
		myFrame.setVisible(true);
		myFrame.setResizable(false);



		if(theClass instanceof StaffGUI) {
			myFrame.setSize(350,175);
			myStaff = (StaffGUI)theClass;
			final Scene scene = createDoubleDateScene();

			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					initFX(scene, fxPanel);
				}
			});
		}
		else if(theClass.getClass().equals(ContactGUI.class)) {
			myFrame.setSize(350,150);
			myContact = (ContactGUI)theClass;
			final Scene scene = createSingleDateScene();

			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					initFX(scene, fxPanel);
				}
			});
		}


	}



	private void initFX(Scene theScene, JFXPanel fxPanel) {
		// This method is invoked on JavaFX thread
		fxPanel.setScene(theScene);
	}

	private Scene createDoubleDateScene() {
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
		Button submitButton = new Button("Submit");
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 10, 0, 35));

		grid.add(new Label("Start Date:"), 0, 0);
		grid.add(start, 1, 0);
		grid.add(new Label("End Date:"), 0, 1);
		grid.add(end, 1, 1);
		grid.add(submitButton, 1,2,2,2);

		submitButton.setOnAction(event -> {

			myStaff.recieveDate(selectedDates);
			myFrame.setVisible(false);

		});


		Scene scene = new Scene(grid);

		return scene;
	}

	private Scene createSingleDateScene() {
		DatePicker start = new DatePicker();

		start.setShowWeekNumbers(true);

		start.setOnAction(event -> {
			System.out.println("Selected date: " + start.getValue());
			selectedDates[0]=start.getValue();
		} );


		GridPane grid = new GridPane();
		Button submitButton = new Button("Submit");
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 10, 0, 35));

		grid.add(new Label("Auction Date:"), 0, 0);
		grid.add(start, 1, 0);
		grid.add(submitButton, 1,1,2,2);

		submitButton.setOnAction(event -> {

			myContact.recieveDate(selectedDates);
			Platform.exit();
			myFrame.dispose();
		});


		Scene scene = new Scene(grid);

		return scene;
	}


}