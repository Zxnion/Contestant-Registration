/**
 * I declare that this code was written by me.
 * I will not copy or allow others to copy my code.
 * I understand that copying code is considered as plagiarism.
 *
 * andy_lee, 15 Jul 2021 2:41:07 pm
 */

package c209_L08;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ContestReg_Add extends Application {
	private VBox vbPane = new VBox();
	private Label lbWelcome = new Label("Contest Registration\n     Add Applicant");
	private Label lbName = new Label("Name");
	private Label lbNRIC = new Label("NRIC");
	private Label lbMobileNo = new Label("Mobile No.");
	private Label lbStatus = new Label(""); // initially blank
	
	private TextField tfName = new TextField();
	private TextField tfNRIC = new TextField();
	private TextField tfMobileNo = new TextField();
	
	private Button btAdd = new Button("ADD Applicant");

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}
	
	public void start(Stage primaryStage) {
		String jdbcURL = "jdbc:mysql://localhost/demodb";
		String dbUsername = "root";
		String dbPassword = "";

		DBUtil.init(jdbcURL, dbUsername, dbPassword);
		
		vbPane.setSpacing(10);
		vbPane.setPadding(new Insets(10,10,10,10));
		vbPane.setAlignment(Pos.TOP_CENTER);
		
		vbPane.getChildren().addAll(lbWelcome, lbName, tfName,
				lbNRIC, tfNRIC, lbMobileNo, tfMobileNo,
				btAdd, lbStatus);
		Scene myScene = new Scene(vbPane);
		primaryStage.setScene(myScene);
				
		primaryStage.setTitle("Add Applicant");
		primaryStage.setWidth(300);
		primaryStage.setHeight(400);
		
		primaryStage.show();
		
		EventHandler<ActionEvent> handleAdd = (ActionEvent e) -> doAdd();
		btAdd.setOnAction(handleAdd);
	}
	
	private void doAdd() {
		String name = tfName.getText();
		String nric = tfNRIC.getText();
		int mobileNumber = Integer.parseInt(tfMobileNo.getText());

		String insertSql = "INSERT INTO participant(name, nric, mobile_number) VALUES('" + name + "', '" 
		   + nric + "', " + mobileNumber + ")";
		int rowsAdded = DBUtil.execSQL(insertSql);

		if (rowsAdded == 1) {

			lbStatus.setText("Participant with NRIC " + nric + " registered!");

		} else {
			lbStatus.setText("Registration failed!");
		}
	}


}
