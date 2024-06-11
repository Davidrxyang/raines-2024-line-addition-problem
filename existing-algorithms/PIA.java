/*
 * PIA algorithm implementation
 * since PIA operates on an undirected graph,
 * Network and Station is treated as undirected
 */

import java.util.ArrayList;
import java.util.Random;

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
    public double D01min = 1;
    public double totalTrips;

    // the number of lines in the network
    public int lineNumber;

    // constraints for candidate
    public double maxCircuity = 1.5;

    // constructor
    // main algorithm logic
    public PIA(DemandSet demand, Network network) {
        this.l = demand;
        this.existingNetwork = network;
        this.R = new Network("new", existingNetwork.stationList);
        R.connections = existingNetwork.connections;
        totalTrips = demand.totalTrips();
        lineNumber = 1;

        while (D0 < D0min || D01 < D01min) {
            Demand d = l.getMaxDemand();
            Line r = network.bfs(d.start, d.end, "r" + lineNumber);
            r.insertStation(d.start, 0);
            r.insertStation(d.end, r.stations.size());
            

            Line[] candidateLines = null;
            Line rPrime = null;
            Line rDoublePrime = null;

            System.out.println("r");
            System.out.println(r);
            if (R.lines.size() > 0) {
                candidateLines = candidate(d, r.travelCost(d.start, d.end));
                rPrime = candidateLines[0];
                rDoublePrime = candidateLines[1];
                System.out.println("r'");
                System.out.println(rPrime);
            }

            if (R.lines.size() == 0 || rPrime == null ){
                R.lines.add(r);
                
                lineNumber++;
            } else if (cost(r) < cost(rPrime) - cost(rDoublePrime)) {
                R.lines.add(r);
                lineNumber++;
            } else {
                R.lines.remove(rDoublePrime);
                R.lines.add(rPrime);

                d.start.removeLine(r);
                d.start.removeLine(rDoublePrime);
                d.end.removeLine(r);
                d.end.removeLine(rDoublePrime);

                d.start.addLine(rPrime);
                d.end.addLine(rPrime);
            }
            updateD0();
        }

        ArrayList<Line> lines = new ArrayList<>(R.lines);
        R.lines.clear();;
        for (Line l : lines) {
            if (l.stations.size() > 1) {
                R.addLine(l);
            }
        }
    }

    public String toString() {
        return R.toString();
    }


    // candidate function
    // chooses the most suitable line in the network to add the two stations from demand
    // and returns modified line
    // returns in format [r', r'']
    // where r' is the route with the new stations added, r'' is the original route
    public Line[] candidate(Demand d, double directCost) {

        Line rPrime = null;
        Line rDoublePrime = null;
        double rCost = Double.MAX_VALUE;
        for (Line r : R.lines) {
            if (r.stations.contains(d.start)) {
                for (int i = 0; i < r.stations.size() + 1; i++) {
                    Line rPrimeTemp = new Line(r);

                    // find shortest path in graph
                    // when inserting a station into a line at a certain position
                    // we need to find the shortest path from the station before to the station,
                    // and from the station to the station after
                    // and make that the new line
                    Line bfs1 = null;
                    if (i > 0) {
                        bfs1 = existingNetwork.bfs(r.stations.get(i-1), d.end, r.name);
                    }
                    Line bfs2 = null;
                    if (i < r.stations.size()) {
                        bfs2 = existingNetwork.bfs(d.end, r.stations.get(i), r.name);
                    }

                    if (i <= 0) {
                        rPrimeTemp.insertStation(d.end, 0);
                        rPrimeTemp.insertLine(bfs2, i + 1);
                    } else if (i >= r.stations.size()) {
                        rPrimeTemp.insertLine(bfs1, i);
                        rPrimeTemp.insertStation(d.end, rPrimeTemp.stations.size());
                    } else {
                        rPrimeTemp.insertStation(d.end, i);
                        rPrimeTemp.insertLine(bfs2, i+1);
                        rPrimeTemp.insertLine(bfs1, i);
                    }

                    Line n = existingNetwork.bfs(rPrimeTemp.origin, rPrimeTemp.destination, r.name);
                    n.insertStation(rPrimeTemp.origin, 0);
                    n.insertStation(rPrimeTemp.destination, n.stations.size());
                    if (cost(rPrimeTemp) < rCost && noLoop(rPrimeTemp)
                    && rPrimeTemp.travelCost(rPrimeTemp.origin, rPrimeTemp.destination)
                        < maxCircuity * n.travelCost(rPrimeTemp.origin, rPrimeTemp.destination)) {
                        rPrime = rPrimeTemp;
                        rCost = cost(rPrimeTemp);
                        rDoublePrime = r;
                    }
                }
                
            }
            else if (r.stations.contains(d.end)) {
                for (int i = 0; i < r.stations.size() + 1; i++) {
                    Line rPrimeTemp = new Line(r);
                    // find shortest path in graph
                    Line bfs1 = null;
                    if (i > 0) {
                        bfs1 = existingNetwork.bfs(r.stations.get(i-1), d.start, r.name);
                    }
                    Line bfs2 = null;
                    if (i < r.stations.size()) {
                        bfs2 = existingNetwork.bfs(d.start, r.stations.get(i), r.name);
                    }

                    if (i <= 0) {
                        rPrimeTemp.insertStation(d.start, 0);
                        rPrimeTemp.insertLine(bfs2, i + 1);
                    } else if (i >= r.stations.size()) {
                        rPrimeTemp.insertLine(bfs1, i);
                        rPrimeTemp.insertStation(d.start, rPrimeTemp.stations.size());
                    } else {
                        rPrimeTemp.insertStation(d.start, i);
                        rPrimeTemp.insertLine(bfs2, i+1);
                        rPrimeTemp.insertLine(bfs1, i);
                    }

                    Line n = existingNetwork.bfs(rPrimeTemp.origin, rPrimeTemp.destination, r.name);
                    n.insertStation(rPrimeTemp.origin, 0);
                    n.insertStation(rPrimeTemp.destination, n.stations.size());
                    // max circuity bound
                    if (cost(rPrimeTemp) < rCost && noLoop(rPrimeTemp)
                    && rPrimeTemp.travelCost(rPrimeTemp.origin, rPrimeTemp.destination)
                    < maxCircuity * n.travelCost(rPrimeTemp.origin, rPrimeTemp.destination)) {
                        rPrime = rPrimeTemp;
                        rCost = cost(rPrimeTemp);
                        rDoublePrime = r;
                    }
                }
            }
            else {
                for (int i = 0; i < r.stations.size() + 1; i++) {
                    Line rPrimeTemp = new Line(r);
                        
                    // find shortest path in graph
                    // should've been its own function but oh well
                    Line bfs1 = null;
                    if (i > 0) {
                        bfs1 = existingNetwork.bfs(r.stations.get(i-1), d.start, r.name);
                    }
                    Line bfs2 = null;
                    if (i < r.stations.size()) {
                        bfs2 = existingNetwork.bfs(d.start, r.stations.get(i), r.name);
                    }

                    if (i <= 0) {
                        rPrimeTemp.insertStation(d.start, 0);
                        rPrimeTemp.insertLine(bfs2, i +1);
                    } else if (i >= r.stations.size()) {
                        rPrimeTemp.insertLine(bfs1, i);
                        rPrimeTemp.insertStation(d.start, rPrimeTemp.stations.size());
                    } else {
                        rPrimeTemp.insertStation(d.start, i);
                        rPrimeTemp.insertLine(bfs2, i+1);
                        rPrimeTemp.insertLine(bfs1, i);
                    }
                    for (int j = 0; j < rPrimeTemp.stations.size() + 1; j++) {
                        Line rPrimeTemp2 = new Line(rPrimeTemp);

                        // find shortest path in graph
                        if (j > 0) {
                            bfs1 = existingNetwork.bfs(rPrimeTemp.stations.get(j-1), d.end, r.name);
                        }
                        if (j < rPrimeTemp.stations.size()) {
                            bfs2 = existingNetwork.bfs(d.end, rPrimeTemp.stations.get(j), r.name);
                        }

                        if (j <= 0) {
                            rPrimeTemp2.insertStation(d.end, 0);
                            rPrimeTemp2.insertLine(bfs2, j + 1);
                        } else if (j >= rPrimeTemp.stations.size()) {
                            rPrimeTemp2.insertLine(bfs1, j);
                            rPrimeTemp2.insertStation(d.end, rPrimeTemp.stations.size());
                        } else {
                            rPrimeTemp2.insertStation(d.end, j);
                            rPrimeTemp2.insertLine(bfs2, j+1);
                            rPrimeTemp2.insertLine(bfs1, j);
                        }

                        Line n = existingNetwork.bfs(rPrimeTemp2.origin, rPrimeTemp2.destination, r.name);
                        n.insertStation(rPrimeTemp2.origin, 0);
                        n.insertStation(rPrimeTemp2.destination, n.stations.size());
                        if (cost(rPrimeTemp2) < rCost && noLoop(rPrimeTemp2) &&
                        rPrimeTemp2.travelCost(rPrimeTemp2.origin, rPrimeTemp2.destination)
                        < maxCircuity * n.travelCost(rPrimeTemp2.origin, rPrimeTemp2.destination)) {
                            rPrime = rPrimeTemp2;
                            rCost = cost(rPrimeTemp2);
                            rDoublePrime = r;
                        }

                    }
                }
            }
        }
        return new Line[] {rPrime, rDoublePrime};
    }

    // cost of a line within a network
    // since in PIA cost(r) is based on travel time,
    // and travel time is (in theory) proportional to length
    public double cost(Line l) {
        return l.getLength();
    }

    // updates D0 and D01
    public void updateD0() {
        // remove all trips that are covered by one route
        deleteVertices();
        int oneTransfer = 0;
        int moreTransfers = 0;
        for (Demand d : l.trips) {
            for (Line r1 : R.lines) {
                for (Line r2: R.lines) {
                    if (r1.commonStations(r2).size() > 0) {
                        if (r1.stations.contains(d.start) && r2.stations.contains(d.end)) {
                            oneTransfer += d.trips;
                            continue;
                        }
                    }
                }
            }
            moreTransfers += d.trips;
        }

        int noTransfers = (int) totalTrips - oneTransfer - moreTransfers;
        D0 = noTransfers / totalTrips;
        D01 = (noTransfers + oneTransfer) / totalTrips;
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

    // checks if a loop exists in the line
    public boolean noLoop(Line l) {
        // copilot wrote this, not me
        // if i was not lazy i'd write this using a hashmap
        for (int i = 0; i < l.stations.size(); i++) {
            for (int j = 0; j < l.stations.size(); j++) {
                if (i != j && l.stations.get(i).name.equals(l.stations.get(j).name)) {
                    // System.out.println("loop: " + l);
                    return false;
                }
            }
        }
        return true;
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
        PIA pia = new PIA(demand, network);
        System.out.println(pia);
        // Line testLine = new Line("test line");
        // testLine.addStation(test1, 0.0);
        // testLine.addStation(test2, test2.getDistance(test1));
        // pia.R.lines.add(testLine);

        // pia.deleteVertices();
        // pia.updateD0();

        // System.out.println(pia.candidate(testDemand2)[0].stations);
        // System.out.println(pia.R.lines);

        for (Line l : pia.R.lines) {
            System.out.println(l);
        }

        System.out.println("D0: " + pia.D0);
        System.out.println("D01: " + pia.D01);

        // System.out.println(pia.D0);
        // System.out.println(pia.D01);
    }
}
