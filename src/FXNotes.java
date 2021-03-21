
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
// Imports for components in this application
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
// Imports for layout
import javafx.scene.layout.BorderPane;
import javafx.geometry.Insets;

// Import for quitting the application
import javafx.application.Platform;

/**
 * 
 */

/**
 * @author reinis janovskis Stuednt ID: 3028319
 *
 */
public class FXNotes extends Application {

	// Define variables that need class scope
	MenuBar mbMain;

	Menu mFile, mEdit, mHelp;

	MenuItem miFileNew, miFileOpen, miFileClose, miFileSave, miFileExit;
	MenuItem miEditDelete, miEditCut, miEditCopy, miEditPaste, miEditSelectAll;
	MenuItem miHelpAbout;
	TextArea txtMain;

	String filePath;

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

	} // constructor()

	// Takes care of actions we want to perform on elements
	@Override
	public void init() {
		// Add basic functionality for our exit button
		miFileExit.setOnAction(ae -> Platform.exit());

		// The edit menu event
		miEditCut.setOnAction(ae -> txtMain.cut());
		miEditCopy.setOnAction(ae -> txtMain.copy());
		miEditPaste.setOnAction(ae -> txtMain.paste());
		miEditSelectAll.setOnAction(ae -> txtMain.selectAll());
		miEditDelete.setOnAction(ae -> doDelete());

		// Action for About menuItem under menu Help
		miHelpAbout.setOnAction(ae -> showAboutInformation());

		// Actions for menuItems under File
		miFileOpen.setOnAction(ae -> doFileOpen());
		miFileSave.setOnAction(ae -> doSave());
		miFileNew.setOnAction(ae -> doNew());
		miFileClose.setOnAction(ae -> doClose());

	} // init()

	// Function that will delete selected text in our text editor
	// Inspiration and idea for implementation taken from stackoverflow
	// https://stackoverflow.com/questions/15859289/how-to-make-selected-text-in-jtextarea-into-a-string
	//
	public void doDelete() {
		if (txtMain.getSelectedText() != null) {
			txtMain.deleteText(txtMain.getSelection().getStart(), txtMain.getSelection().getEnd());
		}
	}

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
	}

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
		else
			; // If user clicks 'Cancel' do nothing

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
			try {
				
				FileOutputStream fos = new FileOutputStream(filePath, true);
				
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
				
			} // try
			catch(IOException ioe) {
				System.out.printf("Error saving file:\n");
				ioe.printStackTrace();
			} // catch()
		}
	}

	// Method taken from class example/task FileExample
	/** method that saves a file to users specified location */
	public void doSaveAs() {

		// Use a file chooser
		FileChooser fc = new FileChooser();

		// Show the file save dialog. Assign the file name.
		File fileToSave = fc.showSaveDialog(null);

		// Test if user tried to save file (dialog confirmed)
		if (fileToSave != null) {

			// Try to save the file using name given by user
			try {

				FileOutputStream fos = new FileOutputStream(fileToSave, true);

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

			} // try
			catch (IOException ioe) {
				System.out.printf("Error saving file:\n");
				ioe.printStackTrace();
			} // catch()
		} // if
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
		//filePath = null;
		// Break connection with previously used file
		doClose();
	}
	
	public void doClose() {
		// Set variable filePath to be null to break association with previous file we were using
		filePath = null;
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
