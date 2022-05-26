import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class BildBricka extends Bricka{
    private String filnamn;
    private Image image;

    public BildBricka(double x, double y, String filnamn){
        super(x, y);
        this.filnamn = filnamn;
        image = new Image("file:" + filnamn);
    }

    public void paintUncovered() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        gc.drawImage(image, 0, 0, getWidth(), getHeight());
    }

    public boolean liknar(Bricka annan) {
        return annan instanceof BildBricka bildBricka && bildBricka.filnamn.equals(filnamn);
    }
}
