/*
 * A* algorithm to find a route between two stations 
 * within a public transportation network, minimizing
 * path cost. 
 */

package ExistingAlgorithms;

import Network.*;
import java.util.PriorityQueue;
import java.util.HashSet;
import java.util.Set;

public class AStar extends PathPlanning {

    public Network network;
    Double c1, c2, c3; // constants for path cost weights

    public AStar(Network network) {
        this.network = network;
        c1 = 0.2;
        c2 = 1.0;
        c3 = 0.8;
    }

    /*
     * finding the least cost route
     */

    public Path pathPlan(Station origin, Station destination) {
        PriorityQueue<Path> frontier = new PriorityQueue<>((path1, path2) -> Double.compare(getCost(path1) + heuristic(path1.getLastStation(), destination), getCost(path2) + heuristic(path2.getLastStation(), destination)));
        Path initialPath = new Path();
        initialPath.addStation(origin, null);
        frontier.add(initialPath);

        Set<Station> explored = new HashSet<>();

        int iterations = 0;

        while (!frontier.isEmpty()) {
            iterations++;
            System.out.println("iteration: " + iterations);
            Path currentPath = frontier.poll(); // remove throws exception, poll returns null, same otherwise
            Station currentStation = currentPath.getLastStation(); // Assuming getLastStation returns the last station in the path

            if (currentStation.equals(destination)) {
                currentPath.findLines();
                return currentPath;
            }

            explored.add(currentStation);

            for (Station neighbor : network.getNeightbors(currentStation)) { // Assuming getNeighbors returns directly connected stations
                if (!explored.contains(neighbor)) {
                    Path newPath = new Path(currentPath); // Assuming this constructor copies the path
                    Connection connection = network.getConnection(currentStation, neighbor); 
                    newPath.addStation(neighbor, connection.distance); // Add neighbor to the path
                    if (!frontier.contains(newPath)) {
                        frontier.add(newPath);
                    }
                }
            }
        }


        return null;
    }

    public Double getCost(Path path) {
        return c1 * path.length + c2 * path.nTransfers + c3 * path.stations.size();
    }

    public Double heuristic(Station current, Station destination) {
        return current.getDistance(destination);
    }

    public static void main(String[] args) {
        WMATA WMATA = new WMATA();

        PathPlanning pp = new AStar(WMATA.WMATA);

        Path path = pp.pathPlan(WMATA.WMATA.getStation("glenmont"), WMATA.WMATA.getStation("west hyattsville"));
        System.out.println(path);
    }
    
}
