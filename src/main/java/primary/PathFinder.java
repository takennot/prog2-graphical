package primary;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import primary.Exempelkoder.Bricka;
import primary.Exempelkoder.Memory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class PathFinder extends Application {

    private boolean hasExpandedHeightOnce = false;
    private ListGraph activeListGraphMap = new ListGraph();
    private Pane bottom;
    private Canvas canvas;
    private Stage mainStage;
    private ImageView imageView;
    private Scene scene;
    private Pane buttonsFlowPane;

    private MapTile mapTile1 = null, mapTile2 = null;

    private boolean unsavedChangesExist = true; //ändra false eller true idk

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
        exitItem.setOnAction(e -> exit());

        // sticks the file's combobox to TOP border of root
        root.setTop(menuBar);
//
//        // button event handler
//        EventHandler<ActionEvent> buttonsEvent = new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent actionEvent){
//                Alert msgBox = new Alert(Alert.AlertType.INFORMATION, "One of the buttons is pressed");
//                msgBox.showAndWait();
//            }
//        };

        //FlowPane for buttons
        buttonsFlowPane = new FlowPane();

        //button Find Path
        Button findPathButton = new Button("Find Path");
        buttonsFlowPane.getChildren().add(findPathButton);
        findPathButton.setOnAction(new FindPathEventHandler(findPathButton));

        //button Show Connection
        Button showConnectionButton = new Button("Show Connection");
        buttonsFlowPane.getChildren().add(showConnectionButton);
        showConnectionButton.setOnAction(new ShowConnectionEventHandler(showConnectionButton));

        //button New Place
        Button newPlaceButton = new Button("New Place");
        buttonsFlowPane.getChildren().add(newPlaceButton);
        newPlaceButton.setOnAction(new NewPlaceEventHandler(newPlaceButton));

        //button New Connection
        Button newConnection = new Button("New Connection");
        buttonsFlowPane.getChildren().add(newConnection);
        newConnection.setOnAction(new NewConnectionEventHandler(newConnection));

        //button Change Connection
        Button changeConnection = new Button("Change Connection");
        buttonsFlowPane.getChildren().add(changeConnection);
        changeConnection.setOnAction(new ChangeConnectionEventHandler(changeConnection));

        root.setCenter(buttonsFlowPane);

        scene = new Scene(root, 618, 50);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        //primaryStage.setHeight(50 + buttonsFlowPane.getHeight() + menuBar.getHeight());
    }
    //"New Map", "Open", "Save", "Save Image", "Exit"
    private void newMap(BorderPane root){

        setBackgroundImage("file:europa.gif");

        //if changes has been made (check boolean unsavedChangesExist)
        boolean okayToClearListMap = true;
        if(unsavedChangesExist) {
            okayToClearListMap = dontSaveAlert("Create a new map without saving this one first?");
        }

        //clear map
        if (okayToClearListMap) {
            //clear canvas
            clearCanvas();

            //clear ListGraph
            clearListGraph();
        }
    }

    private boolean dontSaveAlert(String header){
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
        boolean okayToOpen = true;
        if(unsavedChangesExist) {
            okayToOpen = dontSaveAlert("Open a file without saving this one first?");
        }

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
            //drawListGraph();
            drawListGraphTiles();
        }
    }

    private void save(){
        Path filePath = Paths.get("europa.graph");

        try {
            new FileWriter("europa.graph", true);
            Files.newBufferedWriter(filePath, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("europa.graph", true))) {
            //rad 1
            bw.write(imageView.getImage().getUrl());
            bw.newLine();

            //rad 2
            StringBuilder secondLine = new StringBuilder();
            Set<City> nodes = activeListGraphMap.getNodes();
            for (City c : nodes) {
                secondLine.append(c.getName()).append(";").append(c.getX()).append(";").append(c.getY()).append(";");
            }
            bw.write(secondLine.toString());
            bw.newLine();
            //rad 3+
            for (City c : nodes) {
                Collection<Edge<City>> edges = activeListGraphMap.getEdgesFrom(c);
                for (Edge<City> e : edges) {
                    bw.write(c.getName() + ";" + e.getDestination() + ";" + e.getName() + ";" + e.getWeight() + ";");
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        unsavedChangesExist = false;
    }

    private void saveImage(){
        //F15
        try{
            WritableImage image = bottom.snapshot(null, null);
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null); //(null, null);
            ImageIO.write(bufferedImage, "png", new File("capture.png"));
        } catch (IOException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "IO-fel " + e.getMessage());
            alert.showAndWait();
        }
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
        //write labels with city names under the nodes?

        //draw it on canvas
        bottom.getChildren().add(canvas);
    }

    private void drawListGraphTiles(){
        int radius = 10;
        int diameter = radius * 2;

        ClickHandler clickHandler = new ClickHandler();

        Set<City> nodes = activeListGraphMap.getNodes();

        //edge stuff atm
        canvas = new Canvas(618,729);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.moveTo(0,0);
        gc.stroke();

        for (City c : nodes) {
            //draw edges from city
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(3);

            Collection<Edge> edgesFromCity =  activeListGraphMap.getEdgesFrom(c); //funkar detta?
            for (Edge e : edgesFromCity) {
                City cityTo = (City) e.getDestination();
                gc.strokeLine(c.getX(), c.getY(), cityTo.getX(), cityTo.getY());
            }
        }

        //draw it on canvas
        bottom.getChildren().add(canvas);

        //city node stuff
        List<MapTile> mapTiles = new ArrayList<>();
        int i = 0;
        for (City c: nodes) {
            mapTiles.add(new MapTile(c, radius));
            i++;
        }
        //add tiles to bottom
        for (MapTile m : mapTiles) {
            bottom.getChildren().add(m);
            m.setOnMouseClicked(clickHandler);
        }
    }

    class ClickHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent mouseEvent) {
            System.out.println("ClickHandler active");

            MapTile m = (MapTile) mouseEvent.getSource();
            System.out.println("City: " + m.city.getName());

            if(mapTile1 == null){
                mapTile1 = m;
                m.paintUncovered();
            }
            else if (mapTile2 == null && m != mapTile1){
                mapTile2 = m;
                m.paintUncovered();
            }
        }
    }

    private void exit(){
        //if changes has been made (check boolean unsavedChangesExist)

        boolean okayToExit = dontSaveAlert("Exit without saving?");

        if(okayToExit) {
            System.exit(0);
        }
    }

    public class NewPlaceEventHandler implements EventHandler<ActionEvent>{

        private final Button button;

        public NewPlaceEventHandler(Button button){
            this.button = button;
        }

        @Override
        public void handle(ActionEvent actionEvent){
            //ändra muspekaren till "+"
            scene.setCursor(Cursor.CROSSHAIR);
            //disable newPlace button
            button.setDisable(true);

            //känn av var dom trycker på kartan
            bottom.setOnMouseClicked(event -> {
                System.out.println("Mouse clicked");
                if(button.isDisabled()) {

                    double posX = event.getX();
                    double posY = event.getY();

                    System.out.println("X: " + event.getX());
                    System.out.println("Y: " + event.getY());

                    //då öppna ett alert
                    NewPlaceDialog newPlaceDialog = new NewPlaceDialog();
                    Optional<ButtonType> result = newPlaceDialog.showAndWait();

                    if (result.isPresent() && result.get() != ButtonType.OK) {
                        button.setDisable(false);
                        scene.setCursor(Cursor.DEFAULT);
                        return;
                    }

                    String nameOfCity = newPlaceDialog.getName();

                    //add city to listGraph
                    activeListGraphMap.add(new City(nameOfCity, (float) posX, (float) posY));
                    //drawListGraph();
                    drawListGraphTiles();
                    unsavedChangesExist = true;

                    scene.setCursor(Cursor.DEFAULT);
                    button.setDisable(false);
                }
            });
        }
    }

    public class NewConnectionEventHandler implements EventHandler<ActionEvent>{

        private final Button button;

        public NewConnectionEventHandler(Button button){
            this.button = button;
        }

        @Override
        public void handle(ActionEvent actionEvent){

        }
    }
    public class FindPathEventHandler implements EventHandler<ActionEvent>{

        private final Button button;

        public FindPathEventHandler(Button button){
            this.button = button;
        }

        @Override
        public void handle(ActionEvent actionEvent){

        }
    }
    public class ShowConnectionEventHandler implements EventHandler<ActionEvent>{

        private final Button button;

        public ShowConnectionEventHandler(Button button){
            this.button = button;
        }

        @Override
        public void handle(ActionEvent actionEvent){

        }
    }
    public class ChangeConnectionEventHandler implements EventHandler<ActionEvent>{

        private final Button button;

        public ChangeConnectionEventHandler(Button button){
            this.button = button;
        }

        @Override
        public void handle(ActionEvent actionEvent){

        }
    }

    class NewPlaceDialog extends Alert{
        private final TextField nameOfPlaceField = new TextField();

        public NewPlaceDialog(){
            super(AlertType.CONFIRMATION);
            GridPane grid = new GridPane();

            grid.setAlignment(Pos.CENTER);
            grid.setPadding(new Insets(10));
            grid.setHgap(5);
            grid.setVgap(10);

            grid.addRow(0, new Label("Name of place:"), nameOfPlaceField);
            setHeaderText(null);
            setTitle("Name");
            getDialogPane().setContent(grid);
        }

        public String getName(){
            return nameOfPlaceField.getText();
        }
    }
}
