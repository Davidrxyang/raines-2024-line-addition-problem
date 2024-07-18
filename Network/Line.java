/*
 * a collection of connections that together represent one transit line 
 * 
 * each line is DIRECTED, since lines in reality go in both directions, 
 * there will be two line instantiations for each line, one for each 
 * direction
 */
package Network;

import java.util.ArrayList;
import java.util.Collections;

import Network.Station;
import Network.Connection;


public class Line {
    public String name;
    public Station origin;
    public Station destination; // this is the direction of the line
    public Double length;
    public int index; // self aware indexing for pathplanning matrix

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

    /*
     * default constructor defined for overloading
     */

    public Line() {
        this.name = "";
        connections = new ArrayList<>();
        stations = new ArrayList<>();

        origin = null;
        destination = null;
        length = 0.0;
        index = -1;
    }

    public Line(String name) {
        this.name = name;
        connections = new ArrayList<>();
        stations = new ArrayList<>();

        origin = null;
        destination = null;
        length = 0.0;
        index = -1;
    }

    // copy constructor
    // used in existing algorithms to insert stations into lines
    public Line(Line l) {
        this.name = l.name;
        this.connections = new ArrayList<>(l.connections);
        this.stations = new ArrayList<>(l.stations);
        this.origin = l.origin;
        this.destination = l.destination;
        this.length = l.length;
        index = -1;
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
        return connections.toString();
    }

    public boolean equals(Line line) {
        return this.name.equals(line.name);
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
        addStationWithoutAddingLine(station, distance);

        boolean hasLine = false;
        for (Line l : station.lines) {
            if (l.name.equals(this.name)) {
                hasLine = true;
                break;
            }
        }
        if (!hasLine)
            station.addLine(this);
    }

    public void addStationWithoutAddingLine(Station station, Double distance) {
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

    public void addStationUnordered(Station station) {
        if (!stations.contains(station)) {
            stations.add(station);
        }
    }

    /*
     * METHOD 2
     * 
     * directly add connections to the line, separately specify origin and
     * destination
     */

    public void addConnection(Connection connection) {
        connections.add(connection);
        connection.origin.addLine(this);
        connection.destination.addLine(this);
    }

    public void addConnection(ArrayList<Connection> connections) {
        connections.addAll(connections);
        for (Connection connection : connections) {
            connection.origin.addLine(this);
            connection.destination.addLine(this);
        }
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

    public Station getNextStation(Station station) {
        for (Connection connection : connections) {
            if (connection.origin.equals(station)) {
                return connection.destination;
            }
        }
        return null;
    }

    
    public Station getPreviousStation(Station station) {
        for (Connection connection : connections) {
            if (connection.destination.equals(station)) {
                return connection.origin;
            }
        }
        return null;
    }
    
    /*
     * since each line is directed, this function automatically generates the reverse direction lin
     */
    public Line generateReverseDirection(String name) {
        Line reverseLine = new Line(name);
        reverseLine.origin = this.destination;
        reverseLine.destination = this.origin;
        reverseLine.length = this.length;
        reverseLine.stations.addAll(this.stations);

        for (Connection connection : this.connections) {
            Connection reverseConnection = new Connection(connection.destination, connection.origin, connection.distance);
            reverseLine.addConnection(reverseConnection);
        }
        
        reverseLine.sort();
        return reverseLine;
    }

    /*
     * reverses the current line, new name - modification of above function
     */

    public void reverse(String name) {
        Line reverseLine = new Line(name);
        reverseLine.origin = this.destination;
        reverseLine.destination = this.origin;
        reverseLine.length = this.length;
        reverseLine.stations.addAll(this.stations);

        for (Connection connection : this.connections) {
            Connection reverseConnection = new Connection(connection.destination, connection.origin, connection.distance);
            reverseLine.addConnection(reverseConnection);
        }
        
        reverseLine.sort();

        this.name = reverseLine.name;
        this.origin = reverseLine.origin;
        this.destination = reverseLine.destination;
        this.length = reverseLine.length;
        this.index = reverseLine.index;
        this.connections = reverseLine.connections;
        this.stations = reverseLine.stations;

        // add the reversed line to the line array in each station
        for (Station station : stations) {
            // THIS IS JANKY: 
            // somewhere the reverse line is getting added
            // twice to the station's lines array, 
            // the fix is to remove the line and re-add it

            for (Line line : station.lines) {
                if (line.name.equals(this.name)) {
                    station.lines.remove(line);
                    break;
                }
            }   

            station.addLine(this);
        }
    }

    /*
     * CR function in path planning - returns lines that connect the two non-connected lines
     * 
     * this is terrible, I know
     */
    public ArrayList<Line> commonLines(Network network, Line targetLine) {
        ArrayList<Line> commonLines = new ArrayList<>();

        for (Station station : this.stations) {
            for (Line line : station.lines) {
                for (Station transferStation : line.stations) {
                    if (targetLine.stations.contains(transferStation)) {
                        if (!commonLines.contains(line)) {
                            commonLines.add(line);
                        }
                    }
                }
            }
        }
        return commonLines;
    }
    
    /*
     * all lines that the current line can transfer to
     */
    public ArrayList<Line> transferLines(Network network) {
        ArrayList<Line> transferLines = new ArrayList<>();
        for (Station station : this.stations) {
            for (Line line : station.lines) {
                if (!transferLines.contains(line) && !line.equals(this)) {
                    transferLines.add(line);
                }
            }
        }
        return transferLines;
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
                if (c.origin == current && !sortedConnectionOrder.contains(c)) {
                    current = c.destination;
                    sortedConnectionOrder.add(c);
                    break;
                } else if (c.destination == current && !sortedConnectionOrder.contains(c)) {
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

    // testing function, no actual use
    // shuffles the station arraylist
    private void shuffle() {
        Collections.shuffle(stations);
    }

    /*
     * for use in PIA
     */
    // inserts a station in a position on the line
    public void insertStation(Station s, int i) {
        stations.add(i, s);
        if (stations.size() == 1) {
            origin = s;
            destination = s;
        } else if (i == 0) {
            connections.add(i, new Connection(s, stations.get(1), s.getDistance(stations.get(1))));
            origin = s;
        } else if (i == stations.size() - 1) {
            connections.add(i - 1, new Connection(stations.get(i - 1), s, s.getDistance(stations.get(i - 1))));
            destination = s;
        } else {
            connections.remove(i - 1);
            connections.add(i - 1, new Connection(stations.get(i - 1), s, s.getDistance(stations.get(i - 1))));
            connections.add(i, new Connection(s, stations.get(i + 1), s.getDistance(stations.get(i + 1))));
        }
    }

    /*
     * for use in PIA
     * inserts a line into a position on the line
     */
    public void insertLine(Line l, int i) {
        if (l != null) {
            for (int k = i; k < l.stations.size() + i; k++) {
                insertStation(l.stations.get(k - i), k);
            }
        }
    }

    // cost of travelling down a line
    public double travelCost(Station start, Station end) {
        if (!stations.contains(start) && !stations.contains(end)) {
            return -1;
        }
        double cost = 0;
        sort();
        for (int i = 0; i < stations.size(); i++) {
            if (stations.get(i) == start) {
                for (int j = i; j < stations.size(); j++) {
                    if (stations.get(j) == end) {
                        for (int k = i; k < j; k++) {
                            cost += connections.get(k).distance;
                        }
                        return cost;
                    }
                }
            } else if (stations.get(i) == end) {
                for (int j = i; j < stations.size(); j++) {
                    if (stations.get(j) == start) {
                        for (int k = i; k < j; k++) {
                            cost += connections.get(k).distance;
                        }
                        return cost;
                    }
                }
            }
        }
        return cost;
    }

    public static void main(String[] args) {
        // ArrayList<Station> WMATAStations = new ArrayList<>();

        Station rosslyn = new Station("rosslyn", 38.8969, -77.0720);
        Station foggy_bottom = new Station("foggy bottom", 38.9009, -77.0505);
        Station farragut_west = new Station("farragut west", 38.9016, -77.0420);
        Station mcpherson_square = new Station("mcpherson square", 38.9013, -77.0322);
        Station metro_center = new Station("metro center", 38.8987, -77.0278);
        Station federal_triangle = new Station("federal triangle", 38.8940, -77.0283);
        Station smithsonian = new Station("smithsonian", 38.8892, -77.0282);
        Station lenfant_plaza = new Station("lenfant plaza", 38.8851, -77.0219);
        Station federal_center_sw = new Station("federal center sw", 38.8852, -77.0156);
        Station capitol_south = new Station("capitol south", 38.8858, -77.0060);
        Station eastern_market = new Station("eastern market", 38.8844, -76.9958);

        Station test_station = new Station("test station", 38.8840, -76.99);

        Line blue_line = new Line("blue line");

        blue_line.addStation(rosslyn, null);
        blue_line.addStation(foggy_bottom, 1.3);
        blue_line.addStation(farragut_west, 0.5);
        blue_line.addStation(mcpherson_square, 0.4);
        blue_line.addStation(metro_center, 0.45);
        blue_line.addStation(federal_triangle, 0.3);
        blue_line.addStation(smithsonian, 0.38);
        blue_line.addStation(lenfant_plaza, 0.54);
        blue_line.addStation(federal_center_sw, 0.38);
        blue_line.addStation(capitol_south, 0.58);
        blue_line.addStation(eastern_market, 0.52);

        blue_line.setDestination(eastern_market);

        blue_line.shuffle();

        System.out.println(blue_line.getLength());
        System.out.println(blue_line);
        System.out.println(blue_line.stations);

        blue_line.sort();
        blue_line.insertStation(test_station, 1);
        
        System.out.println(blue_line.getLength());
        System.out.println(blue_line);
        System.out.println(blue_line.stations);

        // WMATAStations.addAll(blue_line.stations);

        // Network WMATA = new Network("WMATA", WMATAStations);

        // WMATA.addLine(blue_line);

        // System.out.println(WMATA);
    }
}
