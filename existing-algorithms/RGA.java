/*
 * RGA implementation
 */

import java.util.ArrayList;

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
    // when the ratio of the demand covered by the network is higher than the
    // minimums
    public double D0min = 0.7;
    public double D01min = 1;
    public double totalTrips;

    // number of initial skeletons
    public int M;

    // current line number
    public int lineNumber;

    // node sharing factor
    public double fns = 1;

    // circuity factor
    public double fc = 1.5;

    public RGA(DemandSet d, Network n, int M) {
        for (Station s : n.stationList) {
            s.calculateDemand(d);
        }
        demandSet = new DemandSet(d);
        existingNetwork = n;
        R = new Network("generated network", existingNetwork.stationList);
        this.M = M;
        lineNumber = 0;

        totalTrips = demandSet.totalTrips();

        do {
            generateInitialSkeleton();
            updateD0();
            removeSubsetRoutes();
            this.M++;
        } while ((D0 < D0min || D01 < D01min) && demandSet.trips.size() > 0);

        // remove existing lines from station
        for (Station s : existingNetwork.stationList) {
            s.lines.clear();
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

                for (Station s : reverseLine.stations) {
                    s.addLine(l);
                    s.addLine(reverseLine);
                }
            }
        }

        // resets connections so unused connections don't mess with pathplanning
        R.connections.clear();
        for (Connection c : R.connectionMap.values()) {
            R.connections.add(c);
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

    // generate the initial skeleton network
    // for each of the M node pairs with highest demand,
    // generate a line between them by finding the shortest path within network
    public void generateInitialSkeleton() {
        ArrayList<Line> skeleton = new ArrayList<Line>();
        for (int i = R.lines.size(); i < M; i++) {
            Demand d = demandSet.getMaxDemand();
            demandSet.trips.remove(d);
            Station start = d.start;
            Station end = d.end;
            start.demandSatisfied += d.trips;
            end.demandSatisfied += d.trips;
            Line l = existingNetwork.bfs(start, end, "r" + lineNumber);
            // necessary for the stupid way i set up bfs
            l.insertStation(start, 0);
            l.insertStation(end, l.stations.size());

            skeleton.add(l);
        }

        for (Line l : skeleton) {
            expandLine(l);
            R.lines.add(l);
            lineNumber++;
        }

    }

    // expands a line based on best station logic
    public void expandLine(Line l) {
        // picks an initial station to expand to
        Station n = null;
        // while the line can still be expanded, expand the line
        // terminate when no suitable lines can be added
        do {
            n = pickBestStation(l);
            if (n != null)
                deleteVertices(n, l);
        } while (n != null); // i love do while loops
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

            Line direct = existingNetwork.bfs(tempLine.origin, tempLine.destination, l.name);
            // because of the stupid way i set up bfs, i have to insert the start and end
            // stations manually
            direct.insertStation(tempLine.origin, 0);
            direct.insertStation(tempLine.destination, direct.stations.size());

            // check station constraints (page 37)
            if (l.stations.contains(s)
                    || s.calculateDemandSatisfied() > fns
                    || tempLine.travelCost(tempLine.origin, tempLine.destination) > fc
                            * direct.travelCost(direct.origin, direct.destination)) {
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

        if (bestStation != null) {
            if (bestStation.getDistance(l.destination) < bestStation.getDistance(l.origin)) {
                l.insertStation(bestStation, l.stations.size());
            } else {
                l.insertStation(bestStation, 0);
            }
        }
        return bestStation;
    }

    // deletes vertices in OD matrix that have demand covered entirely by one route
    public void deleteVertices(Station s, Line line) {
        for (int i = 0; i < demandSet.trips.size(); i++) {
            Demand t = demandSet.trips.get(i);
            // if both start and end of a demand (trip) is on the same line,
            // remove that from the demand matrix (demand set)
            if (line.stations.contains(t.start) && line.stations.contains(t.end)) {
                t.start.demandSatisfied += t.trips;
                t.end.demandSatisfied += t.trips;
                demandSet.trips.remove(t);
                i--;
            }
        }
    }

    public void updateD0() {
        // remove all trips that are covered by one route
        int oneTransfer = 0;
        for (Demand d : demandSet.trips) {
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

        int noTransfers = (int) totalTrips - demandSet.totalTrips();
        D0 = noTransfers / totalTrips;
        D01 = (noTransfers + oneTransfer) / totalTrips;
    }

    public static void main(String[] args) {
        WMATA wmata = new WMATA();
        DemandSet demandSet = new DemandSet();
        demandSet.loadTrips("network/data.csv", wmata.WMATA);

        /*
         * currently this implementation does not update matrix info
         * in network
         */
        RGA rga = new RGA(demandSet, wmata.WMATA, 9);
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
        System.out.println("Number of lines: " + rga.R.lines.size());

        // System.out.println(pia.D0);
        // System.out.println(pia.D01);
    }
}
