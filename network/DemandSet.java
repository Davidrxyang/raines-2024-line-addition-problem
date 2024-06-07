/*
 * the set of trips
 */

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class DemandSet {
    public ArrayList<Demand> trips;
    // tracks which station is which index in the origin-destination matrix
    public HashMap<Station, Integer> stationNumber;
    public int[][] demandMatrix;

    public DemandSet() {
        trips = new ArrayList<Demand>();
    }

    // loads trip information given a network and a data file
    // currently written for WMATA PlanItMetro data set
    // https://planitmetro.com/2016/03/14/metrorail-ridership-data-download-october-2015/
    public void loadTrips(String filename, Network network) {
        // load station information and number the stations
        stationNumber = new HashMap<Station, Integer>();

        // create origin-destination matrix based on network
        // currently not in use, integrate after combining with network
        // demandMatrix = new int[network.stations.size()][network.stations.size()];
        // int i = 0;
        // ArrayList<Station> stationList = new ArrayList<Station>(network.stations.values());
        // for (Station station : stationList) {
        //     stationNumber.put(station, i);
        //     i++;
        // }

        // temporary function to create station information
        network.stations = new HashMap<>();

        // load the trips from the file
        try {
            File file = new File(filename);
            Scanner sc = new Scanner(file);
            sc.nextLine();
            while (sc.hasNextLine()) {
                String[] line = sc.nextLine().split(",");
                String start = line[2].substring(1, line[2].length() - 1);
                String end = line[3].substring(1, line[3].length() - 1);

                Station startStation;
                Station endStation;

                // temporary fix to create station information
                if (!network.stations.containsKey(start)) {
                    network.stations.put(start, new Station(start, 0.0, 0.0));
                }
                if (!network.stations.containsKey(end)) {
                    network.stations.put(end, new Station(end, 0.0, 0.0));
                }

                startStation = network.stations.get(start);
                endStation = network.stations.get(end);

                // number of trips from start to end
                int tripNum = Integer.parseInt(line[6]);

                Demand d;
                boolean found = false;
                for (Demand trip : trips) {
                    if (trip.start == startStation && trip.end == endStation) {
                        d = trip;
                        d.addTrip(tripNum);
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    d = new Demand(startStation, endStation);
                    d.addTrip(tripNum);
                    trips.add(d);
                }
                
            }

            System.out.println(trips);

            sc.close();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public int totalTrips() {
        int tripTotal = 0;
        for (Demand d : trips) {
            tripTotal += d.trips;
        }
        return tripTotal;
    }

    public Demand getMaxDemand() {
        int maxTrips = 0;
        Demand d = null;
        for (Demand trip : trips) {
            if (trip.trips > maxTrips) {
                maxTrips = trip.trips;
                d = trip;
            }
        }
        return d;
    }

    public static void main(String[] args) {
        // sample usage
        Network network = new Network("network", new ArrayList<>());
        DemandSet demandSet = new DemandSet();
        demandSet.loadTrips("network/data.csv", network);

        System.out.println(demandSet.totalTrips());
    }
}
