// standard javafx imports
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

// Imports for components in this application
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;

// Imports for layout
import javafx.scene.layout.BorderPane;
import javafx.geometry.Insets;

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
	MenuItem miFileNew, miFileOpen, miFileClose, miFileSave, miFileExit;
	MenuItem miEditDelete, miEditCut, miEditCopy, miEditPaste, miEditSelectAll;
	MenuItem miHelpAbout;
	TextArea txtMain;
	
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
		
	} // constructor()
	
	
	// Takes care of actions we want to perform on elements
	@Override
	public void init() {
		
	} // init()

	
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
