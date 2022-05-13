package primary;

import java.util.*;

public class City{
    private final String name;

    public City(String cityName){
        this.name = cityName;
    }

    public String getName(){
        return name;
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