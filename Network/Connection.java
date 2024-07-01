/*
 * one connection between two stations, represents the distance of one stop 
 * on the metro
 * 
 * one directed edge in the underlying graph network
 * 
 * note that this connection is uni-directional, which means that, for each 
 * pair of consecutive stations on any given line in the network, there should
 * be two connections:
 * 
 * connection 1: [foggy bottom -> rosslyn]
 * connection 2: [rosslyn -> foggy bottom]
 */

package Network; 

import Network.Station;

public class Connection {

    Station origin;
    Station destination;
    Double distance;

    /*
     * add data members for ridership data? 
     */

    public Connection(Station origin, Station destination, Double distance) {
        this.origin = origin;
        this.destination = destination;
        this.distance = distance;
    }

    public String toString() {
        return (new StringBuilder()).append(origin.name).append(" -> ").append(destination.name).toString();
    }
}
