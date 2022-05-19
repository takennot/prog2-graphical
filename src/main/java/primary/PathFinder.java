package primary;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import java.util.ArrayList;
import java.util.Set;

public class PathFinder extends Application {

    private boolean hasExpandedHeightOnce = false;
    private ListGraph listGraphMap = new ListGraph();
    private Pane bottom;

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

        if (hasExpandedHeightOnce == false){
            primaryStage.setHeight(primaryStage.getHeight() + imageMap.getHeight()); //729
            hasExpandedHeightOnce = true;
        }
    }

    private void open(BorderPane root){
//        ArrayList node = new ArrayList();
//        Circle city = new Circle();
//        double coordX;
//        double coordY;
//        ImageView imageToDrawOn = new ImageView("file:europa.gif");
//        final double maxX = imageToDrawOn.getImage().getWidth();
//        final double maxY = imageToDrawOn.getImage().getHeight();
//
//        root.getChildren().add(imageToDrawOn);
//
//        // place this shit in some loop idk
//        // set coordinates to array's 1st and 2nd index (0 is name)
//        coordX = (double) node.get(1);
//        coordY = (double) node.get(2);
//        city.setCenterX(coordX);
//        city.setCenterY(coordY);
//        city.setRadius(10);
//        // get list of cities
//        // foreach city in cities
//        // get edges from current city
//        // foreach edge in currentCityEdgeList
//        // get destination of said edge
//        // Draw line from city to destination?

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
                        listGraphMap.add(newNode);
                    }
                }
                else if(lineNumber >= 3){
                    //läser in edges
                    String edgeValues[] = line.split(";");

                    Set<City> nodesSet = listGraphMap.getNodes();

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

                        listGraphMap.connect(nodesClone[firstCityIndex], nodesClone[secondCityIndex], edgeValues[i+2], Integer.parseInt(edgeValues[i+3]));
                    }
                }
            }
        } catch (IOException e) {
            //nej = ge felmeddelande!
            e.printStackTrace();
            System.out.println("europa.graph not found");
        }

        //skriver ut ListGraph för att se att allt stämmer
        System.out.println(listGraphMap.toString());

        //draw it out
        drawListGraph(root);
    }

    private void save(){

    }

    private void saveImage(){
        //F15
    }

    private void drawListGraph(BorderPane root){
        //create a canvas
        Canvas canvas = new Canvas(618,729);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        //root.getChildren().add(canvas);

        gc.setStroke(Color.BLUE);
        gc.moveTo(0,0);
        // lineTo city1
        //gc.lineTo(470,242);
        gc.stroke();

        int radius = 7.5;
        int diameter = radius * 2;

        //Stockholm
        gc.setFill(Color.BLUE);
        //gc.fillOval(470.0 - radius, 242.0- radius, diameter, diameter);
        //Berlin
        //gc.fillOval(411.0 - radius, 368.0 - radius, diameter, diameter);

        Set<City> nodes = listGraphMap.getNodes();
        gc.setFill(Color.BLUE);
        for (City c : nodes) {
            gc.fillOval(c.getX() - radius, c.getY() - radius, diameter, diameter);
        }

        bottom.getChildren().add(canvas);
        //draw nodes/Cities on a canvas
            //write labels with names under the nodes

        //draw edges on canvas
    }

//    private class Node extends Canvas{
//        public Node(double x, double y){
//            super(x, y);
//            GraphicsContext gc = getGraphicsContext2D();
//            gc.setFill(Color.BLUE);
//            gc.fillOval(getLayoutX(), getLayoutY(), 20, 20);
//        }
//    }
}
