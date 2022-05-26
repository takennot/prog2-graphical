import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Bricka2 extends Bricka{

    public Bricka2(double x, double y){
        super(x, y);
    }

    public void paintUncovered() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        gc.setFill(Color.GREEN);
        gc.fillPolygon(new double[] {0,25,50}, new double[] {50,25,50},3);
    }

    public boolean liknar(Bricka annan) {
        return annan instanceof Bricka2;
    }
}
