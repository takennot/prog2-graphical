import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class FolkRegisterGUI extends Application {

    private Map<Persnr, Person> folk = new TreeMap<>();
    private TextField pnrField, nameField, weightField;
    private TextArea display;
    private RadioButton persButton, nameButton;

    @Override public void start(Stage primaryStage){
        BorderPane root = new BorderPane();
        root.setStyle("-fx-font-size:18");

        FlowPane top = new FlowPane();
        top.setPadding(new Insets(5));
        top.setHgap(5);
        top.setAlignment(Pos.CENTER);

        top.getChildren().add(new Label("Pnr:"));
        pnrField = new TextField();
        top.getChildren().add(pnrField);
        top.getChildren().add(new Label("Namn:"));
        nameField = new TextField();
        top.getChildren().add(nameField);
        top.getChildren().add(new Label("Vikt:"));
        weightField = new TextField();
        top.getChildren().add(weightField);

        root.setTop(top);

        VBox right = new VBox();
        right.getChildren().add(new Label("Sortering"));
        persButton = new RadioButton("Persnr");
        persButton.setSelected(true);
        nameButton = new RadioButton("Namn");
        right.getChildren().addAll(persButton, nameButton);
        ToggleGroup tg = new ToggleGroup();
        persButton.setToggleGroup(tg);
        nameButton.setToggleGroup(tg);

        root.setRight(right);

        display = new TextArea();
        display.setEditable(false);

        root.setCenter(display);

        Button newButton = new Button("Ny");
        newButton.setOnAction(new NewHandler());
        Button allButton = new Button("Alla");
        allButton.setOnAction(new AllHandler());
        Button loadButton = new Button("Ladda");
        loadButton.setOnAction(new LaddaHandler());
        Button saveButton = new Button("Spara");
        saveButton.setOnAction(new SparaHandler());
        FlowPane bottom = new FlowPane();
        bottom.getChildren().addAll(newButton, allButton, loadButton, saveButton);
        bottom.setAlignment(Pos.CENTER);
        bottom.setPadding(new Insets(5));
        bottom.setHgap(5);

        root.setBottom(bottom);

        Scene scene = new Scene(root,1000, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    class NewHandler implements EventHandler<ActionEvent>{
        @Override public void handle(ActionEvent event){
            try {
                Persnr pnr = Persnr.parsePersnr(pnrField.getText());
                String name = nameField.getText();
                int weight = Integer.parseInt(weightField.getText());
                Person person = new Person(pnr, name, weight);
                folk.put(person.getPnr(), person);
            }catch(NumberFormatException e){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Fel nummer!");
                alert.showAndWait();
            }
        }
    }

    class AllHandler implements EventHandler<ActionEvent>{
        @Override public void handle(ActionEvent event){
            display.clear();
            List<Person> lista = new ArrayList<>(folk.values());
            if (nameButton.isSelected())
                lista.sort( (p1,p2) -> p1.getName().compareTo(p2.getName()));
            for(Person p : lista)
                display.appendText(p.getPnr() + " " + p.getName() + " " + p.getVikt() + "\n");
        }
    }

    class LaddaHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent event){
            folk.clear();
            try{
                FileReader reader = new FileReader("folk.csv");
                BufferedReader in = new BufferedReader(reader);
                String line;
                while ((line = in.readLine()) != null){
                    Person person = parseLine(line);
                    folk.put(person.getPnr(), person);
                }
                in.close();
                reader.close();
            }catch(FileNotFoundException e){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Kan inte öppna filen!");
                alert.showAndWait();
            }catch(IOException e){
                Alert alert = new Alert(Alert.AlertType.ERROR, "IO-fel " + e.getMessage());
                alert.showAndWait();
            }
        }
        // Borde kanske hantera ev. fel i filen, men det görs inte här
        private Person parseLine(String line){
            String[] tokens = line.split(",");
            Persnr pnr = Persnr.parsePersnr(tokens[0]);
            String namn = tokens[1];
            int vikt = Integer.parseInt(tokens[2]);
            return new Person(pnr, namn, vikt);
        }
    }

    class SparaHandler implements EventHandler<ActionEvent>{
        public void handle(ActionEvent event){
            try{
                FileWriter writer = new FileWriter("folk.csv");
                PrintWriter out = new PrintWriter(writer);
                for(Person p : folk.values())
                    out.println(p.getPnr() + "," + p.getName() + "," + p.getVikt());
                out.close();
                writer.close();
            }catch(FileNotFoundException e){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Kan inte öppna filen!");
                alert.showAndWait();
            }catch(IOException e){
                Alert alert = new Alert(Alert.AlertType.ERROR, "IO-fel " + e.getMessage());
                alert.showAndWait();
            }
        }
    }

}
