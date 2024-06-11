/*
 * RGA implementation
 */

public class RGA {
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
    public double D01min = 1;
    public double totalTrips;

    // number of initial skeletons
    public int M;

    // current line number
    public int lineNumber;
    
    public RGA(int M) {

    }

    // removes all the routes from network R
    // that are a subset of another route
    public void removeSubsetRoutes() {
        
    }

    // generate the initial skeleton network
    // for each of the M node pairs with highest demand,
    // generate a line between them by finding the shortest path within network
    public void generateInitialSkeleton() {
        for (int i = R.lines.size(); i < M; i++) {
            Demand d = l.getMaxDemand();
            Station start = d.start;
            Station end = d.end;
            Line l = existingNetwork.bfs(start, end, "r" + lineNumber);

            lineNumber++;
            R.addLine(l);
        }
    }
}
