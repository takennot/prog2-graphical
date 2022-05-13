package primary;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.controlsfx.control.action.Action;

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
        ComboBox fileCombobox = new ComboBox(FXCollections.observableArrayList(fileActions));
        // test label to see if event handler is working
        Label testSelected = new Label("default selected");
        // creates action event
        EventHandler<ActionEvent> fileEvent = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent){
                testSelected.setText(fileCombobox.getValue() + " is selected");
            }
        };
        fileCombobox.setOnAction(fileEvent);
        // second event handler event for buttons
        EventHandler<ActionEvent> buttonsEvent = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent){
                testSelected.setText(fileCombobox.getValue() + " is selected");
            }
        };
        // sticks root pane to top border
        root.setTop(fileCombobox);

        //FlowPane for buttons
        Pane buttonsFlowPane = new FlowPane();

        //button Find Path
        Button findPathButton = new Button("Find Path");
        buttonsFlowPane.getChildren().add(findPathButton);

        //button Show Connection
        Button showConnectionButton = new Button("Show Connection");
        buttonsFlowPane.getChildren().add(showConnectionButton);

        //button New Place
        Button newPlaceButton = new Button("New Place");
        buttonsFlowPane.getChildren().add(newPlaceButton);

        //button New Connection
        Button newConnection = new Button("New Connection");
        buttonsFlowPane.getChildren().add(newConnection);

        //button Change Connection
        Button changeConnection = new Button("Change Connection");
        buttonsFlowPane.getChildren().add(changeConnection);

        root.setCenter(buttonsFlowPane);

        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }
}
