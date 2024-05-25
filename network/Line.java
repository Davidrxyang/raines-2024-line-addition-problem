/*
 * a collection of connections that together represent one transit line 
 * 
 * each line is DIRECTED, since lines in reality go in both directions, 
 * there will be two line instantiations for each line, one for each 
 * direction
 */

import java.util.ArrayList;

public class Line {
    String name;
    Station origin;
    Station destination; // this is the direction of the line

    /*
     * note: the arraylist of connections is not necessarily in correct order,
     * since each connected is directed and unique, and each station, except for
     * the line's origin and destination, must occur exactly twice in the network, once 
     * as an origin for some connection and once as the destination for some connection
     */

    ArrayList<Connection> connections;
    ArrayList<Station> stations; 

    public Line(String name) {
        this.name = name;
        connections = new ArrayList<>();

        origin = null;
        destination = null;
    }

    /*
     * these setter functions are dangerous
     */

    public void setOrigin(Station station) {
        this.origin = station;
    }

    public void setDestination(Station station) {
        this.destination = station;
    }

    /*
     * two methods to construct a line
     * 
     * 1. add a bunch of stations IN ORDER - connections generated automatically
     * 2. add a bunch of connections (unordered??) and specify origin and destination
     */

    /*
     * METHOD 1
     * 
     * add stations in correct order one by one and construct connections between them
     * automatically
     */

    public void addStation(Station station, Double distance) {
        if (origin == null) {
            // this is the first station in the network
            origin = station;
        }
        else {
            Station lastStation = connections.getLast().destination;
            Connection newConnection = new Connection(lastStation, station, distance);
            connections.add(newConnection);
        }
        destination = station;
        stations.add(station);
    }

    /*
     * METHOD 2
     * 
     * directly add connections to the line, separately specify origin and destination
     */

     public void addConnection(Connection connection) {
        connections.add(connection);
     }

     public void addConnection(ArrayList<Connection> connections) {
        connections.addAll(connections);
     }
}
