/**
 * I declare that this code was written by me.
 * I will not copy or allow others to copy my code.
 * I understand that copying code is considered as plagiarism.
 *
 * andy_lee, 14 Jul 2021 11:35:16 am
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * @author andy_lee
 *
 */
public class ContestReg_JFX extends Application {

	private VBox vbPane = new VBox();
	private HBox hbPane = new HBox();
	private Label lbAppTitleWelcome = new Label("Welcome to the Contest Registration App!");
	private Button btView = new Button("View All Applicants");
	private Button btAdd = new Button("Add Applicant");
	private Button btEdit = new Button("Edit Applicant Details");
	private Button btDelete = new Button("Delete Applicant");
	
	private TextArea taResults = new TextArea();
	
	private ArrayList<Participant> pList = new ArrayList<Participant>();

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage primaryStage) {
		String jdbcURL = "jdbc:mysql://localhost/demodb";
		String dbUsername = "root";
		String dbPassword = "";

		DBUtil.init(jdbcURL, dbUsername, dbPassword);
		
		loadParticipants();
		
		hbPane.setSpacing(10);
		hbPane.setAlignment(Pos.CENTER);
		hbPane.getChildren().addAll(btView, btAdd, btEdit, btDelete);
		
		vbPane.setSpacing(10);
		vbPane.setPadding(new Insets(10,10,10,10));
		vbPane.setAlignment(Pos.TOP_CENTER);
		vbPane.getChildren().addAll(lbAppTitleWelcome,hbPane,taResults);		
		Scene mainScene = new Scene(vbPane);
		
		primaryStage.setTitle("Contest Registration");
		primaryStage.setWidth(600);
		primaryStage.setHeight(400);
		primaryStage.setScene(mainScene);
		
		primaryStage.show();
		
		EventHandler<ActionEvent> handleView = (ActionEvent e) -> doView();
		btView.setOnAction(handleView);
		
		EventHandler<ActionEvent> handleAdd = (ActionEvent e) -> (new ContestReg_Add()).start(new Stage());
		btAdd.setOnAction(handleAdd);
		
		EventHandler<ActionEvent> handleUpdate = (ActionEvent e) -> (new ContestReg_Update()).start(new Stage());
		btEdit.setOnAction(handleUpdate);
		
		EventHandler<ActionEvent> handleDelete = (ActionEvent e) -> (new ContestReg_Delete()).start(new Stage());
		btDelete.setOnAction(handleDelete);

	}
	
	private void loadParticipants() {
		String sql = "SELECT nric, name, mobile_number FROM participant";
		ResultSet rs = DBUtil.getTable(sql);

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
			taResults.setText("SQL Error: " + e.getMessage());
		}
	}
	
	private void doView2() {
		String output = String.format("%-10s %-15s %-10s\n", "NRIC", "NAME", "MOBILE NUMBER");

		for (Participant p : pList) {
			output += String.format("%-10s %-15s %-10d\n", p.getNric(), p.getName(), p.getMobileNum());
		}
		output += "\nTotal number of participants: " + pList.size();

		taResults.setFont(Font.font("Consolas", 15));
		taResults.setText(output);
	}
	
	private void doView() {
		String sql = "SELECT nric, name, mobile_number FROM participant";
		String output = String.format("%-10s %-15s %-10s\n", "NRIC", "NAME", "MOBILE NUMBER");

		ResultSet rs = DBUtil.getTable(sql);

		try {
			while (rs.next()) {

				String nric = rs.getString("nric");
				String name = rs.getString("name");
				int mobileNum = rs.getInt("mobile_number");
				output += String.format("%-10s %-15s %-10d\n", nric, name, mobileNum);
			}

			rs.last();
			int count = rs.getRow();
			//System.out.println("Total number of participants: " + count);
			//System.out.println(output);
			output += "\nTotal number of participants: " + count;
			
			taResults.setFont(Font.font("Consolas", 15));
			taResults.setText(output);

		} catch (SQLException e) {
			taResults.setText("SQL Error: " + e.getMessage());
		}
	}
	
}
