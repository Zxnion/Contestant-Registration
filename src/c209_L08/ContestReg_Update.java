/**
 * I declare that this code was written by me.
 * I will not copy or allow others to copy my code.
 * I understand that copying code is considered as plagiarism.
 *
 * andy_lee, 15 Jul 2021 2:41:07 pm
 */

package c209_L08;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ContestReg_Update extends Application {
	private VBox vbPane = new VBox();
	private HBox hbPane = new HBox();
	private Label lbWelcome = new Label("   Contest Registration\nUpdate Applicant Contact");
	private Label lbNRIC = new Label("Enter NRIC to start search");
	private Label lbName = new Label("Name");
	private Label lbMobileNo = new Label("Mobile No.");
	private Label lbStatus = new Label(""); // initially blank
	
	private TextField tfName = new TextField();
	private TextField tfNRIC = new TextField();
	private TextField tfMobileNo = new TextField();

	private Button btSearch = new Button("Search for Applicant");
	private Button btUpdate = new Button("Update Applicant Contact");
	
	private ArrayList<Participant> pList = new ArrayList<Participant>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}
	
	public void start(Stage primaryStage) {
		String jdbcURL = "jdbc:mysql://localhost/demodb";
		String dbUsername = "root";
		String dbPassword = "";

		DBUtil.init(jdbcURL, dbUsername, dbPassword);
		
		loadParticipants();
		
		vbPane.setSpacing(10);
		vbPane.setPadding(new Insets(10,10,10,10));
		vbPane.setAlignment(Pos.TOP_CENTER);
		
		hbPane.setSpacing(10);
		hbPane.setAlignment(Pos.CENTER);
		hbPane.getChildren().addAll(lbNRIC, tfNRIC, btSearch);
		
		vbPane.getChildren().addAll(lbWelcome, hbPane, 
				lbName, tfName,
				lbMobileNo, tfMobileNo,
				btUpdate, lbStatus);
		Scene myScene = new Scene(vbPane);
		primaryStage.setScene(myScene);
				
		primaryStage.setTitle("Update Applicant Contact");
		primaryStage.setWidth(500);
		primaryStage.setHeight(400);
		
		primaryStage.show();

		EventHandler<ActionEvent> handleSearch = (ActionEvent e) -> doSearch();
		btSearch.setOnAction(handleSearch);
		
		EventHandler<ActionEvent> handleUpdate = (ActionEvent e) -> doUpdate();
		btUpdate.setOnAction(handleUpdate);
	}
	
	private void loadParticipants() {
		String sql = "SELECT nric, name, mobile_number FROM participant";
		ResultSet rs = DBUtil.getTable(sql);
		
		pList.clear(); // remove all records

		try {
			while (rs.next()) {
				// User getXXX methods to extract data from rs ResultSet
				String nric = rs.getString("nric");
				String name = rs.getString("name");
				int mobileNum = rs.getInt("mobile_number");

				// Create new participant
				Participant p = new Participant(nric, name, mobileNum);
				
				// Add new participant into pList
				pList.add(p);
			}
		} catch (SQLException e) {
			lbStatus.setText("SQL Error: " + e.getMessage());
		}
	}

	
	public void doUpdate() {
		String name = tfName.getText();
		String nric = tfNRIC.getText();
		int mobileNumber = Integer.parseInt(tfMobileNo.getText());

		String updateSQL = "UPDATE participant SET mobile_number = " + mobileNumber + " WHERE nric='" + nric + "'";
		int rowsUpdated = DBUtil.execSQL(updateSQL);

		if (rowsUpdated == 1) {

			lbStatus.setText("Applicant contact info updated to " + mobileNumber + "!");
			loadParticipants();

		} else {
			lbStatus.setText("Updating failed!");
		}
	}
	
	private void doSearch() {

		String nric = tfNRIC.getText();
		boolean found = false;

		for (Participant p: pList) {
			if (nric.equals(p.getNric())) {
				found = true;
				tfName.setText(p.getName());
				tfMobileNo.setText(p.getMobileNum() + "");
				lbStatus.setText("");
				break;
			}
		}
		if (found == false) {
			tfName.setText("");
			tfMobileNo.setText("");
			lbStatus.setText("Applicant with NRIC no. " + nric + " not found...");
		}
	}


}
