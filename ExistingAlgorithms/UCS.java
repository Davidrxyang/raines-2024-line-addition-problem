/*
 * A* algorithm to find a route between two stations 
 * within a public transportation network, minimizing
 * path cost. 
 */

package ExistingAlgorithms;

import Network.*;

public class UCS {

    public Network network;

    public UCS(Network network) {
        this.network = network;
    }

    /*
     * finding the least cost route
     */

    public Path pathPlan(Station origin, Station destination) {
        Path path = new Path();
        path.setOrigin(origin);
        path.setDestination(destination);
        path.nTransfers = 0;

        // check if equal
        if (origin.equals(destination)) {
            path.lines.add(origin.lines.get(0));
            return path;
        }



        return path;
    }
    
}
