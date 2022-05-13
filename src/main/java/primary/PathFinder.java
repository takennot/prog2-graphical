package primary;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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

        // sticks root pane to top border
        root.setTop(fileCombobox);
        root.setRight(testSelected);
        // button event handler
        EventHandler<ActionEvent> buttonsEvent = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent){
                Alert msgBox = new Alert(Alert.AlertType.INFORMATION, "One of the buttons is pressed");
                msgBox.showAndWait();
            }
        };
        //FlowPane for buttons
        Pane buttonsFlowPane = new FlowPane();

        //button Find Path
        Button findPathButton = new Button("Find Path");
        buttonsFlowPane.getChildren().add(findPathButton);
        findPathButton.setOnAction(buttonsEvent);

        //button Show Connection
        Button showConnectionButton = new Button("Show Connection");
        buttonsFlowPane.getChildren().add(showConnectionButton);
        showConnectionButton.setOnAction(buttonsEvent);

        //button New Place
        Button newPlaceButton = new Button("New Place");
        buttonsFlowPane.getChildren().add(newPlaceButton);
        newPlaceButton.setOnAction(buttonsEvent);

        //button New Connection
        Button newConnection = new Button("New Connection");
        buttonsFlowPane.getChildren().add(newConnection);
        newConnection.setOnAction(buttonsEvent);

        //button Change Connection
        Button changeConnection = new Button("Change Connection");
        buttonsFlowPane.getChildren().add(changeConnection);
        changeConnection.setOnAction(buttonsEvent);

        root.setCenter(buttonsFlowPane);

        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }
}
