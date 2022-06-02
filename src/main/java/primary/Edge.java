// PROG2 VT2022, Inlämningsuppgift, del 1
// Grupp 055
// Saga Liljenroth Dickman sali3923
// Ruslan Musaev rumu4402

package primary;

import java.io.Serializable;
import java.util.*;

public class Edge<T> implements Serializable {
    /*The Edge class should represent an edge, that is, one direction of a connection
    between two nodes. Values for all instance variables in this class should be passed as
    argument to the constructor.*/

    private final T destination;
    private final String name;
    private double weight; //"length or time"

    public Edge (T destination, String name, double weight){
        this.destination = Objects.requireNonNull(destination);
        this.name = Objects.requireNonNull(name);

        if (Double.isNaN(weight))
            throw new IllegalArgumentException("weight assigned for Edge is Not a Number (NaN)");

        this.weight = weight;
    }

    public T getDestination(){
        //return the node that the edge points at
        return destination;
    }

    public String getName(){
        //returns the edge name
        return name;
    }

    public int getWeight(){
        //returns the edge weight
        return (int) weight;
    }

    public void setWeight(int newWeight){
        if (Double.isNaN(weight))
            throw new IllegalArgumentException("weight assigned for Edge is Not a Number (NaN)");

        this.weight = newWeight;
    }

    public boolean equals(Object edgeToCompare){
        if(edgeToCompare instanceof Edge edge){
            return name.equals(edge.name) && destination.equals(edge.destination);
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(name, destination);
    }

    @Override
    public String toString(){
        return "till " + destination + " med " + name + " tar " + (int) weight;
    }
}