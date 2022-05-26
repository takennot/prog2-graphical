import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Bricka3 extends Bricka{

    public Bricka3(double x, double y){
        super(x, y);
    }

    public void paintUncovered() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        gc.setFill(Color.RED);
        gc.fillOval(0, 0, getWidth(), getHeight());
        gc.setFill(Color.BLACK);
        gc.fillOval(10, 10, getWidth()-20, getHeight()-20);
    }

    public boolean liknar(Bricka annan) {
        return annan instanceof Bricka3;
    }
}