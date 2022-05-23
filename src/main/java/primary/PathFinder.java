package primary;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class PathFinder extends Application {

    private boolean hasExpandedHeightOnce = false;
    private ListGraph activeListGraphMap = new ListGraph();
    private Pane bottom;
    private Canvas canvas;

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
        bottom = new Pane();
        ImageView imageView = new ImageView();
        bottom.getChildren().add(imageView);
        root.setBottom(bottom);

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
        openItem.setOnAction(e -> open(root));

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
        bottom.getChildren().add(imageView);

        boolean okayToClearListMap = true;

        if(bottom.getChildren().contains(canvas)) {
            //ask to save changes
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Create a new map without saving this one first?");
            alert.setContentText("Are you sure?");

            Optional<ButtonType> svar = alert.showAndWait();
            if (svar.isPresent() && svar.get() == ButtonType.OK)
                okayToClearListMap = true;
        }
        else{
            okayToClearListMap = false;
        }

        //clear map
        if (okayToClearListMap) {
            //clear canvas
            clearCanvas();

            //clear ListGraph
            activeListGraphMap = new ListGraph();
        }

        if (!hasExpandedHeightOnce){
            primaryStage.setHeight(primaryStage.getHeight() + imageMap.getHeight()); //729
            hasExpandedHeightOnce = true;
        }
    }

    private void open(BorderPane root){
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
                }
                else if(lineNumber == 2){
                    //läser in noder
                    String nodeValues[] = line.split(";");

                    //skriv ut alla värden
                    for (String s : nodeValues) {
                        System.out.println(s);
                    }

                    //sätt ihop värdena och lägg till alla noder i ListGraph:en
                    for (int i = 0; i < nodeValues.length; i += 3) {
                        City newNode = new City(nodeValues[i], Float.parseFloat(nodeValues[i+1]), Float.parseFloat(nodeValues[i+2]));
                        activeListGraphMap.add(newNode);
                    }
                }
                else if(lineNumber >= 3){
                    //läser in edges
                    String edgeValues[] = line.split(";");

                    Set<City> nodesSet = activeListGraphMap.getNodes();

                    Object[] nodes = nodesSet.toArray();

                    City[] nodesClone = new City[nodes.length];
                    int bruh = 0;
                    for (Object o : nodes){
                        nodesClone[bruh++] = (City) o;
                    }

                    for (int i = 0; i < edgeValues.length; i += 4) {
                        int firstCityIndex = -1; //FIXA SÅ DET INTE ÄR -1!!!!!!!!
                        int secondCityIndex = -1; //FIXA SÅ DET INTE ÄR -1!!!!!!!!

                        for (int j = 0; j < nodes.length; j++) {
                            if(nodesClone[j].getName().equals(edgeValues[i])){
                                firstCityIndex = j;
                            }
                            else if(nodesClone[j].getName().equals(edgeValues[i+1])){
                                secondCityIndex = j;
                            }
                        }

                        activeListGraphMap.connect(nodesClone[firstCityIndex], nodesClone[secondCityIndex], edgeValues[i+2], Integer.parseInt(edgeValues[i+3]));
                    }
                }
            }
        } catch (IOException e) {
            //nej = ge felmeddelande!
            e.printStackTrace();
            System.out.println("europa.graph not found");
        }

        //skriver ut ListGraph för att se att allt stämmer
        System.out.println(activeListGraphMap.toString());

        //draw it out
        drawListGraph();
    }

    private void save(){
        Path file = Paths.get("hellothere.txt");
        if(!Files.exists(file)) {
            try {
                Files.createFile(file.toAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveImage(){
        //F15
    }

    private void clearCanvas(){
        bottom.getChildren().remove(canvas);
    }

    private void drawListGraph(){
        //TODO: lines and nodes overlap. FIX IT!
        //TODO: ska dom ens ritas ut 2 gånger? kan vi fixa det på något sätt?

        //create a canvas
        canvas = new Canvas(618,729);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.moveTo(0,0);
        gc.stroke();

        int radius = 10;
        int diameter = radius * 2;

        //draw nodes and edges on a canvas
        Set<City> nodes = activeListGraphMap.getNodes();
        gc.setFill(Color.BLUE);
        for (City c : nodes) {
            //draw edges from city
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(3);

            Collection<Edge> edgesFromCity =  activeListGraphMap.getEdgesFrom(c); //funkar detta?
            for (Edge e : edgesFromCity) {
                City cityTo = (City) e.getDestination();
                gc.strokeLine(c.getX(), c.getY(), cityTo.getX(), cityTo.getY());
            }

            //draw node
            gc.fillOval(c.getX() - radius, c.getY() - radius, diameter, diameter);
        }
        //write labels with city names under the nodes

        //draw it on canvas
        bottom.getChildren().add(canvas);
    }
}
