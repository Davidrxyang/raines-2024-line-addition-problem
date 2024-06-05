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

    // constructor
    // main algorithm logic
    public PIA(DemandSet demand, Network network) {
        this.l = demand;
        this.existingNetwork = network;
        this.R = new Network("new", existingNetwork.stationList);
        R.connections = existingNetwork.connections;
        totalTrips = demand.totalTrips();
    }

    public String toString() {
        return R.toString();
    }


    // candidate function
    // chooses the most suitable line in the network to add the two stations from demand
    // and returns modified line
    // returns in format [r', r'']
    // where r' is the route with the new stations added, r'' is the original route
    public Line[] candidate(Demand d) {
        Line rPrime = null;
        Line rDoublePrime = null;
        double rCost = Double.MAX_VALUE;
        for (Line r : R.lines) {
            if (r.stations.contains(d.start)) {
                for (int i = 0; i < r.stations.size() + 1; i++) {
                    Line rPrimeTemp = new Line(r);
                    rPrimeTemp.insertStation(d.end, i);
                    if (cost(rPrimeTemp) < rCost) {
                        rPrime = rPrimeTemp;
                        rCost = cost(rPrimeTemp);
                        rDoublePrime = r;
                    }
                }
                
            }
            else if (r.stations.contains(d.end)) {
                for (int i = 0; i < r.stations.size() + 1; i++) {
                    Line rPrimeTemp = new Line(r);
                    rPrimeTemp.insertStation(d.start, i);
                    if (cost(rPrimeTemp) < rCost) {
                        rPrime = rPrimeTemp;
                        rCost = cost(rPrimeTemp);
                        rDoublePrime = r;
                    }
                }
            }
            else {
                for (int i = 0; i < r.stations.size() + 1; i++) {
                    for (int j = 0; j < r.stations.size() + 2; j++) {
                        Line rPrimeTemp = new Line(r);
                        rPrimeTemp.insertStation(d.start, i);
                        rPrimeTemp.insertStation(d.end, j);
                        if (cost(rPrimeTemp) < rCost) {
                            rPrime = rPrimeTemp;
                            rCost = cost(rPrimeTemp);
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


    public static void main(String[] args) {
        // dummy test data
        Station test1 = new Station("1", 0.0, 0.0);
        Station test2 = new Station("2", 0.0, 0.1);
        Station test3 = new Station("3", 0.0, 0.2);
        Station test4 = new Station("4", 0.1, 0.0);
        Station test5 = new Station("5", 0.1, 0.1);
        Station test6 = new Station("6", 0.1, 0.2);
        Station test7 = new Station("7", 0.2, 0.0);
        Station test8 = new Station("8", 0.2, 0.1);
        Station test9 = new Station("9", 0.2, 0.2);

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
        demand.trips.add(new Demand(test4, test5, random.nextInt(20)));
        demand.trips.add(new Demand(test4, test6, random.nextInt(20)));
        demand.trips.add(new Demand(test4, test7, random.nextInt(20)));

        PIA pia = new PIA(demand, network);
        System.out.println(pia);
        Line testLine = new Line("test line");
        testLine.addStation(test1, 0.0);
        testLine.addStation(test2, test2.getDistance(test1));
        pia.R.lines.add(testLine);

        pia.deleteVertices();
        pia.updateD0();

        System.out.println(pia.candidate(testDemand)[0].stations);
        System.out.println(pia.R.lines);

        System.out.println(pia.D0);
        System.out.println(pia.D01);
    }
}
