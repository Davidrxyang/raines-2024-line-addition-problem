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

    // constructor
    // main algorithm logic
    public PIA(DemandSet demand, Network network) {
        this.l = demand;
        this.existingNetwork = network;
        this.R = new Network("new", existingNetwork.stationList);
        R.connections = existingNetwork.connections;
    }


    // candidate function
    // chooses the most suitable line in the network to add the two stations from demand
    // and returns modified line
    public Line candidate(Demand d) {
        return null;
    }

    // cost of a line within a network
    public double cost(Line l) {
        return 0;
    }

    // updates D0 and D01
    public void updateD0() {

    }

}
