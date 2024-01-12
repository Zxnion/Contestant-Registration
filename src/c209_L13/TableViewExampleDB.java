package c209_L13;

/**
 * I declare that this code was written by me.
 * I will not copy or allow others to copy my code.
 * I understand that copying code is considered as plagiarism.
 *
 * 22027336, Aug 15, 2023 10:20:51 AM
 */



import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

import c209_L08.DBUtil;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

public class TableViewExampleDB extends Application {
	
	private TableView<Participant> tableView = new TableView<>();
	private TextField nricField = new TextField();
	private TextField nameField = new TextField();
	private TextField mobileField = new TextField();
	private Button addBtn = new Button("Add");
	private Button deleteBtn = new Button("Delete");
	private HBox fieldsBox = new HBox();
	private HBox buttonsBox = new HBox();
	private VBox vPane = new VBox();
	private Label labelText = new Label();
	private static final String NRIC_PATTERN = "[A-Z][0-9]{7}[A-Z]";
	private static final String MOBILE_PATTERN = "[89][0-9]{7}";
	private static final String NAME_PATTERN = "^[a-zA-Z]+(\\s[a-zA-Z]+)*$";

	// Create an ObservableList
	ObservableList<Participant> participants = FXCollections.observableArrayList();
	
	public static void main(String[] args) {
		String JDBC_URL = "jdbc:mysql://localhost/demodb";
		String DB_USERNAME = "root";
		String DB_PASSWORD = "";
		
		DBUtil.init(JDBC_URL, DB_USERNAME, DB_PASSWORD);
		
		launch(args);
	}
	
	@SuppressWarnings("unchecked")
	public void start(Stage primaryStage) {
		
		loadParticipants();
		
		
		// Create columns for your TableView
		TableColumn<Participant, String> nricColumn = new TableColumn<>("NRIC");
		nricColumn.setCellValueFactory(new PropertyValueFactory<Participant, String>("nric"));
		TableColumn<Participant, String> nameColumn = new TableColumn<>("Name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<Participant, String>("name"));
		TableColumn<Participant, Integer> mobileColumn = new TableColumn<Participant, Integer>("Mobile number");
		mobileColumn.setCellValueFactory(new PropertyValueFactory<Participant, Integer>("mobileNum"));
		
		
		// Add columns to your TableView
		tableView.getColumns().addAll(nricColumn, nameColumn, mobileColumn);
		tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
		
		nricField.setPromptText("NRIC");
		nameField.setPromptText("Name");
		mobileField.setPromptText("Mobile");
		

		fieldsBox.getChildren().addAll(nricField, nameField, mobileField);
		fieldsBox.setAlignment(Pos.CENTER);
		fieldsBox.setSpacing(10);
		
		// Add button click-action
		addBtn.setOnAction(e -> add());
		
		// Delete button click-action
		deleteBtn.setOnAction(e -> delete());
		
		// Edit mobile number
		tableView.setEditable(true);
		mobileColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
		mobileColumn.setOnEditCommit(e -> update(e));
		
		
		buttonsBox.getChildren().addAll(addBtn, deleteBtn);
		buttonsBox.setAlignment(Pos.CENTER);
		buttonsBox.setSpacing(10);
		
		labelText.setText("");
		vPane.getChildren().add(labelText);
		vPane.getChildren().addAll(tableView, fieldsBox, buttonsBox);
		vPane.setSpacing(10);
		vPane.setPadding(new Insets(10,10,10,10));
		vPane.setAlignment(Pos.TOP_CENTER);
		
		Scene mainScene = new Scene(vPane);
		primaryStage.setTitle("Contest Registration");
		primaryStage.setWidth(600);
		primaryStage.setHeight(500);
		primaryStage.setScene(mainScene);
		primaryStage.show();
		
	}

	
	private void loadParticipants() {
		// Create some Participant objects
		
		String sql = "Select nric, name, mobile_number FROM participant";
		ResultSet rs = DBUtil.getTable(sql);
		
		try {
			while (rs.next()) {
				String nric = rs.getString("nric");
				String name = rs.getString("name");
				int mobileNum = rs.getInt("mobile_number");
				
				Participant p = new Participant(nric, name, mobileNum);
				
				participants.add(p);
			}
			
		}
		
		catch (SQLException e) {
			System.out.println("SQL Eroor: " + e.getMessage());
		}
		    
  		    tableView.setItems(participants);
		
		
	}
	
	private void add() {
	    String nric = nricField.getText();
	    String name = nameField.getText();
	    String mobile = mobileField.getText();
	    
	    boolean MobV = Pattern.matches(MOBILE_PATTERN, mobile);
	    boolean NRICV = Pattern.matches(NRIC_PATTERN, nric);
	    boolean NAMEV = Pattern.matches(NAME_PATTERN, name);

	    if (!isValidInput(nric, name, mobile)) {
	        // Handle invalid input
	    } 
	    
	    else if (NRICV && MobV && NAMEV) {
	        if (participantExists(mobile)) {
	            labelText.setText("Participant with the same mobile number already exists");
	        } 
	        else {
	            int mobileNum = Integer.parseInt(mobile);
	            String insertSQL = String.format("INSERT INTO PARTICIPANT(nric, name, mobile_number) VALUES ('%s', '%s', %d)", nric, name, mobileNum);
	            int rowsAffected = DBUtil.execSQL(insertSQL);

	            if (rowsAffected == 1) {
	                labelText.setText("Participant with NRIC " + nric + " registered!");
	                participants.add(new Participant(nric, name, mobileNum));
	                nricField.clear();
	                nameField.clear();
	                mobileField.clear();
	            } 
	            else {
	                labelText.setText("Participant with " + nric + " already exists.");
	            }
	        }
	    } 
	   
	    else {
	        if (!NRICV) {
	            labelText.setText("Invalid NRIC");
	        } 
	        else if (!MobV) {
	            labelText.setText("Invalid Mobile Number");
	        }
	        else if (!NAMEV) {
	            labelText.setText("Invalid Name");
	        }
	    }
	}
 
	

	private void delete() {
		
	    int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
	    if (selectedIndex >= 0) {
	        Participant selectedParticipant = tableView.getItems().get(selectedIndex);
	        String deleteSql = "DELETE FROM participant WHERE nric = '" + selectedParticipant.getNric() + "'";
	        
	        
	        int rowsDeleted = DBUtil.execSQL(deleteSql);

	        if (rowsDeleted == 1) {
	            labelText.setText("Participant " + selectedParticipant.getName() + " deleted!");
	            participants.remove(selectedIndex);
	        } else {
	        	labelText.setText("Deletion failed!");
	        }

	        
	    }
	}


	private void update(CellEditEvent<Participant, Integer> e) {
	    Participant p = e.getRowValue();
	    int newMobile = e.getNewValue();
	    
	    boolean MobV = Pattern.matches(MOBILE_PATTERN, Integer.toString(newMobile));

	    if (MobV == true) {

		    String updateSql = "UPDATE participant SET mobile_number = " + newMobile + " WHERE nric = '" + p.getNric() + "'";
		    int rowsUpdated = DBUtil.execSQL(updateSql);
		      
		      if (rowsUpdated == 1) {
		    	  p.setMobileNum(newMobile);
		    	  labelText.setText ("Participant " + p.getName() + " successfully changed phone number to " + p.getMobileNum()); 
		    	  
		    	  
		      } 
		      else if (rowsUpdated == 0) {
		    	  labelText.setText("Failed to edit mobile number!");
		      }
		    } 
	    
	    	else if (MobV == false && newMobile >= 9) {
	    	labelText.setText("Invalid Mobile Number");
	    	
	    	}
	    
	    
		    else{
		    	labelText.setText("Invalid Mobile Number");
		    	
		    }
		    
	}


	
	private boolean isValidInput(String nric, String name, String mobile) {
	    if (nric.isEmpty() || name.isEmpty() || mobile.isEmpty()) {
	        labelText.setText("Please fill in all fields.");
	        return false;
	    }

	    try {
	        Integer.parseInt(mobile); // Check if the mobile is a valid integer
	    } catch (NumberFormatException e) {
	    	 labelText.setText("Mobile number must be a valid integer.");
	        return false;
	    }

	    return true;
	}
	
	private boolean participantExists(String mobile) {
	    for (Participant participant : participants) {
	        if (Integer.toString(participant.getMobileNum()).equals(mobile)) {
	            return true;
	        }
	    }
	    return false;
	}

	
}
