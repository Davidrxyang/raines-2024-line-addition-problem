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

    ArrayList<Connection> connections;
    // ArrayList<Station> stations; // will this be helpful?? 

    public Line(String name) {
        this.name = name;
        connections = new ArrayList<>();
    }

    /*
     * two methods to construct a line
     * 
     * 1. add a bunch of station IN ORDER - connections generated automatically
     * 2. add a bunch of connections (unordered??) and specify origin and destination
     */
}
