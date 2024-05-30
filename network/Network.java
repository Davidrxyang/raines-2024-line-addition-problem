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
    public ArrayList< ArrayList< Double > > matrix;

    public Network(ArrayList<Station> stationList) {
        this.stationList = stationList;
        nStations = stationList.size();
        for (int i = 0; i < nStations; i++) {
            stations.put(stationList.get(i).name, stationList.get(i));
        }
        connections = new ArrayList<>();
        lines = new ArrayList<>();

        // instantiate and populate the matrix
        matrix = new ArrayList<>();

        for (int i = 0; i < nStations; i++) {
            matrix.add(new ArrayList<Double>(nStations));
        }
    }




}
