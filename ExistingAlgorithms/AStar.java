/*
 * A* algorithm to find a route between two stations 
 * within a public transportation network, minimizing
 * path cost. 
 */

package ExistingAlgorithms;

import Network.*;
import java.util.PriorityQueue;
import java.util.HashMap;
import java.util.Comparator;
import java.util.ArrayList;

public class AStar extends PathPlanning {

    public Network network;
    Double c1, c2, c3; // constants for path cost weights

    public AStar(Network network) {
        this.network = network;
        // c1 = 0.2;
        // c2 = 10.0;
        // c3 = 0.8;
        c1 = 1.0;
        c2 = 0.0; // disabling transfers for now since when looking at stations with multiple lines, only one line is considered
        c3 = 0.0;
    }

    /*
     * finding the least cost route
     */
    public Path pathPlan(Station origin, Station destination) {
        HashMap<Station, Path> reached = new HashMap<>();
        PriorityQueue<Path> frontier = new PriorityQueue<Path>(new Comparator<Path>() {
            public int compare(Path p1, Path p2) {
                return totalCost(p1, origin, destination)
                        .compareTo(totalCost(p2, origin, destination));
            }
        });

        Path initialPath = new Path();
        initialPath.addStation(origin, null);
        initialPath.update();

        Path currentPath = initialPath;

        reached.put(initialPath.destination, initialPath);

        frontier.add(initialPath);

        while(!frontier.isEmpty()) {
            currentPath = frontier.peek();
            currentPath.update();
            frontier.remove();

            if(currentPath.destination.equals(destination)) {
                return currentPath;
            }

            Station currentStation = currentPath.destination;
            ArrayList<Station> neighbors = network.getNeighbors(currentStation);

            for (Station neighbor : neighbors) {
                Double cost = totalCost(currentPath, origin, destination);
                Path newPath = new Path(currentPath);
                Connection connection = network.getConnection(currentStation, neighbor);
                newPath.addStation(neighbor, connection.distance);
                newPath.update();

                if (!reached.containsKey(neighbor)) {
                    reached.put(neighbor, newPath);
                    frontier.add(newPath);
                }
                else if (cost < totalCost(reached.get(neighbor), origin, destination)) {
                    frontier.remove(reached.get(neighbor));
                    reached.replace(neighbor, newPath);
                    frontier.add(newPath);
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

        Path path = pp.pathPlan(WMATA.WMATA.getStation("pentagon city"), WMATA.WMATA.getStation("lenfant plaza"));
        System.out.println(path);
    }
    
}
