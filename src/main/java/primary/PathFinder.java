package primary;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class PathFinder extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        //our main pane idk
        BorderPane root = new BorderPane();

        //title of stage
        primaryStage.setTitle("PathFinder");

        //combobox
        // Array with actions
        String[] fileActions = {"New Map", "Open", "Save", "Save Image", "Exit"};
        // combobox declaration
        ComboBox fileDropDown = new ComboBox(FXCollections.observableArrayList(fileActions));
        Label testSelected = 
        // sticks root pane to top border
        root.setTop(fileDropDown);

        //FlowPane for buttons
        Pane flow = new FlowPane();

        //button Find Path
        Button findPathButton = new Button("FindPath");

        //button Show Connection

        //button New Place

        //button New Connection

        //button Change Connection


        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }
}
