package primary;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class PathFinder extends Application {

    private boolean hasExpandedHeightOnce = false;
    private ListGraph listGraphMap = new ListGraph();

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // root pane
        BorderPane root = new BorderPane();
        // title of stage
        primaryStage.setTitle("PathFinder");
        // Image
        ImageView imageView = new ImageView();
        root.setBottom(imageView);

        // MenuBar declaration
        MenuBar menuBar = new MenuBar();
        VBox fileVBox = new VBox();
        fileVBox.getChildren().add(menuBar);

        // "file"-menu declaration
        Menu fileMenu = new Menu("File");
        menuBar.getMenus().add(fileMenu);

        // "file"-menu items declarations
        MenuItem newMapItem = new MenuItem("New Map");
        fileMenu.getItems().add(newMapItem);
        newMapItem.setOnAction(e -> newMap(root, primaryStage, imageView));

        MenuItem openItem = new MenuItem("Open");
        fileMenu.getItems().add(openItem);
        openItem.setOnAction(e -> open());

        MenuItem saveItem = new MenuItem("Save");
        fileMenu.getItems().add(saveItem);
        saveItem.setOnAction(e -> save());

        MenuItem saveImageItem = new MenuItem("Save Image");
        fileMenu.getItems().add(saveImageItem);
        saveImageItem.setOnAction(e -> saveImage());

        MenuItem exitItem = new MenuItem("Exit");
        fileMenu.getItems().add(exitItem);
        exitItem.setOnAction(e -> System.exit(0));

        // sticks the file's combobox to TOP border of root
        root.setTop(menuBar);

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

        primaryStage.setScene(new Scene(root, 618, 50));
        primaryStage.setResizable(false);
        primaryStage.show();

        //primaryStage.setHeight(50 + buttonsFlowPane.getHeight() + menuBar.getHeight());
    }

    //"New Map", "Open", "Save", "Save Image", "Exit"
    private void newMap(BorderPane root, Stage primaryStage, ImageView imageView){
        Image imageMap = new Image("file:europa.gif");
        imageView = new ImageView(imageMap);
        root.setBottom(imageView);

        if (hasExpandedHeightOnce == false){
            primaryStage.setHeight(primaryStage.getHeight() + imageMap.getHeight()); //729
            hasExpandedHeightOnce = true;
        }
    }

    private void open(BorderPane root){
        ArrayList nodes = new ArrayList();
        double coordX;
        double coordY;
        ImageView imageToDrawOn = new ImageView("file:europa.gif");
        final double maxX = imageToDrawOn.getImage().getWidth();
        final double maxY = imageToDrawOn.getImage().getHeight();

        root.getChildren().add(imageToDrawOn);

        // set coordinates to array's 1st and 2nd index (0 is name)
        coordX = node[]

        //finns "europa.graph"?
        try (BufferedReader reader = new BufferedReader(new FileReader(new File("europa.graph")))) {
            //ja = open file
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null){
                lineNumber++;
                System.out.println(line);

                if(lineNumber == 1){
                    //reads file-name
                    break;
                }
                else if(lineNumber == 2){
                    //läser in noder
                    String nodeValues[] = line.split(";");


                }
                else if(lineNumber >= 3){
                    //läser in edges
                }
                else{
                    //blä
                    break;
                }
            }

        } catch (IOException e) {
            //nej = ge felmeddelande!
            e.printStackTrace();
        }
    }

    private void save(){

    }

    private void saveImage(){
        //F15
    }
}
