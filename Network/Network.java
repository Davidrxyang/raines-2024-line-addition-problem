/*
 * A collection of stations, connections, and routes which together represent a 
 * metro network
 * 
 */
package Network; 

import java.util.HashMap;
import java.util.ArrayList;

import Network.Station;

public class Network {

    String name;

    // nodes
    public ArrayList<Station> stationList;
    public HashMap<String, Station> stations;
    int nStations;

    // edges
    public ArrayList<Connection> connections;
    public HashMap<String, Connection> connectionMap;

    // lines
    public ArrayList<Line> lines;
    public int nLines;
    // public HashMap<String, Line> lines;

    // adjacency distanceMatrix
    public Double[][] distanceMatrix;
    public int[][] lineMatrix;

    public Network(String name, ArrayList<Station> stationList) {

        this.name = name;

        stations = new HashMap<>();
        this.stationList = stationList;
        nStations = stationList.size();

        // add stations to the map, ALSO set the station index member

        for (int i = 0; i < nStations; i++) {
            stations.put(stationList.get(i).name, stationList.get(i));

            stationList.get(i).index = i;
        }
        connections = new ArrayList<>();
        connectionMap = new HashMap<>();
        lines = new ArrayList<>();
        nLines = 0;

        // instantiate and populate the distanceMatrix
        distanceMatrix = new Double[nStations][nStations];
        lineMatrix = new int[nStations][nStations];

        for (int i = 0; i < nStations; i++) {
            for (int j = 0; j < nStations; j++) {
                distanceMatrix[i][j] = -1.0;
                lineMatrix[i][j] = 0;
            }
        }
    }

    

    /*
     * the network is constructed iteratively by adding lines, each line
     * will update the connectivity information stored in the distanceMatrix
     */

    public void addLine(Line line) {
        nLines++;
        lines.add(line);

        for (Connection connection : line.connections) {
            connectionMap.put(connection.toString(), connection);
        }

        // iterate through the set of connections encapsulated within the line
        
        for (int i = 0; i < line.connections.size(); i++) {

            Connection currentConnection = line.connections.get(i);

            /*
             * adds each connection in the current line to the total set
             * of connections for the network
             */

            connections.add(currentConnection);

            /*
             * update the connectivity distanceMatrix value to be the distance
             */
            distanceMatrix[currentConnection.origin.index][currentConnection.destination.index] = currentConnection.distance;
            lineMatrix[currentConnection.origin.index][currentConnection.destination.index]++;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("----------").append(name).append("----------\n");

        for (int i = 0; i < nStations; i++) {
            for (int j = 0; j < nStations; j++) {
                sb.append(" ").append(distanceMatrix[i][j] != -1.0).append(" ");
            }
            sb.append("\n");
        }

        sb.append("----------------------------\n");

        for (int i = 0; i < nStations; i++) {
            for (int j = 0; j < nStations; j++) {
                sb.append(" ").append(lineMatrix[i][j]).append(" ");
            }
            sb.append("\n");
        }

        sb.append("----------------------------\n");

        return sb.toString();
    }

    public Station getStation(String name) {
        return stations.get(name);
    }

    // dfs function for PIA
    // finds the shortest path between two stations
    // using existing connections
    public Line bfs(Station start, Station end, String pathname) {
        ArrayList<Station> visited = new ArrayList<>();
        ArrayList<Station> queue = new ArrayList<>();
        HashMap<Station, Station> parent = new HashMap<>();
        ArrayList<Station> path = new ArrayList<>();

        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            Station current = queue.remove(0);

            if (current == end) {
                Station node = end;
                while (node != start) {
                    path.add(node);
                    node = parent.get(node);
                }
                path.add(start);

                Line l = new Line(pathname);
                for (int i = path.size() - 2; i > 0; i--) {
                    l.insertStation(path.get(i), path.size() - 2 - i);
                }
                return l;
            }

            for (Connection c : connections) {
                if (c.origin == current && !visited.contains(c.destination)) {
                    visited.add(c.destination);
                    queue.add(c.destination);
                    parent.put(c.destination, c.origin);
                } else if (c.destination == current && !visited.contains(c.origin)) {
                    visited.add(c.origin);
                    queue.add(c.origin);
                    parent.put(c.origin, c.destination);
                }
            }
        }

        return null;
    }

    public ArrayList<Station> getNeighbors(Station s) {
        ArrayList<Station> neighbors = new ArrayList<>();
        for (Connection c : connections) {
            if (c.origin == s) {
                neighbors.add(c.destination);
            } else if (c.destination == s) {
                neighbors.add(c.origin);
            }
        }
        return neighbors;
    }

    public Connection getConnection(Station origin, Station destination) {
        return connectionMap.get(origin.name + " -> " + destination.name);
    }

}
