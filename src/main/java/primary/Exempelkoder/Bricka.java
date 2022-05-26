package primary.Exempelkoder;

import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

//public class Bricka extends Rectangle {
abstract public class Bricka extends Canvas {
    public Bricka(double x, double y){
        super(50, 50);
        relocate(x,y);
        paintCovered();
    }

    public void paintCovered(){
        GraphicsContext gc = getGraphicsContext2D();
        gc.setFill(Color.BLUE);
        gc.fillRect(0, 0, getWidth(), getHeight());
    }
    public abstract void paintUncovered();
    public abstract boolean liknar(Bricka other);
 }
