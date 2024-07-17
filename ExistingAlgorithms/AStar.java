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
            Path currentPath = frontier.poll(); // Assuming frontier is a PriorityQueue that orders paths by their total cost
            Station currentStation = currentPath.getLastStation();
        
            if (currentStation.equals(destination)) {
                currentPath.findLines(); // Now it's efficient to find lines and calculate length
                currentPath.calculateLength();
                return currentPath;
            }
        
            explored.add(currentStation);
        
            for (Station neighbor : network.getNeightbors(currentStation)) {
                if (!explored.contains(neighbor)) {
                    Path newPath = new Path(currentPath);
                    newPath.calculateLength();
                    newPath.findLines();
                    Connection connection = network.getConnection(currentStation, neighbor);
                    newPath.addStation(neighbor, connection.distance);
        
                    // Instead of checking if newPath is in frontier, we add paths to the frontier with their total cost
                    // The PriorityQueue will automatically handle duplicates based on the comparator logic
                    if (!frontier.contains(newPath) || totalCost(newPath, neighbor, destination) < totalCost(currentPath, currentStation, destination)) {
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

    private Double totalCost(Path path, Station current, Station goal) {
        // This method should calculate the total cost of the path including the heuristic from current to goal
        return getCost(path) + heuristic(current, goal); // Placeholder for actual implementation
    }
    public static void main(String[] args) {
        WMATA WMATA = new WMATA();

        PathPlanning pp = new AStar(WMATA.WMATA);

        Path path = pp.pathPlan(WMATA.WMATA.getStation("glenmont"), WMATA.WMATA.getStation("columbia heights"));
        System.out.println(path);
    }
    
}
