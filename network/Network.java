/*
 * A collection of stations, connections, and routes which together represent a 
 * metro network
 * 
 */

import java.util.HashMap;
import java.util.ArrayList;

public class Network {

    String name;

    // nodes
    public ArrayList<Station> stationList;
    public HashMap<String, Station> stations;
    int nStations;

    // edges
    public ArrayList<Connection> connections;

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
        connections.addAll(line.connections);

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
}
