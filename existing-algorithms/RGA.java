/*
 * RGA implementation
 */

import java.util.ArrayList;
import java.util.Random;

public class RGA {
    // the demand of the network
    public DemandSet demandSet;

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

    // node sharing factor
    public double fns = 0.8;
    
    public RGA(DemandSet d, Network n, int M) {

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
            Demand d = demandSet.getMaxDemand();
            Station start = d.start;
            Station end = d.end;
            Line l = existingNetwork.bfs(start, end, "r" + lineNumber);
            // necessary for the stupid way i set up bfs
            l.insertStation(start, 0);
            l.insertStation(end, l.stations.size());

            expandLine(l);
            R.addLine(l);
        }
    }

    // expands a line based on best station logic
    public void expandLine(Line l) {
        Station n = pickBestStation(l);
        if (n == null) {
            return;
        }
    }

    // using MD heuristic
    // im interpreting the algorithm as picking from
    // stations adjacent to the current origin and destination of the line
    // and not every possible station in the network
    public Station pickBestStation(Line l) {
        ArrayList<Station> stationCandidates = existingNetwork.getNeightbors(l.origin);
        stationCandidates.addAll(existingNetwork.getNeightbors(l.destination));

        int demandIncrease = 0;

        Station bestStation = null;

        for (Station s : stationCandidates) {
            Line tempLine = new Line(l);
            if (s.getDistance(l.destination) < s.getDistance(l.origin)) {
                tempLine.insertStation(s, tempLine.stations.size());
            } else {
                tempLine.insertStation(s, 0);
            }

            Line direct = existingNetwork.bfs(tempLine.origin, tempLine.destination, "direct");

            // check station constraints (page 37)
            if (l.stations.contains(s)
            || s.calculateDemandSatisfied() > fns
            || tempLine.travelCost(tempLine.origin, tempLine.destination) < direct.travelCost(direct.origin, direct.destination)) {
                continue;
            }

            // calculate increase in demand by putting this station in the line
            int increase = 0;
            for (Demand d : demandSet.trips) {
                if (d.start == s && l.stations.contains(d.end) || d.end == s && l.stations.contains(d.start)) {
                    increase += d.trips;
                }
            }

            if (increase > demandIncrease) {
                demandIncrease = increase;
                bestStation = s;
            }
        }
        return bestStation;
    }


    public static void main(String[] args) {
        // dummy test data
        Station test1 = new Station("1", 0.1, 0.0);
        Station test2 = new Station("2", 0.1, 0.1);
        Station test3 = new Station("3", 0.1, 0.2);
        Station test4 = new Station("4", 0.2, 0.0);
        Station test5 = new Station("5", 0.2, 0.1);
        Station test6 = new Station("6", 0.2, 0.2);
        Station test7 = new Station("7", 0.3, 0.0);
        Station test8 = new Station("8", 0.3, 0.1);
        Station test9 = new Station("9", 0.3, 0.2);

        ArrayList<Station> stationList = new ArrayList<>();
        stationList.add(test1);
        stationList.add(test2);
        stationList.add(test3);
        stationList.add(test4);
        stationList.add(test5);
        stationList.add(test6);
        stationList.add(test7);
        stationList.add(test8);
        stationList.add(test9);

        Network network = new Network("test", stationList);

        network.connections.add(new Connection(test1, test2, test1.getDistance(test2)));
        network.connections.add(new Connection(test2, test3, test1.getDistance(test3)));
        network.connections.add(new Connection(test4, test5, test4.getDistance(test5)));
        network.connections.add(new Connection(test5, test6, test5.getDistance(test6)));
        network.connections.add(new Connection(test7, test8, test7.getDistance(test8)));
        network.connections.add(new Connection(test8, test9, test8.getDistance(test9)));

        network.connections.add(new Connection(test1, test4, test1.getDistance(test4)));
        network.connections.add(new Connection(test2, test5, test2.getDistance(test5)));
        network.connections.add(new Connection(test3, test6, test3.getDistance(test6)));
        network.connections.add(new Connection(test4, test7, test4.getDistance(test6)));
        network.connections.add(new Connection(test5, test8, test5.getDistance(test7)));
        network.connections.add(new Connection(test6, test9, test6.getDistance(test9)));

        network.connections.add(new Connection(test1, test5, test1.getDistance(test5)));
        network.connections.add(new Connection(test3, test5, test3.getDistance(test5)));
        network.connections.add(new Connection(test7, test5, test7.getDistance(test5)));
        network.connections.add(new Connection(test9, test5, test9.getDistance(test5)));

        network.connections.add(new Connection(test2, test6, test2.getDistance(test6)));
        network.connections.add(new Connection(test6, test8, test6.getDistance(test8)));
        network.connections.add(new Connection(test8, test4, test8.getDistance(test4)));
        network.connections.add(new Connection(test4, test2, test4.getDistance(test2)));

        Random random = new Random();

        DemandSet demand = new DemandSet();
        demand.trips.add(new Demand(test1, test2, random.nextInt(20)));
        Demand testDemand = new Demand(test1, test3, random.nextInt(20));
        demand.trips.add(testDemand);
        demand.trips.add(new Demand(test1, test4, random.nextInt(20)));
        demand.trips.add(new Demand(test1, test5, random.nextInt(20)));
        demand.trips.add(new Demand(test1, test6, random.nextInt(20)));
        demand.trips.add(new Demand(test1, test7, random.nextInt(20)));
        demand.trips.add(new Demand(test1, test8, random.nextInt(20)));
        demand.trips.add(new Demand(test1, test9, random.nextInt(20)));
        demand.trips.add(new Demand(test2, test3, random.nextInt(20)));
        demand.trips.add(new Demand(test2, test4, random.nextInt(20)));
        demand.trips.add(new Demand(test2, test5, random.nextInt(20)));
        demand.trips.add(new Demand(test2, test6, random.nextInt(20)));
        demand.trips.add(new Demand(test2, test7, random.nextInt(20)));
        demand.trips.add(new Demand(test2, test8, random.nextInt(20)));
        demand.trips.add(new Demand(test2, test9, random.nextInt(20)));
        demand.trips.add(new Demand(test3, test4, random.nextInt(20)));
        demand.trips.add(new Demand(test3, test5, random.nextInt(20)));
        demand.trips.add(new Demand(test3, test6, random.nextInt(20)));
        demand.trips.add(new Demand(test3, test7, random.nextInt(20)));
        demand.trips.add(new Demand(test3, test8, random.nextInt(20)));
        demand.trips.add(new Demand(test3, test9, random.nextInt(20)));
        Demand testDemand2 = new Demand(test4, test5, random.nextInt(20));
        demand.trips.add(testDemand2);
        demand.trips.add(new Demand(test4, test6, random.nextInt(20)));
        demand.trips.add(new Demand(test4, test7, random.nextInt(20)));

        /*
         * currently this implementation does not update matrix info
         * in network
         */
        RGA rga = new RGA(demand, network, 3);
        System.out.println(rga);
        // Line testLine = new Line("test line");
        // testLine.addStation(test1, 0.0);
        // testLine.addStation(test2, test2.getDistance(test1));
        // pia.R.lines.add(testLine);

        // pia.deleteVertices();
        // pia.updateD0();

        // System.out.println(pia.candidate(testDemand2)[0].stations);
        // System.out.println(pia.R.lines);

        for (Line l : rga.R.lines) {
            System.out.println(l);
        }

        System.out.println("D0: " + rga.D0);
        System.out.println("D01: " + rga.D01);

        // System.out.println(pia.D0);
        // System.out.println(pia.D01);
    }
}
