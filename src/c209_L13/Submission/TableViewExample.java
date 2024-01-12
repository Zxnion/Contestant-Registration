package c209_L13;



import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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

public class TableViewExample extends Application {
	
	private TableView<Participant> tableView = new TableView<>();
	private TextField nricField = new TextField();
	private TextField nameField = new TextField();
	private TextField mobileField = new TextField();
	private Button addBtn = new Button("Add");
	private Button deleteBtn = new Button("Delete");
	private HBox fieldsBox = new HBox();
	private HBox buttonsBox = new HBox();
	private VBox vPane = new VBox();

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
		
		vPane.getChildren().addAll(tableView, fieldsBox, buttonsBox);
		vPane.setSpacing(10);
		vPane.setPadding(new Insets(10,10,10,10));
		vPane.setAlignment(Pos.TOP_CENTER);
		
		Scene mainScene = new Scene(vPane);
		primaryStage.setTitle("Contest Registration");
		primaryStage.setWidth(600);
		primaryStage.setHeight(400);
		primaryStage.setScene(mainScene);
		primaryStage.show();
		
	}

	
	private void loadParticipants() {
		// Create some Participant objects
		
		    participants.add(new Participant("S1234567A", "John Doe", 12345678));
		    participants.add(new Participant("T9876543B", "Jane Smith", 98765432));
		    participants.add(new Participant("G5678901C", "Alice Johnson", 55555555));

  		    tableView.setItems(participants);
		
		
	}
	
	private void add() {
		Participant p = new Participant(nricField.getText(), nameField.getText(), Integer.parseInt(mobileField.getText()));
		
		participants.add(p);
	}
	
	private void delete() {
		
		int row = tableView.getSelectionModel().getSelectedIndex();
		if (row >= 0) {
			participants.remove(row);
		}
		System.out.println(participants.size());
	}
	

	
	private void update(CellEditEvent<Participant, Integer> e) {
		Participant p = e.getRowValue();
		p.setMobileNum(e.getNewValue());
		int row = tableView.getSelectionModel().getSelectedIndex();
		System.out.println(participants.get(row).getMobileNum());
	}
}
