/*
 * the set of trips
 */
package Network; 

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class DemandSet {
    public ArrayList<Demand> trips;
    // tracks which station is which index in the origin-destination matrix
    // public HashMap<Station, Integer> stationNumber;
    // public int[][] demandMatrix;

    public DemandSet() {
        trips = new ArrayList<Demand>();
    }

    public DemandSet(DemandSet d) {
        trips = new ArrayList<Demand>();
        for (Demand trip : d.trips) {
            trips.add(new Demand(trip));
        }
    }

    // loads trip information given a network and a data file
    // currently written for WMATA PlanItMetro data set
    // https://planitmetro.com/2016/03/14/metrorail-ridership-data-download-october-2015/
    public void loadTrips(String filename, Network network) {
        // load station information and number the stations
        // stationNumber = new HashMap<Station, Integer>();

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
        // network.stations = new HashMap<>();

        // load the trips from the file
        try {
            File file = new File(filename);
            Scanner sc = new Scanner(file);
            sc.nextLine();
            while (sc.hasNextLine()) {
                String[] line = sc.nextLine().split(",");
                if (line[0].substring(1, line[0].length() - 1).equals("Weekday")
                && line[4].substring(1, line[4].length() - 1).equals("AM Peak")) {
                        
                    String start = line[2].substring(1, line[2].length() - 1).toLowerCase();
                    String end = line[3].substring(1, line[3].length() - 1).toLowerCase();

                    Station startStation;
                    Station endStation;

                    // temporary fix to create station information
                    // if (!network.stations.containsKey(start)) {
                    //     network.stations.put(start, new Station(start, 0.0, 0.0));
                    // }
                    // if (!network.stations.containsKey(end)) {
                    //     network.stations.put(end, new Station(end, 0.0, 0.0));
                    // }

                    // System.out.println(start + " -> " + end);
                    startStation = network.stations.get(start);
                    endStation = network.stations.get(end);
                    // System.out.println(startStation + " -> " + endStation);
                    if (startStation == null) {
                        System.out.println("Station not found: " + start);
                        break;
                    }
                    if (endStation == null) {
                        System.out.println("Station not found: " + end);
                        break;
                    }

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
                
            }

            // System.out.println(trips);

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

    public Demand getDemand(Station start, Station end) {
        for (Demand demand : trips) {
            if (demand.start == start && demand.end == end) {
                return demand;
            }
        }
        if (start != end) {
            Demand d = new Demand(start, end, 0);
            trips.add(d);
            return d;
        }
        return null;
    }

    public static void main(String[] args) {
        WMATA wmata = new WMATA();
        // sample usage
        DemandSet demandSet = new DemandSet();
        demandSet.loadTrips("network/data.csv", wmata.WMATA);

        System.out.println(demandSet.totalTrips());
    }
}
