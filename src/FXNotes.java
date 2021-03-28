
// standard javafx imports
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

// Imports for components in this application
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

// Imports for layout
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
// Import for quitting the application
import javafx.application.Platform;

/**
 * 
 */

/**
 * @author reinis janovskis 
 * Stuednt ID: 3028319
 *
 */
public class FXNotes extends Application {

	// Define variables that need class scope
	MenuBar mbMain;

	Menu mFile, mEdit, mHelp;

	MenuItem miFileNew, miFileOpen, miFileClose, miFileSave, miFileSaveAs, miFileExit;
	MenuItem miEditDelete, miEditCut, miEditCopy, miEditPaste, miEditSelectAll;
	MenuItem miHelpAbout;
	TextArea txtMain;

	// File path to active file (file we work in)
	String filePath;
	
	//Element to compare changes against (file at the start)
	StringBuilder activeFileContent;

	public FXNotes() {
		// Instantiate class variables
		mbMain = new MenuBar();

		mFile = new Menu("File");
		mEdit = new Menu("Edit");
		mHelp = new Menu("Help");

		miFileNew = new MenuItem("New");
		miFileOpen = new MenuItem("Open");
		miFileClose = new MenuItem("Close");
		miFileSave = new MenuItem("Save");
		miFileSaveAs = new MenuItem("Save As...");
		miFileExit = new MenuItem("Exit");

		miEditDelete = new MenuItem("Delete");
		miEditCut = new MenuItem("Cut");
		miEditCopy = new MenuItem("Copy");
		miEditPaste = new MenuItem("Paste");
		miEditSelectAll = new MenuItem("Select All");

		miHelpAbout = new MenuItem("About");
		txtMain = new TextArea();

		// Variable where we will hold information about file we have opened
		filePath = null;
		
		activeFileContent = new StringBuilder();

	} // constructor()

	// Takes care of actions we want to perform on elements
	@Override
	public void init() {
		// Add basic functionality for our exit button
		miFileExit.setOnAction(ae -> doExit());

		// The edit menu event
		miEditCut.setOnAction(ae -> txtMain.cut());
		miEditCopy.setOnAction(ae -> txtMain.copy());
		miEditPaste.setOnAction(ae -> txtMain.paste());
		miEditSelectAll.setOnAction(ae -> txtMain.selectAll());
		miEditDelete.setOnAction(ae -> doDelete());

		// Action for About menuItem under menu Help
		miHelpAbout.setOnAction(ae -> showAboutInformation());

		// Actions for menuItems under File
		miFileOpen.setOnAction(ae -> doFileOpenWithCheck());
		miFileSave.setOnAction(ae -> doSave());
		miFileSaveAs.setOnAction(ae -> doSaveAs());
		miFileNew.setOnAction(ae -> doNewOrClose("New"));
		miFileClose.setOnAction(ae -> doNewOrClose("Close"));

	} // init()

	
	// Check if content has changed before we kill application
	public void doExit() {
		// Check if changes has been made to file in action or if there is file at all
		if(!activeFileContent.toString().equals(txtMain.getText())) {
			// Prompt user if changes should be saved
			showPromptDialog("Exit");
		}
		// no changes since last save, just close it all
		else {
			doQuit();
		}
	} // doExit()
	
	
	// Function that will delete selected text in our text editor
	// Inspiration and idea for implementation taken from stackoverflow
	// https://stackoverflow.com/questions/15859289/how-to-make-selected-text-in-jtextarea-into-a-string
	//
	public void doDelete() {
		if (txtMain.getSelectedText() != null) {
			txtMain.deleteText(txtMain.getSelection().getStart(), txtMain.getSelection().getEnd());
		}
	} // doDelete()

	/** Simple dialog created by Alert class */
	// Taken from class project DialogSimple and adapted for our requirements
	public void showAboutInformation() {

		// Use an alert for this first dialog application
		Alert alert = new Alert(AlertType.INFORMATION);

		// Modify alert window's dimensions
		alert.setHeight(500);
		alert.setWidth(400);

		alert.setTitle("About FXNotes");
		alert.setHeaderText("Author: Reinis Janovskis\nStudent ID: 3028319");
		alert.setContentText("FXNotes is created as apart of a learning process.\n\n"
				+ "To earn Higher Diploma in Computing Science from Griffith University we have to successfully pass several courses including "
				+ "'HCI and GUI Proggramming' (HDC-HGP/Dub/FT) lead by lecturer Paddy Fahy.\n\n"
				+ "This version of FXNotes was creted in 2021 spring semester.");

		alert.showAndWait();
	} //showAboutInformation
	
	public void doFileOpenWithCheck() {
		// there are changed made to the file since last save
		if(!activeFileContent.toString().equals(txtMain.getText())) {
			// Prompt user if changes should be saved
			showPromptDialog("Open");
		}
		// no changes since last save, just close it all
		else {
			doFileOpen();
		}
	} // doFileOpenWithCheck()
	
	// Method taken from class example/task FileExample
	/** Method that opens file */
	public void doFileOpen() {
		
		FileChooser fc = new FileChooser();

		File fileToOpen = fc.showOpenDialog(null);

		// Dialog confirmed (clicked OK)
		if (fileToOpen != null) {
			
			// Try open the file and display it in the main text area.
			try {
				// Accumulate lines from file in string builder.
				StringBuilder sb = new StringBuilder();

				// Use buffered reader to open and read from the file
				FileReader fr = new FileReader(fileToOpen);
				BufferedReader buf = new BufferedReader(fr);

				// A string to store lines from BufferedReader temporarily
				String text;

				// Loop/iterate through the file one line at the time (line-by-line)
				// We assign line from buffer to string and check if it is null. buf.readLine()
				// reads next line from reader
				while ((text = buf.readLine()) != null) {
					// Paste text straight to textArea
					// txtMain.appendText(text + "\n");

					// Add newline argument at end
					text = text + "\n";
					// Add line from file to our stringBuilder
					sb.append(text);
				}

				// Done iterating through the file - end of file reached.
				// Push text from StringBuilder to textArea
				txtMain.setText(sb.toString());
				
				// Save copy of file content
				activeFileContent = sb;

				// close the file
				buf.close();

				// At the end save file path in our apps variable filePath so we would be able
				// to save it.
				filePath = fileToOpen.getPath();

			} // try
			catch (IOException ioe) {
				System.out.printf("Error opening file:\n");
				ioe.printStackTrace();
			} // catch()
		} // if
		else; // If user clicks 'Cancel' do nothing

	} // doFileOpen()

	public void doSave() {
		// We don't have file associated with content that we have in our textEditor
		// We call doSaveAs function to create new file
		if(filePath == null || filePath.isBlank() || filePath.isEmpty()) {
			doSaveAs();
		} 
		// We have file already associated with content in our text editor - just save content there
		else {
			// Try to save the file in a file that is already associated to the content in editor
			File fileToSave = new File(filePath);
			save(fileToSave);
		}
	} //doSave()

	// Method taken from class example/task FileExample
	/** method that saves a file to users specified location */
	public void doSaveAs() {

		// Use a file chooser
		FileChooser fc = new FileChooser();

		// Show the file save dialog. Assign the file name.
		File fileToSave = fc.showSaveDialog(null);

		// Test if user tried to save file (dialog confirmed)
		if (fileToSave != null) {

			save(fileToSave);
		} 
		else
			; // If user cancels dialog do nothing

	} // doSaveAs()
	
	
	// Functionality for menuItem New under File
	// Resets the text area to be blank and deletes association with previous file
	public void doNew() {
		// Select all text to be cleared
		txtMain.selectAll();
		
		// Use created delete method to clear selected text
		doDelete();
		
		// Set variable filePath to be null to break association with previous file we were using
		filePath = null;
		
		// Reset out string buffer
		activeFileContent = new StringBuilder();
		
	} //doNew()
	
	// Functionality for button close or new.
	// Both of them should check if there are any changes in text editor after previous save.
	// In case content has changed - prompt user for risk of losing changes , otherwise just prepare workspace for new task.
	// We take in 
	public void doNewOrClose(String message) {
		// Check if changes has been made to file in action or if there is file at all
		if(!activeFileContent.toString().equals(txtMain.getText())) {
			// Prompt user if changes should be saved
			showPromptDialog(message);
		}
		// no changes since last save, just close it all
		else {
			doNew();
		}
	} //doNewOrClose()
	
	//Quitting the application
	public void doQuit() {
		Platform.exit();
	} //doQuit()
	
	
	// Sample taken from DialogSimle class exercise
	/**Shows dialog for user to confirm changes made*/
	public void showPromptDialog(String message) {
		// TODO
		// Create secondary stage
		Stage dialogStage = new Stage();
		
		// Set the title
		dialogStage.setTitle(message + " Dialog");
		
		// Set width and height of stage
		dialogStage.setWidth(300);
		dialogStage.setHeight(150);
		
		// Create layout for the dialog stage
		// VBox as main container - for message and buttons
		VBox vBoxMain = new VBox();
		// HBox as container for buttons
		HBox hbButtons = new HBox();
		
		
		// Set the attributes of the VBox and HBox.
		vBoxMain.setPadding(new Insets(20));
		//vBoxMain.setAlignment(Pos.CENTER);
		
		
		hbButtons.setSpacing(10);
		
		hbButtons.setAlignment(Pos.BASELINE_RIGHT);
		
		// Create controls for the dialog layout
		Label lblPrompt = new Label("Save changes before " + message.toLowerCase() + "?");
		lblPrompt.setMinHeight(50);
		lblPrompt.setAlignment(Pos.TOP_LEFT);
		
		// Create buttons for dialog
		Button btnSave = new Button("Save");
		Button btnDontSave = new Button("Don't Save");
		Button btnCancel = new Button("Cancel");
		
		// Create button dimensions to be the same
		btnSave.setMinWidth(70);
		btnSave.setMaxHeight(20);
		
		btnDontSave.setMinWidth(70);
		btnDontSave.setMaxHeight(20);
		
		btnCancel.setMinWidth(70);
		btnCancel.setMaxHeight(20);
		
		
		// Add controls/components to the layout {column goes first and then row...}
		vBoxMain.getChildren().add(lblPrompt);
		
		hbButtons.getChildren().add(btnSave);
		hbButtons.getChildren().add(btnDontSave);
		hbButtons.getChildren().add(btnCancel);
		
		vBoxMain.getChildren().add(hbButtons);
		
		
		// Manage button events
		btnCancel.setOnAction(ae -> dialogStage.close());
		
		// Changes need to be saved before action
		btnSave.setOnAction(ae -> {
			doSave();
			// which command called this? we ask that to extraAction helper function
			extraActionAfterPrompt(message);
			dialogStage.close();
		});
		
		
		// User doesn't want to save changes before action
		btnDontSave.setOnAction(ae -> {
			// which command called this? we ask that to extraAction helper function
			extraActionAfterPrompt(message);
			dialogStage.close();
		});
		
		// Create scene for the dialog
		Scene scDialog = new Scene(vBoxMain);
		
		// Set scene on the dialog stage
		dialogStage.setScene(scDialog);
		
		// Show created dialog
		dialogStage.show();
		
	} // showDialog()
	
	// After user has chosen to save file or not in prompt we have to do another extra action -> close the app or clear text area
	public void extraActionAfterPrompt(String call) {
		if(call.toLowerCase().equals("close") || call.toLowerCase().equals("new")) {
			doNew();
		}
		// For exit end application
		else if (call.toLowerCase().equals("exit") ){
			doQuit();
		}
		// If call came from open branch - go to next step -> opening a new file
		else if (call.toLowerCase().equals("open") ){
			doFileOpen();
		}
		// else it might be open - do nothing
		else;
	}
	
	/** Does the actual saving of file */
	public void save(File fileToSave) {
		// Try to save the file using name given by user
		try {
			
			FileOutputStream fos = new FileOutputStream(fileToSave, false);

			// Get text from text area
			String text = txtMain.getText();

			// The file text must be saved as bytes
			byte[] dataOut = text.getBytes();

			// Save data (text) from text array to the file => write to file
			fos.write(dataOut);

			// Flush data from fos. It might be buffered
			fos.flush();

			// Close the file output stream
			fos.close();

			// Save path to created file in memory
			filePath = fileToSave.getPath();
			
			// Save copy to keep track of further changes
			activeFileContent = new StringBuilder(text);

		} // try
		catch (IOException ioe) {
			System.out.printf("Error saving file:\n");
			ioe.printStackTrace();
		} // catch()
	}
	
	

	// Contains layout formation
	@Override
	public void start(Stage primaryStage) throws Exception {
		// Set title of application/stage
		primaryStage.setTitle("FXNotes");

		// Define dimensions of application/stage (Width and height)
		primaryStage.setWidth(400);
		primaryStage.setHeight(300);

		// Add items to menus
		mFile.getItems().add(miFileNew);
		mFile.getItems().add(miFileOpen);
		mFile.getItems().add(miFileClose);
		mFile.getItems().add(miFileSave);
		mFile.getItems().add(miFileSaveAs);
		mFile.getItems().add(miFileExit);

		mEdit.getItems().add(miEditCut);
		mEdit.getItems().add(miEditCopy);
		mEdit.getItems().add(miEditPaste);
		mEdit.getItems().add(miEditDelete);
		mEdit.getItems().add(miEditSelectAll);

		mHelp.getItems().add(miHelpAbout);

		// Add menus to menu bar
		mbMain.getMenus().add(mFile);
		mbMain.getMenus().add(mEdit);
		mbMain.getMenus().add(mHelp);

		// Create a layout - borderpane in our case
		BorderPane bpMain = new BorderPane();

		// Add elements to our layout: text area in center and menu bar at the top
		bpMain.setTop(mbMain);
		bpMain.setCenter(txtMain);

		// Create scene
		Scene s = new Scene(bpMain);

		// Put scene on the stage
		primaryStage.setScene(s);

		// Show Stage
		primaryStage.show();

	} // start()

	// Launches the application
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch();
	} // main()

} // class
