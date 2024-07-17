/*
 * a collection of connections that together represent one transit PATH
 */
package Network; 

import java.util.ArrayList;

import Network.Station;
import ExistingAlgorithms.PathPlanning;
import ExistingAlgorithms.LeastTransfers;

public class Path {
    public Station origin;
    public Station destination; // this is the direction of the line
    public Double length;

    public int nTransfers;

    /*
     * note: the arraylist of connections is not necessarily in correct order,
     * since each connected is directed and unique, and each station, except for
     * the line's origin and destination, must occur exactly twice in the network,
     * once
     * as an origin for some connection and once as the destination for some
     * connection
     */

    public ArrayList<Connection> connections;
    public ArrayList<Station> stations;
    public ArrayList<Line> lines;

    public Path() {
        this.connections = new ArrayList<>();
        this.stations = new ArrayList<>();
        this.lines = new ArrayList<>();

        this.origin = null;
        this.destination = null;
        this.length = 0.0;
        this.nTransfers = 0;
    }

    // copy constructor
    // used in existing algorithms to insert stations into lines
    public Path(Path l) {
        this.connections = new ArrayList<>(l.connections);
        this.stations = new ArrayList<>(l.stations);
        this.lines = new ArrayList<>(l.lines);

        this.origin = l.origin;
        this.destination = l.destination;
        this.length = l.length;
        this.nTransfers = l.nTransfers;
    }

    public void update() {
        calculateLength();
        findLines();
    }

    public void buildPath(Network network, Line line, Station origin, Station destination) {

        Station currentStation = destination;
        stations.add(currentStation);
        lines.add(line);

        while(!currentStation.equals(origin)) {
            Station firstStation = line.getPreviousStation(currentStation);
            String connectionName = firstStation.name + " -> " + currentStation.name;
            Connection c = network.connectionMap.get(connectionName);
            connections.add(c);
            currentStation = firstStation;
            stations.add(currentStation);
            // System.out.println("current connection made: " + c + " on " + line.name);
        }
    }

    public void findLines() {
        ArrayList<Line> foundLines = new ArrayList<>();
        
        if (stations.size() == 1) {
            foundLines.add(origin.lines.get(0));
            lines = foundLines;
            return;
        }

        for (int i = 0; i < stations.size() - 1; i++) {
            Station currentStation = stations.get(i);
            Station nextStation = stations.get(i + 1);
            Line currentLine = currentStation.getCommonLines(nextStation).get(0);
            if (!foundLines.contains(currentLine)) {
                foundLines.add(currentLine);
            }
        }

        lines = foundLines;
        nTransfers = lines.size() - 1;
    }

    /*
     * these setter functions are dangerous
     */

    public void setOrigin(Station station) {
        this.origin = station;
    }

    public void setDestination(Station station) {
        this.destination = station;
    }
 
    public void calculateLength() {
        for (int i = 0; i < connections.size(); i++) {
            length += connections.get(i).distance;
        }
    }

    public Double getLength() {
        if (length == 0.0) {
            calculateLength();
        }
        return length;
    }

    public Boolean empty() {
        return connections.size() == 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Path from ").append(origin.name).append(" to ").append(destination.name).append("\n");
        sb.append("\n");
        sb.append("Lines: \n");
        for (Line l : lines) {
            sb.append(l.name).append("\n");
        }
        sb.append("\n");
        sb.append("Transfers: ").append(nTransfers).append("\n");
        sb.append("Connections: \n");
        for (Connection c : connections) {
            sb.append(c + "\n");
        }
        return sb.toString();

    }

    /*
     * two methods to construct a line
     * 
     * 1. add a bunch of stations IN ORDER - connections generated automatically
     * 2. add a bunch of connections (unordered??) and specify origin and
     * destination
     */

    /*
     * METHOD 1
     * 
     * add stations in correct order one by one and construct connections between
     * them
     * automatically
     */

    public void addStation(Station station, Double distance) {
        if (origin == null) {
            // this is the first station in the network
            origin = station;
        } else {
            Station lastStation;
            if (empty()) {
                lastStation = origin;
            } else {
                lastStation = connections.get(connections.size() - 1).destination;
            }
            Connection newConnection = new Connection(lastStation, station, distance);
            connections.add(newConnection);
        }
        destination = station;
        stations.add(station);
    }

    /*
     * METHOD 2
     * 
     * directly add connections to the line, separately specify origin and
     * destination
     */

    public void addConnection(Connection connection) {
        connections.add(connection);
    }

    public void addConnection(ArrayList<Connection> connections) {
        connections.addAll(connections);
    }

    public ArrayList<Station> commonStations(Line line) {
        ArrayList<Station> commonStations = new ArrayList<>();
        for (Station station : stations) {
            if (line.stations.contains(station)) {
                commonStations.add(station);
            }
        }
        return commonStations;
    }

    // order the stations in the line
    // using the connection information
    public void sort() {
        ArrayList<Station> sortedStationOrder = new ArrayList<>();
        ArrayList<Connection> sortedConnectionOrder = new ArrayList<>();
        Station current = origin;

        while (current != destination) {
            sortedStationOrder.add(current);
            for (Connection c : connections) {
                if (c.origin == current && !sortedStationOrder.contains(c.destination)) {
                    current = c.destination;
                    sortedConnectionOrder.add(c);
                    break;
                } else if (c.destination == current && !sortedStationOrder.contains(c.origin)) {
                    current = c.origin;
                    sortedConnectionOrder.add(c);
                    break;
                }
            }
        }

        sortedStationOrder.add(current);
        stations = sortedStationOrder;
        connections = sortedConnectionOrder;
    }

    /*
     * for use in PIA
     */
    // inserts a station in a position on the line
    public void insertStation(Station s, int i) {
        stations.add(i, s);
        if (i == 0) {
            connections.add(i, new Connection(s, stations.get(1), s.getDistance(stations.get(1))));
        } else if (i == stations.size() - 1) {
            connections.add(i - 1, new Connection(stations.get(i - 1), s, s.getDistance(stations.get(i - 1))));
        } else {
            connections.remove(i - 1);
            connections.add(i - 1, new Connection(stations.get(i - 1), s, s.getDistance(stations.get(i - 1))));
            connections.add(i, new Connection(s, stations.get(i + 1), s.getDistance(stations.get(i + 1))));
        }
    }

    public boolean hasSubpath(Path p) {
        if (p.stations.size() > stations.size()) {
            return false;
        }
        for (int i = 0; i < stations.size() - p.stations.size(); i++) {
            if (stations.get(i).equals(p.origin)) {
                for (int j = 0; j < p.stations.size(); j++) {
                    if (!stations.get(i + j).equals(p.stations.get(j))) {
                        return false;
                    }
                }
                return true;
            }
 
        }
   
        return false;
    }

    public Station getLastStation() {
        return stations.get(stations.size() - 1);
    }

    public static void main(String[] args) {
        WMATA WMATA = new WMATA();

        PathPlanning pp = new LeastTransfers(WMATA.WMATA);
        Path path = pp.pathPlan(WMATA.WMATA.getStation("arlington cemetery"), WMATA.WMATA.getStation("downtown largo"));
        System.out.println(path);

        Path subPath = pp.pathPlan(WMATA.WMATA.getStation("farragut west"), WMATA.WMATA.getStation("eastern market"));
        Path falseSubPath = pp.pathPlan(WMATA.WMATA.getStation("farragut west"), WMATA.WMATA.getStation("shady grove"));
        System.out.println(path.hasSubpath(subPath));
        System.out.println(path.hasSubpath(falseSubPath));
    }
}
