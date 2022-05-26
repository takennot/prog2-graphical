package primary.Exempelkoder;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Bricka1 extends Bricka{
    public Bricka1(double x, double y){
        super(x,y);
    }
    public void paintUncovered(){
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        gc.setFill(Color.RED);
        gc.fillOval(0,0, getWidth(), getHeight());
    }

    public boolean liknar(Bricka other){
        return other instanceof Bricka1;
    }
}
