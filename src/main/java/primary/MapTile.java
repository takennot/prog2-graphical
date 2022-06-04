// PROG2 VT2022, Inlämningsuppgift, del 2
// Grupp 055
// Saga Liljenroth Dickman sali3923
// Ruslan Musaev rumu4402
package primary;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MapTile extends Canvas {
    private final City city;

    private double radius;
    private double diameter;

    public MapTile(City city, double radius){
        super(radius * 2, radius * 2);

        this.radius = radius;
        diameter = radius * 2;

        relocate(city.getX(), city.getY());
        paintNotSelected();

        this.city = city;

        setTranslateX(0 - radius);
        setTranslateY(0 - radius);
    }

    public void paintSelected(){
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        gc.setFill(Color.RED);
        gc.fillOval(0, 0, getWidth(), getHeight());
    }

    public void paintNotSelected(){
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        gc.setFill(Color.BLUE);
        gc.fillOval(0, 0, getWidth(), getHeight());
    }

    public City getCity(){
        return city;
    }
}
