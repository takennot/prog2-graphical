// PROG2 VT2022, Inlämningsuppgift, del 1
// Grupp 055
// Saga Liljenroth Dickman sali3923
// Ruslan Musaev rumu4402
package primary;
import java.io.Serializable;
import java.util.*;

public class ListGraph<T> implements Graph<T>, Serializable {

    //Map med noder
    private final Map<T, Set<Edge>> nodes = new HashMap<>();

    public void add(T node){
        // Takes a node as argument and add it to the graph. If it already exists - then don't change anything
        //compare node with other nodes
        //if it doesnt exist(absent), add to graph

        nodes.putIfAbsent(node, new HashSet<>());
    }

    public void remove(T nodeToRemove){
        // receives a node and removes it from the graph. If the node is missing in The graph it should throw NoSuchElementException from the java.util package generated.
        if(!nodes.containsKey(nodeToRemove)){
            throw new NoSuchElementException("No such node in the map");
        }

        // create new HashSet with edges of the argument node
        // from edge name get destination
        // and call disconnect method
        // after for loop call nodes.remove(node)

        Set<Edge<T>> edgeList = new HashSet<>(getEdgesFrom(nodeToRemove));
        for(Edge<T> edge : edgeList){
            T cityName = (T) edge.getDestination();
            disconnect(nodeToRemove, cityName);
        }
        nodes.remove(nodeToRemove);
    }

    public void connect(T cityOne, T cityTwo, String name, int weight){

        //If one of the nodes does not exist - NoSuchElementException from java.util should be thrown.
        if(!nodes.containsKey(cityOne) || !nodes.containsKey(cityTwo)){
            throw new NoSuchElementException("HashMap does not contain one of the cities! (1: " + cityOne.toString() + " 2: " + cityTwo.toString() + ")");
        }
        // If the weight is negative - IllegalArgumentException should be thrown.
        if(weight < 0){
            throw new IllegalArgumentException("Weight is negative!");
        }
        // if there is already a connection between the cities - IllegalStateException
        if(getEdgeBetween(cityOne, cityTwo) != null){
            throw new IllegalStateException("Edge already exists between " + cityOne + " and " + cityTwo + "!");
        }

        // Then we get the cities edges
        Set<Edge> cityOneEdges = nodes.get(cityOne);
        Set<Edge> cityTwoEdges = nodes.get(cityTwo);

        //add the new edge in the Sets
        cityOneEdges.add(new Edge(cityTwo, name, weight));
        cityTwoEdges.add(new Edge(cityOne, name, weight));
    }

    public void disconnect(T nodeOne, T nodeTwo){
        // the method takes two nodes and removes the edge that connects these nodes.

        // If any of the nodes are missing in the graph, the exception NoSuchElementException from the java.util package must be generated.
        if(!nodes.containsKey(nodeOne) || !nodes.containsKey(nodeTwo)){
            throw new NoSuchElementException("One of the nodes does not exist");
        }
        // If there is no edge between these two nodes, the IllegalStateException exception should be generated. (Here, the getEdgeBetween method can certainly be useful.)
        if(getEdgeBetween(nodeOne, nodeTwo) == null){
            throw new IllegalStateException("No edge between nodes");
        }

        Edge edgeOne = getEdgeBetween(nodeOne, nodeTwo);
        Edge edgeTwo = getEdgeBetween(nodeTwo, nodeOne);
        nodes.get(nodeOne).remove(edgeOne);
        nodes.get(nodeTwo).remove(edgeTwo);
    }

    public void setConnectionWeight(T node1, T node2, int newWeight){
        // takes two nodes and an integer (the new weight of the connection)
        // If the weight is negative, the IllegalArgumentException exception should be generated.
        if(newWeight < 0){
            throw new IllegalArgumentException("weight is negative");
        }

        // If any of the nodes are missing in the graph or there is no edge between these two nodes, the exception NoSuchElementException from the java.util package must be generated.

        //if nodes are missing
        Set<T> nodes = getNodes();

        if (!nodes.contains(node1) || !nodes.contains(node2)){
            throw new NoSuchElementException("Node is missing");
        }

        //get edges
        Edge edgeOne = getEdgeBetween(node1, node2);
        Edge edgeTwo = getEdgeBetween(node2, node1);

        //sets this new weight as the new weight of the connection between these two nodes.
        edgeOne.setWeight(newWeight);
        edgeTwo.setWeight(newWeight);

    }

    public Set<T> getNodes(){
        //returnerar en kopia av mängden av alla noder

        //Convert Map keys to Set
        Set<T> keySet = (Set<T>) new HashSet<T>(nodes.keySet());
        //keySet.forEach(key -> System.out.println(key));

        return (Set<T>) keySet;
    }

    public Collection<Edge<T>> getEdgesFrom(T node){
        //If the node is missing in the graph, the exception should be NoSuchElementException is generated.
        //the method takes a node and returns a copy of the collection of all edges leading from this node.
        if(!nodes.containsKey(node)){
            throw new NoSuchElementException("No such node in the map");
        }

        //the method takes a node and returns a copy of the collection of all edges leading from this node.

        Collection<Edge<T>> edges = new HashSet<>();

        edges = (Collection) nodes.get(node);

        return edges;
    }

    public Edge getEdgeBetween(T cityOne, T cityTwo){
        //If any of the nodes are missing in the graph, the exception should be NoSuchElementException is generated.
        //if(cityOne instanceof MapTile || cityTwo instanceof MapTile){
        //    throw new NoSuchElementException("One of the cities is a MapTile");
        //}

        //if the given cities is mapTiles, change them to cities
        if(cityOne instanceof MapTile){
            cityOne = (T) ((MapTile) cityOne).getCity();

        }
        if(cityTwo instanceof MapTile){
            cityTwo = (T) ((MapTile) cityTwo).getCity();

        }

        if(!nodes.containsKey(cityOne) || !nodes.containsKey(cityTwo)){
            throw new NoSuchElementException("One of those cities is not in the map");
        }

        //takes two nodes and returns the edge between these nodes.
        for (Edge edge : nodes.get(cityOne)){
            if(edge.getDestination().equals(cityTwo))
                return edge;
        }

        //If there is no border between the nodes are returned null.
        return null;
    }

    @Override
    public String toString(){
        // toString - returns a long string with strings taken from the nodes toString methods and the edges toString methods.
        StringBuilder sb = new StringBuilder();
        for (T city : nodes.keySet()) {
            sb.append(city).append(": ").append(nodes.get(city)).append("\n");
        }

        // Preferably with line breaks so that you get information about one node per line for improved readability.
        return sb.toString();
    }

    public boolean pathExists(T from, T to){
        //if the given cities is mapTiles, change them to cities
        if(from instanceof MapTile){
            from = (T) ((MapTile) from).getCity();

        }
        if(to instanceof MapTile){
            to = (T) ((MapTile) to).getCity();

        }

        // takes two nodes and returns true if there is a path through the graph from one node to the other (possibly over many other nodes), otherwise false is returned.
        // If any of the nodes are not in the graph, false is also returned. Uses an aid method for depth-first search through a graph.

        if(!nodes.containsKey(from) || !nodes.containsKey(to)){
            return false;
        }

        Set<T> visited = new HashSet<>();
        depthFirstVisitAll(from, visited);
        return visited.contains(to);
    }

    private void depthFirstVisitAll(T currentCity, Set<T> visited){
        visited.add(currentCity);
        for (Edge edge : nodes.get(currentCity)) {
            if (!visited.contains(edge.getDestination())) {
                depthFirstVisitAll((T) edge.getDestination(), visited);
            }
        }
    }

    public List<Edge<T>> getPath(T from, T to){
        //takes two nodes and returns a list (java.util.List) with borders which represents a path between these nodes through the graph, or null if there is no path between these two nodes.

        //In the simplest variant it is therefore sufficient that the method returns some path between the two nodes,
        //but voluntarily one can make a solution where returns the shortest path (in the number of edges that must be crossed) or the fastest route (taking into account taken to the weights of the edges).

        Map<T, T> connections = new HashMap<>();
        connections.put(from, null);

        LinkedList<T> queue = new LinkedList<>();
        queue.add(from);

        while (!queue.isEmpty()) {
            T city = queue.pollFirst();
            for (Edge edge : nodes.get(city)) {
                T destination = (T) edge.getDestination();
                if (!connections.containsKey(destination)) {
                    connections.put(destination, city);
                    queue.add(destination);
                }
            }
        }

        if (!connections.containsKey(to)) {
            return null;
        }

        return gatherPath(from, to, connections);
    }

    private List<Edge<T>> gatherPath(T from, T to, Map<T, T> connection) {
        LinkedList<Edge<T>> path = new LinkedList<>();
        T current = to;
        while (!current.equals(from)) {
            T next = connection.get(current);
            Edge edge = getEdgeBetween(next, current);
            path.addFirst(edge);
            current = next;
        }
        return Collections.unmodifiableList(path);
    }
}