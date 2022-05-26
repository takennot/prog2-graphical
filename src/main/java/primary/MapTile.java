package primary;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MapTile extends Canvas {
    public City city;

    private double radius;
    private double diameter;

    public MapTile(City city, double radius){
        super(radius * 2, radius * 2);

        this.radius = radius;
        diameter = radius * 2;

        relocate(city.getX() - radius, city.getY() - radius);
        paintCovered();

        this.city = city;
    }

    public void paintCovered(){
        GraphicsContext gc = getGraphicsContext2D();
        gc.setFill(Color.BLUE);
        //gc.fillRect(0, 0, getWidth(), getHeight());
        //gc.fillOval(getWidth() - (getWidth() / 2), getHeight() - (getHeight() / 2), getWidth(), getHeight());
        gc.fillOval(0, 0, getWidth(), getHeight());
    }

    public void paintUncovered(){
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        gc.setFill(Color.RED);
        gc.fillOval(0, 0, getWidth(), getHeight());
    }
}
