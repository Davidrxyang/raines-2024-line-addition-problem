/*
 * PIA algorithm implementation
 * since PIA operates on an undirected graph,
 * Network and Station is treated as undirected
 */

import java.util.ArrayList;

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
    public double D0min = 0.65;
    public double D01min = 1;
    public double totalTrips;

    // the number of lines in the network
    public int lineNumber;

    // constraints for candidate
    public double maxCircuity = 2;

    // constructor
    // main algorithm logic
    public PIA(DemandSet demand, Network network) {
        this.l = demand;
        this.existingNetwork = network;
        this.R = new Network("new", existingNetwork.stationList);
        R.connections = existingNetwork.connections;
        totalTrips = demand.totalTrips();
        lineNumber = 1;

        while ((D0 < D0min || D01 < D01min) && l.trips.size() > 0){
            Demand d = l.getMaxDemand();
            Line r = network.bfs(d.start, d.end, "r" + lineNumber);
            r.insertStation(d.start, 0);
            r.insertStation(d.end, r.stations.size());
            

            Line[] candidateLines = null;
            Line rPrime = null;
            Line rDoublePrime = null;

            //System.out.println("r");
            //System.out.println(r);
            if (R.lines.size() > 0) {
                candidateLines = candidate(d, r.travelCost(d.start, d.end));
                rPrime = candidateLines[0];
                rDoublePrime = candidateLines[1];
                //System.out.println("r'");
                //System.out.println(rPrime);
            }

            if (R.lines.size() == 0 || rPrime == null ){
                R.lines.add(r);
                
                lineNumber++;
            } else if (cost(r) < (cost(rPrime) - cost(rDoublePrime))) {
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
            removeSubsetRoutes();
        }

        ArrayList<Line> lines = new ArrayList<>(R.lines);
        R.lines.clear();
        for (Line l : lines) {
            if (l.stations.size() > 1) {
                R.addLine(l);


                // david's edits to add reverse lines
                Line reverseLine = new Line(l);
                StringBuilder sb = new StringBuilder();
                sb.append(reverseLine.name).append(" reverse");
                reverseLine.reverse(sb.toString());
                R.addLine(reverseLine);
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
        for (Demand d : l.trips) {
            for (Line r1 : R.lines) {
                boolean found = false;
                for (Line r2: R.lines) {
                    if (r1.commonStations(r2).size() > 0) {
                        if (r1.stations.contains(d.start) && r2.stations.contains(d.end)
                        || r1.stations.contains(d.end) && r2.stations.contains(d.start)) {
                            oneTransfer += d.trips;
                            found = true;
                            break;
                        }
                    }
                }
                if (found) {
                    break;
                }
            }
        }

        int noTransfers = (int) totalTrips - l.totalTrips();
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

    // removes all the routes from network R
    // that are a subset of another route
    public void removeSubsetRoutes() {
        ArrayList<Line> toRemove = new ArrayList<Line>();
        for (Line l1 : R.lines) {
            for (Line l2 : R.lines) {
                if (l1 != l2 && l1.stations.containsAll(l2.stations)) {
                    toRemove.add(l2);
                }
            }
        }
        for (Line l : toRemove) {
            R.lines.remove(l);
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
        WMATA wmata = new WMATA();
        DemandSet demandSet = new DemandSet();
        demandSet.loadTrips("network/data.csv", wmata.WMATA);

        /*
         * currently this implementation does not update matrix info
         * in network
         */
        PIA pia = new PIA(demandSet, wmata.WMATA);
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

        System.out.println("Number of lines: " + pia.R.lines.size());

        System.out.println("D0: " + pia.D0);
        System.out.println("D01: " + pia.D01);

        // System.out.println(pia.D0);
        // System.out.println(pia.D01);
    }
}
