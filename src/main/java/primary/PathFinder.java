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

import java.io.*;
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
    private Stage mainStage;
    private ImageView imageView;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        //Stage
        mainStage = primaryStage;

        // root pane
        BorderPane root = new BorderPane();
        // title of stage
        mainStage.setTitle("PathFinder");
        // Image
        bottom = new Pane();
        imageView = new ImageView();
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
        newMapItem.setOnAction(e -> newMap(root));

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
    private void newMap(BorderPane root){

        setBackgroundImage("file:europa.gif");

        boolean okayToClearListMap = true;
        okayToClearListMap = openAFileAlert("Create a new map without saving this one first?");

        //clear map
        if (okayToClearListMap) {
            //clear canvas
            clearCanvas();

            //clear ListGraph
            clearListGraph();
        }
    }

    private boolean openAFileAlert(String header){
        if(bottom.getChildren().contains(canvas)) {
            //ask to save changes
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(header);
            alert.setContentText("Are you sure?");

            Optional<ButtonType> svar = alert.showAndWait();
            if (svar.isPresent() && svar.get() == ButtonType.CANCEL)
                return false;
        }

        return true;
    }

    private void setBackgroundImage(String fileName){
        Image imageMap = new Image("file:europa.gif");
        imageView = new ImageView(imageMap);
        bottom.getChildren().add(imageView);

        if (!hasExpandedHeightOnce){
            mainStage.setHeight(mainStage.getHeight() + imageMap.getHeight()); //729
            hasExpandedHeightOnce = true;
        }
    }

    private void open(BorderPane root){
        //if changes has been made (skapa variabel)
        boolean okayToOpen = openAFileAlert("Open a file without saving this one first?");

        if(okayToOpen) {

            //clear canvas
            clearCanvas();
            //clear ListGraph
            clearListGraph();

            //finns "europa.graph"?
            try (BufferedReader reader = new BufferedReader(new FileReader(new File("europa.graph")))) {
                //ja = open file
                String line;
                int lineNumber = 0;

                while((line = reader.readLine()) != null){
                    lineNumber++;
                    System.out.println(line);

                    if (lineNumber == 1) {
                        //reads file-name
                        setBackgroundImage(line);
                    } else if (lineNumber == 2) {
                        //läser in noder
                        String nodeValues[] = line.split(";");

                        //skriv ut alla värden
                        for (String s : nodeValues) {
                            System.out.println(s);
                        }

                        //sätt ihop värdena och lägg till alla noder i ListGraph:en
                        for (int i = 0; i < nodeValues.length; i += 3) {
                            City newNode = new City(nodeValues[i], Float.parseFloat(nodeValues[i + 1]), Float.parseFloat(nodeValues[i + 2]));
                            activeListGraphMap.add(newNode);
                        }
                    } else if (lineNumber >= 3) {
                        //läser in edges
                        String edgeValues[] = line.split(";");

                        Set<City> nodesSet = activeListGraphMap.getNodes();

                        Object[] nodes = nodesSet.toArray();

                        City[] nodesClone = new City[nodes.length];
                        int index = 0;
                        for (Object o : nodes) {
                            nodesClone[index++] = (City) o;
                        }

                        for (int i = 0; i < edgeValues.length; i += 4) {
                            int firstCityIndex = -1; //FIXA SÅ DET INTE ÄR -1!!!!!!!!
                            int secondCityIndex = -1; //FIXA SÅ DET INTE ÄR -1!!!!!!!!

                            for (int j = 0; j < nodes.length; j++) {
                                if (nodesClone[j].getName().equals(edgeValues[i])) {
                                    firstCityIndex = j;
                                } else if (nodesClone[j].getName().equals(edgeValues[i + 1])) {
                                    secondCityIndex = j;
                                }
                            }
                            //TODO: check if the cities are already connected - then dont do shit
                            if(!activeListGraphMap.pathExists(nodesClone[firstCityIndex], nodesClone[secondCityIndex]))
                                activeListGraphMap.connect(nodesClone[firstCityIndex], nodesClone[secondCityIndex], edgeValues[i + 2], Integer.parseInt(edgeValues[i + 3]));
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
    }

    private void save(){
        Path file = Paths.get("europa.graph");
        if(!Files.exists(file)) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("europa.graph", true))) {
                //rad 1
                bw.write(imageView.getImage().getUrl());
                bw.newLine();

                //rad 2
                String secondLine = "";
                Set<City> nodes = activeListGraphMap.getNodes();
                for (City c : nodes
                     ) {

                }

                bw.write("long line with nodes here");
                bw.newLine();

                //rad 3+
                bw.write("sorted node info here");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                System.out.println("before creating file");
                BufferedWriter bw = new BufferedWriter(new FileWriter("europa.graph", true));
                System.out.println("after creating file");
                //rad 1
                System.out.println("before flush file");
                bw.flush();
                System.out.println("after flush file");
                bw.write(imageView.getImage().getUrl());
                System.out.println("after writing file url");
                bw.newLine();
                //rad 2
                bw.write("long line with nodes here OVERWRITTEN");
                bw.newLine();
                //rad 3+
                bw.write("sorted node info here OVERWRITTEN");
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }

        //rad1 - write the imageViews image file-name on row 1

        //rad2 - write every city-node as following: "Name;X-value;Y-value;" on row 2

        //rad3+ - write every edge

    }

    private void saveImage(){
        //F15
    }

    private void clearCanvas(){
        bottom.getChildren().remove(canvas);
    }
    private void clearListGraph() { activeListGraphMap = new ListGraph(); }

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
