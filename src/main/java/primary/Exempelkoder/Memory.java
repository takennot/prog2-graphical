import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Memory extends Application {
    private Bricka bricka1 = null, bricka2 = null;
    private Pane center;
    private int hits = 0, tries = 0;
    private Label hitsLabel, triesLabel;

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        center = new Pane();
        Image image = new Image("file:C:/images/memory/bg.jpg");
        ImageView imageView = new ImageView(image);
        center.getChildren().add(imageView);
        root.setCenter(center);

        FlowPane bottom = new FlowPane();
        bottom.setPadding(new Insets(5));
        bottom.setHgap(5);
        bottom.setAlignment(Pos.CENTER);
        bottom.getChildren().add(new Label("Antal försök:"));
        triesLabel = new Label("0");
        bottom.getChildren().add(triesLabel);
        bottom.getChildren().add(new Label("Antal träffar:"));
        hitsLabel = new Label("0");
        bottom.getChildren().add(hitsLabel);
        Button testButton = new Button("Testa");
        testButton.setOnAction(new TestHandler());
        bottom.getChildren().add(testButton);
        root.setBottom(bottom);


        ClickHandler clickHandler = new ClickHandler();
        Bricka[] brickor = {
                new Bricka1(112,87),
                new Bricka1(318,145),
                new Bricka2(12,305),
                new Bricka2(219, 290),
                new Bricka3(510, 25),
                new Bricka3(430,270),
                new BildBricka(12,400,"C:/images/memory/bart.gif"),
                new BildBricka(300, 17, "C:/images/memory/bart.gif"),
                new BildBricka(40, 207, "C:/images/memory/ande.gif"),
                new BildBricka(207, 40, "C:/images/memory/ande.gif")
        };

        for(Bricka b : brickor){
            center.getChildren().add(b);
            b.setOnMouseClicked(clickHandler);
        }

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Memory");
        stage.show();

        // Ett sätt att automatiskt anpassa bildens storlek (bredden) till fönstret
        stage.widthProperty().addListener((obs, oldValue, newValue) ->
                imageView.setFitWidth((Double) newValue));

        // Ett annat sätt att anpassa bildens storlek (höjden) till fönstret
        imageView.fitHeightProperty().bind(center.heightProperty());

    }

    class ClickHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            Bricka b = (Bricka) event.getSource();
            if (bricka1 == null) {
                bricka1 = b;
                b.paintUncovered();
            } else if (bricka2 == null && b != bricka1) {
                bricka2 = b;
                b.paintUncovered();
            }
        }
    }

    class TestHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            if (bricka1 != null && bricka2 != null) {
                if (bricka1.liknar(bricka2)) {
                    center.getChildren().remove(bricka1);
                    center.getChildren().remove(bricka2);
                    hitsLabel.setText("" + ++hits);
                }
                else {
                    bricka1.paintCovered();
                    bricka2.paintCovered();
                }
                triesLabel.setText("" + ++tries);
                bricka1 = bricka2 = null;
            }
        }

    }
}
