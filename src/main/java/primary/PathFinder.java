package primary;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class PathFinder extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        //our main pane idk
        Pane root = new Pane();

        //title of stage
        primaryStage.setTitle("PathFinder");

        //dropDown

        //button Find Path


        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }
}
