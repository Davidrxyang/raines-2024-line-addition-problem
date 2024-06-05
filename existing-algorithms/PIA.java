/*
 * PIA algorithm implementation
 * since PIA operates on an undirected graph,
 * Network and Station is treated as undirected
 */

public class PIA {
    // the demand of the network
    public DemandSet l;

    // the initial network containing station information
    // and connectivity information
    public Network existingNetwork;

    // the network to be returned with transit lines
    public Network R;

    // the percentage of demand covered by the network
    // D0: ratio of demand covered by network without transfers
    // D01: ratio of demand covered by network with one transfer
    public double D0 = 0;
    public double D01 = 0;

    // parameters to determine termination of the algorithm
    // when the ratio of the demand covered by the network is higher than the minimums
    public double D0min = 0.75;
    public double D01min = 1.0;
    public double totalTrips;

    // constructor
    // main algorithm logic
    public PIA(DemandSet demand, Network network) {
        this.l = demand;
        this.existingNetwork = network;
        this.R = new Network("new", existingNetwork.stationList);
        R.connections = existingNetwork.connections;
        totalTrips = demand.totalTrips();
    }


    // candidate function
    // chooses the most suitable line in the network to add the two stations from demand
    // and returns modified line
    // returns in format [r', r'']
    // where r' is the route with the new stations added, r'' is the original route
    public Line[] candidate(Demand d) {
        return null;
    }

    // cost of a line within a network
    public double cost(Line l) {
        return 0;
    }

    // updates D0 and D01
    public void updateD0() {
        int oneTransfer = 0;
        int moreTransfers = 0;
        for (Demand d : l.trips) {
            for (Line r1 : R.lines) {
                for (Line r2: R.lines) {
                    // work in progress
                }
            }
        }
    }

    // deletes vertices in OD matrix that have demand covered entirely by one route
    public void deleteVertices() {
        for (Line line : R.lines) {
            for (int i = 0; i < l.trips.size(); i++) {
                Demand t = l.trips.get(i);
                // if both start and end of a demand (trip) is on the same line,
                // remove that from the demand matrix (demand set)
                if (line.stations.contains(t.start) && line.stations.contains(t.end)) {
                    l.trips.remove(t);
                    i--;
                }
            }
        }
    }

}
