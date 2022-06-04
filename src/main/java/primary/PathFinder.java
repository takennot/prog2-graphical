// PROG2 VT2022, Inlämningsuppgift, del 2
// Grupp 055
// Saga Liljenroth Dickman sali3923
// Ruslan Musaev rumu4402
package primary;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class PathFinder extends Application {

    private boolean hasExpandedHeightOnce;
    private ListGraph<City> activeListGraphMap = new ListGraph<>();
    private Pane bottom;
    private Canvas canvas;
    private Stage mainStage;
    private final ImageView imageView = new ImageView(); //TODO: ta bort final ifall det fuckar upp
    private Scene scene;

    private MapTile mapTile1;
    private MapTile mapTile2;

    private boolean unsavedChangesExist; //ändra false eller true idk

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
        bottom.setId("outputArea");
        bottom.getChildren().add(imageView);

        root.setBottom(bottom);

        // MenuBar declaration
        MenuBar menuBar = new MenuBar();
        menuBar.setId("menu");
        VBox fileVBox = new VBox();
        fileVBox.getChildren().add(menuBar);

        // "file"-menu declaration
        Menu fileMenu = new Menu("File");
        fileMenu.setId("menuFile");
        menuBar.getMenus().add(fileMenu);

        // "file"-menu items declarations
        MenuItem newMapItem = new MenuItem("New Map");
        newMapItem.setId("menuNewMap");
        fileMenu.getItems().add(newMapItem);
        newMapItem.setOnAction(e -> newMap());

        MenuItem openItem = new MenuItem("Open");
        openItem.setId("menuOpenFile");
        fileMenu.getItems().add(openItem);
        openItem.setOnAction(e -> open());

        MenuItem saveItem = new MenuItem("Save");
        saveItem.setId("menuSaveFile");
        fileMenu.getItems().add(saveItem);
        saveItem.setOnAction(e -> save());

        MenuItem saveImageItem = new MenuItem("Save Image");
        saveImageItem.setId("menuSaveImage");
        fileMenu.getItems().add(saveImageItem);
        saveImageItem.setOnAction(e -> saveImage());

        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setId("menuExit");
        fileMenu.getItems().add(exitItem);
        exitItem.setOnAction(e -> exit());

        // sticks the file's combobox to TOP border of root
        root.setTop(menuBar);

        //Pane for buttons
        HBox buttonsPane = new HBox(10);

        //button Find Path
        Button findPathButton = new Button("Find Path");
        findPathButton.setId("btnFindPath");
        buttonsPane.getChildren().add(findPathButton);
        findPathButton.setOnAction(new FindPathEventHandler(findPathButton));

        //button Show Connection
        Button showConnectionButton = new Button("Show Connection");
        showConnectionButton.setId("btnShowConnection");
        buttonsPane.getChildren().add(showConnectionButton);
        showConnectionButton.setOnAction(new ShowConnectionEventHandler(showConnectionButton));

        //button New Place
        Button newPlaceButton = new Button("New Place");
        newPlaceButton.setId("btnNewPlace");
        buttonsPane.getChildren().add(newPlaceButton);
        newPlaceButton.setOnAction(new NewPlaceEventHandler(newPlaceButton));

        //button New Connection
        Button newConnection = new Button("New Connection");
        newConnection.setId("btnNewConnection");
        buttonsPane.getChildren().add(newConnection);
        newConnection.setOnAction(new NewConnectionEventHandler());

        //button Change Connection
        Button changeConnection = new Button("Change Connection");
        changeConnection.setId("btnChangeConnection");
        buttonsPane.getChildren().add(changeConnection);
        changeConnection.setOnAction(new ChangeConnectionEventHandler(changeConnection));

        root.setCenter(buttonsPane);
        buttonsPane.setAlignment(Pos.CENTER);

        scene = new Scene(root, 618, 70);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        //primaryStage.setHeight(50 + buttonsFlowPane.getHeight() + menuBar.getHeight());
    }

    private void newMap(){

        setBackgroundImage("file:europa.gif");

        //if changes has been made (check boolean unsavedChangesExist)
        boolean okayToClearListMap = true;
        if(unsavedChangesExist) {
            okayToClearListMap = dontSaveAlert("Create a new map without saving this one first?");
        }

        //clear map
        if (okayToClearListMap) {
            //clear bottom
            if (mapTile1 != null){
                mapTile1.paintNotSelected();
                mapTile1 = null;
            }
            if(mapTile2 != null){
                mapTile2.paintNotSelected();
                mapTile2 = null;
            }
            clearListGraph();
            clearBottom();
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
        Image imageMap = new Image(fileName);
        imageView.setImage(imageMap);

        if (!hasExpandedHeightOnce){
            mainStage.setHeight(mainStage.getHeight() + imageMap.getHeight()); //729
            hasExpandedHeightOnce = true;
        }
    }

    private void open(){
        //if changes has been made (skapa variabel)
        boolean okayToOpen = true;
        if(unsavedChangesExist) {
            okayToOpen = dontSaveAlert("Open a file without saving this one first?");
        }

        if(okayToOpen) {
            //clear bottom
            if (mapTile1 != null){
                mapTile1.paintNotSelected();
                mapTile1 = null;
            }
            if(mapTile2 != null){
                mapTile2.paintNotSelected();
                mapTile2 = null;
            }
            clearListGraph();
            clearBottom();

            //finns "europa.graph"?
            try (BufferedReader reader = new BufferedReader(new FileReader(new File("europa.graph")))) {

                String line;
                int lineNumber = 0;

                while((line = reader.readLine()) != null){
                    lineNumber++;

                    if (lineNumber == 1) {
                        //reads file-name
                        setBackgroundImage(line);
                    }
                    else if (lineNumber == 2) {
                        //läser in noder
                        String[] nodeValues = line.split(";");

                        //sätt ihop värdena och lägg till alla noder i ListGraph:en
                        for (int i = 0; i < nodeValues.length; i += 3) {
                            City newNode = new City(nodeValues[i], Float.parseFloat(nodeValues[i + 1]), Float.parseFloat(nodeValues[i + 2]));
                            activeListGraphMap.add(newNode);
                        }
                    }
                    else if (lineNumber >= 3) {
                        //läser in edges
                        String[] edgeLineValues = line.split(";");

                        Set<City> nodesSet = activeListGraphMap.getNodes();

                        Object[] nodes = nodesSet.toArray();

                        City[] nodesClone = new City[nodes.length];
                        int index = 0;
                        for (Object o : nodes) {
                            nodesClone[index++] = (City) o;
                        }

                        for (int i = 0; i < edgeLineValues.length; i += 4) {
                            City firstCity = null;
                            City secondCity = null;

                            // System.out.println(edgeLineValues[i] + edgeLineValues[i+1] + edgeLineValues[i+2] + edgeLineValues[i+3]);

                            for (int j = 0; j < nodes.length; j++) {
                                if (nodesClone[j].getName().equals(edgeLineValues[i])) {
                                    firstCity = nodesClone[j];
                                } else if (nodesClone[j].getName().equals(edgeLineValues[i + 1])) {
                                    secondCity = nodesClone[j];
                                }
                            }

                            if(firstCity != null && secondCity != null && activeListGraphMap.getEdgeBetween(firstCity, secondCity) == null)
                                activeListGraphMap.connect(firstCity, secondCity, edgeLineValues[i + 2], Integer.parseInt(edgeLineValues[i + 3]));
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            //draw it out
            //System.out.println("Open: draw Edges");
            drawListGraphEdges();
            //System.out.println("Open: draw Tiles");
            drawListGraphTiles();
        }
    }

    private void save(){
        if (mapTile1 != null){
            mapTile1.paintNotSelected();
            mapTile1 = null;
        }
        if(mapTile2 != null){
            mapTile2.paintNotSelected();
            mapTile2 = null;
        }

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
                    bw.write(c.getName() + ";" + e.getDestination() + ";" + e.getName() + ";" + e.getWeight());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        unsavedChangesExist = false;
    }

    private void saveImage(){
        try{
            if (mapTile1 != null){
                mapTile1.paintNotSelected();
                mapTile1 = null;
            }
            if(mapTile2 != null){
                mapTile2.paintNotSelected();
                mapTile2 = null;
            }
            WritableImage image = bottom.snapshot(null, null);
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null); //(null, null);
            ImageIO.write(bufferedImage, "png", new File("capture.png"));
        } catch (IOException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "IO-fel " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void clearBottom(){
        bottom.getChildren().clear();
        bottom.getChildren().add(imageView);
    }

    private void clearTiles(){
        //remove mapTiles
        bottom.getChildren().clear();
        bottom.getChildren().add(imageView);
        if(canvas != null)
            bottom.getChildren().add(canvas);
    }

    private void clearEdges(){
        bottom.getChildren().remove(canvas);
    }

    private void clearListGraph() {
        activeListGraphMap = new ListGraph<>();
    }

    private void drawListGraphEdges(){

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

            Collection<Edge<City>> edgesFromCity =  activeListGraphMap.getEdgesFrom(c);
            for (Edge<City> e : edgesFromCity) {
                City cityTo = e.getDestination();
                gc.strokeLine(c.getX(), c.getY(), cityTo.getX(), cityTo.getY());
            }
        }

        //draw it on canvas
        bottom.getChildren().add(1, canvas); //TODO: !!!!
    }

    private void drawListGraphTiles(){
        int radius = 10;

        ClickHandler clickHandler = new ClickHandler();
        Set<City> listGraphNodes = activeListGraphMap.getNodes();

        //create list with mapTiles
        List<MapTile> mapTiles = new ArrayList<>();
        for (City c: listGraphNodes) {
            mapTiles.add(new MapTile(c, radius));
        }

        clearTiles();

        //add tiles to bottom
        for (MapTile m : mapTiles) {
//            //clear all tiles
//            for (Node node : bottom.getChildren()) {
//                bottom.getChildren().remove(m);
//            }

            //add them
            //m.setId(m.getCity().getName());

            bottom.getChildren().add(m);
            //mapTilesBottom.getChildren().add(m);
            m.setId(m.getCity().getName());

            Label cityNameLabel = new Label(m.getCity().getName());
            cityNameLabel.relocate(m.getCity().getX() - radius, m.getCity().getY() + radius);
            cityNameLabel.setStyle("-fx-font-weight: bold");
            cityNameLabel.setDisable(true);
            bottom.getChildren().add(cityNameLabel);

            m.setOnMouseClicked(clickHandler);
        }

        mapTile1 = null;
        mapTile2 = null;
    }

    private void showMustSelectTwoPlacesAlert(){
        //error alert
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error!");
        alert.setHeaderText(null);
        alert.setContentText("Two places must be selected!");

        alert.showAndWait();
    }

    class ClickHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent mouseEvent) {
            if(mouseEvent.getSource() instanceof MapTile) {
                MapTile m = (MapTile) mouseEvent.getSource();

                if (mapTile1 == null && m != mapTile2) {
                    mapTile1 = m;
                    m.paintSelected();
                }
                else if (mapTile2 == null && m != mapTile1) {
                    mapTile2 = m;
                    m.paintSelected();
                }
                else if (m == mapTile1) {
                    //deselect
                    mapTile1 = null;
                    m.paintNotSelected();
                }
                else if (m == mapTile2) {
                    //deselect
                    mapTile2 = null;
                    m.paintNotSelected();
                }
            }
        }
    }

    private void exit(){
        boolean okayToExit = true;
        if(unsavedChangesExist)
            okayToExit = dontSaveAlert("Exit without saving?");

        if(okayToExit) {
            System.exit(0);
        }
    }

    //EventHandlers
    public class NewPlaceEventHandler implements EventHandler<ActionEvent>{

        private final Button button;

        public NewPlaceEventHandler(Button button){
            this.button = button;
        }

        @Override
        public void handle(ActionEvent actionEvent){
            if (mapTile1 != null){
                mapTile1.paintNotSelected();
                mapTile1 = null;
            }
            if(mapTile2 != null){
                mapTile2.paintNotSelected();
                mapTile2 = null;
            }
            //ändra muspekaren till "+"
            scene.setCursor(Cursor.CROSSHAIR);
            //disable newPlace button
            button.setDisable(true);

            //känn av var dom trycker på kartan
            bottom.setOnMouseClicked(event -> {
                if(button.isDisabled()) {

                    double posX = event.getX();
                    double posY = event.getY();

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
                    drawListGraphEdges();
                    drawListGraphTiles();
                    unsavedChangesExist = true;

                    scene.setCursor(Cursor.DEFAULT);
                    button.setDisable(false);
                }
            });
        }
    }
    public class NewConnectionEventHandler implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent actionEvent){
            //markeras 2 tiles?
            if(mapTile1 != null && mapTile2 != null){
                if(activeListGraphMap.getEdgeBetween(mapTile1.getCity(), mapTile2.getCity()) == null) {
                    //Nodes
                    City from = mapTile1.getCity();
                    City to = mapTile2.getCity();

                    //connection alert dialog
                    NewConnectionDialog newConnectionDialog = new NewConnectionDialog(from, to);
                    Optional<ButtonType> result = newConnectionDialog.showAndWait();

                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        if (newConnectionDialog.getName().isEmpty()) {
                            //error alert
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error!");
                            alert.setHeaderText(null);
                            alert.setContentText("Name cannot be empty!");

                            alert.showAndWait();
                        } else if (!(newConnectionDialog.getWeight() == 0)) {
                            activeListGraphMap.connect(from, to, newConnectionDialog.getName(), (int) newConnectionDialog.getWeight());

                            //clear stuff
                            clearEdges();

                            //ListGraph backup = new ListGraph();

                            //draw stuff
                            drawListGraphEdges();
                            //drawListGraphTiles();

                            unsavedChangesExist = true;
                        }
                    }
                }
                else{
                    //alert edge already exists
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Edge already exists");
                    alert.setContentText("Edge between " + mapTile1.getCity().getName() + " and " + mapTile2.getCity().getName() + " already exists!");
                    alert.showAndWait();
                }
            }
            else{
                //error alert
                showMustSelectTwoPlacesAlert();
            }
        }
    }
    public class FindPathEventHandler implements EventHandler<ActionEvent>{

        private final Button button;

        public FindPathEventHandler(Button button){
            this.button = button;
        }

        @Override
        public void handle(ActionEvent actionEvent){
            if(mapTile1 != null && mapTile2 != null){
                //path exists?
                if(activeListGraphMap.pathExists(mapTile1.getCity(), mapTile2.getCity())){
                    //get path
                    List<Edge<City>> path =  activeListGraphMap.getPath(mapTile1.getCity(), mapTile2.getCity());

                    //write out and show
                    FindPathDialog findPathDialog = new FindPathDialog(mapTile1.getCity(), mapTile2.getCity(), path);
                    findPathDialog.showAndWait();
                }
                else{
                    //error alert - no edge between
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error!");
                    alert.setHeaderText(null);
                    alert.setContentText("There is no path between " + mapTile1.getCity().getName() + " and " + mapTile2.getCity().getName() + "!");

                    alert.showAndWait();
                }
            }
            else{
                //error alert
                showMustSelectTwoPlacesAlert();
            }
        }
    }
    public class ShowConnectionEventHandler implements EventHandler<ActionEvent>{

        private final Button button;

        public ShowConnectionEventHandler(Button button){
            this.button = button;
        }

        @Override
        public void handle(ActionEvent actionEvent){
            if(mapTile1 != null && mapTile2 != null){
                Edge<City> edge = activeListGraphMap.getEdgeBetween(mapTile1.getCity(), mapTile2.getCity());

                if(!(edge == null)){
                    ShowConnectionDialog showConnectionDialog = new ShowConnectionDialog(mapTile1.getCity(), mapTile2.getCity(), edge);
                    showConnectionDialog.showAndWait();
                }
                else{
                    //error alert - no edge between
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error!");
                    alert.setHeaderText(null);
                    alert.setContentText("There is no edge between " + mapTile1.getCity().getName() + " and " + mapTile2.getCity().getName() + "!");

                    alert.showAndWait();
                }
            }
            else{
                //error alert
                showMustSelectTwoPlacesAlert();
            }
        }
    }
    public class ChangeConnectionEventHandler implements EventHandler<ActionEvent>{

        private final Button button;

        public ChangeConnectionEventHandler(Button button){
            this.button = button;
        }

        @Override
        public void handle(ActionEvent actionEvent){
            if(mapTile1 != null && mapTile2 != null){
                Edge<City> edge = activeListGraphMap.getEdgeBetween(mapTile1.getCity(), mapTile2.getCity());

                if(!(edge == null)){
                    ChangeConnectionDialog changeConnectionDialog = new ChangeConnectionDialog(mapTile1.getCity(), mapTile2.getCity(), edge);
                    Optional<ButtonType> result = changeConnectionDialog.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK){
                        if(changeConnectionDialog.getWeight() != 0)
                            activeListGraphMap.setConnectionWeight(mapTile1.getCity(), mapTile2.getCity(), (int) changeConnectionDialog.getWeight());
                    }
                }
                else{
                    //error alert - no edge between
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error!");
                    alert.setHeaderText(null);
                    alert.setContentText("There is no edge between " + mapTile1.getCity().getName() + " and " + mapTile2.getCity().getName() + "!");

                    alert.showAndWait();
                }
            }
            else{
                //error alert
                showMustSelectTwoPlacesAlert();
            }
        }
    }

    //Button Dialogs
    class NewPlaceDialog extends Alert{
        private final TextField nameOfPlaceField = new TextField();

        NewPlaceDialog(){
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
    class NewConnectionDialog extends Alert{
        private final TextField nameOfEdgeField = new TextField();
        private final TextField timeField = new TextField();

        NewConnectionDialog(City from, City to){
            super(AlertType.CONFIRMATION);
            GridPane grid = new GridPane();

            grid.setAlignment(Pos.CENTER);
            grid.setPadding(new Insets(10));
            grid.setHgap(5);
            grid.setVgap(10);

            grid.addRow(0, new Label("Name:"), nameOfEdgeField);
            grid.addRow(1, new Label("Time:"), timeField);

            setHeaderText("Connection from " + from.getName() + " to " + to.getName());
            setTitle("Connection");
            getDialogPane().setContent(grid);
        }

        public String getName(){
            return nameOfEdgeField.getText();
        }
        public double getWeight(){
            try {
                return Double.parseDouble(timeField.getText());
            }
            catch (Exception e) {
                //error alert
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText(null);
                alert.setContentText("wrong input type!");

                alert.showAndWait();
            }
            return 0;
        }
    }
    class ShowConnectionDialog extends Alert{

        ShowConnectionDialog(City from, City to, Edge<City> edge){
            super(AlertType.CONFIRMATION);

            GridPane grid = new GridPane();

            grid.setAlignment(Pos.CENTER);
            grid.setPadding(new Insets(10));
            grid.setHgap(5);
            grid.setVgap(10);

            TextField nameOfEdgeField = new TextField();
            grid.addRow(0, new Label("Name:"), nameOfEdgeField);
            TextField timeField = new TextField();
            grid.addRow(1, new Label("Time:"), timeField);

            setHeaderText("Connection from " + from.getName() + " to " + to.getName());
            setTitle("Connection");

            nameOfEdgeField.setText(edge.getName());
            timeField.setText( "" + edge.getWeight());

            nameOfEdgeField.setDisable(true);
            timeField.setDisable(true);
            nameOfEdgeField.setStyle("-fx-opacity: 1;");
            timeField.setStyle("-fx-opacity: 1;");

            getDialogPane().setContent(grid);
        }
    }
    class ChangeConnectionDialog extends Alert{

        private TextField timeField = new TextField();

        ChangeConnectionDialog(City from, City to, Edge<City> edge){
            super(AlertType.CONFIRMATION);

            GridPane grid = new GridPane();

            grid.setAlignment(Pos.CENTER);
            grid.setPadding(new Insets(10));
            grid.setHgap(5);
            grid.setVgap(10);

            TextField nameOfEdgeField = new TextField();
            grid.addRow(0, new Label("Name:"), nameOfEdgeField);

            grid.addRow(1, new Label("Time:"), timeField);

            setHeaderText("Connection from " + from.getName() + " to " + to.getName());
            setTitle("Connection");

            nameOfEdgeField.setText(edge.getName());
            timeField.setText( "" + edge.getWeight());

            nameOfEdgeField.setDisable(true);
            nameOfEdgeField.setStyle("-fx-opacity: 1;");

            getDialogPane().setContent(grid);
        }

        public double getWeight(){
            try {
                return Double.parseDouble(timeField.getText());
            }
            catch (Exception e) {
                //error alert
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText(null);
                alert.setContentText("wrong input type!");

                alert.showAndWait();
            }
            return 0;
        }
    }
    class FindPathDialog extends Alert{

        FindPathDialog(City from, City to, List<Edge<City>> path){
            super(AlertType.INFORMATION);

            setTitle("Message");
            setHeaderText("The Path from " + from + " to " + to + ":");

            TextArea pathArea = new TextArea();

            int totalWeight = 0;
            for (Edge<City> edge : path) {
                pathArea.setText(pathArea.getText() + edge.toString() +"\r\n");
                totalWeight += edge.getWeight();
            }
            pathArea.setText(pathArea.getText() + "Total " + totalWeight);

            getDialogPane().setContent(pathArea);
            pathArea.setEditable(false);

            pathArea.setWrapText(true);
            pathArea.setMaxWidth(Double.MAX_VALUE);
            pathArea.setMaxHeight(Double.MAX_VALUE);
        }
    }
}
