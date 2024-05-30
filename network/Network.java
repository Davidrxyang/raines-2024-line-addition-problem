/*
 * A collection of stations, connections, and routes which together represent a 
 * metro network
 * 
 */

import java.util.HashMap;
import java.util.ArrayList;

public class Network {

    // nodes
    public ArrayList<Station> stationList;
    public HashMap<String, Station> stations;
    int nStations;

    // edges
    public ArrayList<Connection> connections;

    // lines
    public ArrayList<Line> lines;
    // public HashMap<String, Line> lines;

    // adjacency matrix
    public Double[][] matrix;

    public Network(ArrayList<Station> stationList) {
        this.stationList = stationList;
        nStations = stationList.size();

        // add stations to the map, ALSO set the station index member

        for (int i = 0; i < nStations; i++) {
            stations.put(stationList.get(i).name, stationList.get(i));

            stations.get(i).index = i;
        }
        connections = new ArrayList<>();
        lines = new ArrayList<>();

        // instantiate and populate the matrix
        matrix = new Double[nStations][nStations];

        for (int i = 0; i < nStations; i++) {
            for (int j = 0; j < nStations; j++) {
                matrix[i][j] = -1.0;
            }
        }
    }

    /*
     * the network is constructed iteratively by adding lines, each line
     * will update the connectivity information stored in the matrix
     */

    public void addLine(Line line) {
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
             * update the connectivity matrix value to be the distance
             */
            matrix[currentConnection.origin.index][currentConnection.destination.index] = currentConnection.distance;
        }
    }


}
