// PROG2 VT2022, Inlämningsuppgift, del 1
// Grupp 055
// Saga Liljenroth Dickman sali3923
// Ruslan Musaev rumu4402
package primary;
import java.util.*;

public class City {
    private final String name;

    private final float x;
    private final float y;

    public City(String cityName, float xcoordinate, float ycoordinate){
        this.name = cityName;

        x = xcoordinate;
        y = ycoordinate;
    }

    public String getName(){
        return name;
    }
    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }

    public boolean equals(Object cityToCompare) {
        if (cityToCompare instanceof City city) {
            return name.equals(city.name);
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(name);
    }

    public String toString() {
        return name;
    }
}