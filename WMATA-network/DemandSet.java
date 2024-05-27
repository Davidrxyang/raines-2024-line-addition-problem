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
        // origin-destination matrix

        // demandMatrix = new int[network.stations.size()][network.stations.size()];
        // int i = 0;
        // ArrayList<Station> stationList = new ArrayList<Station>(network.stations.values());
        // for (Station station : stationList) {
        //     stationNumber.put(station, i);
        //     i++;
        // }

        // load the trips from the file
        try {
            File file = new File(filename);
            Scanner sc = new Scanner(file);
            sc.nextLine();
            while (sc.hasNextLine()) {
                String[] line = sc.nextLine().split(",");
                String start = line[2].substring(1, line[2].length() - 1);
                String end = line[3].substring(1, line[3].length() - 1);
                int trips = Integer.parseInt(line[6].substring(1, line[6].length() - 1));

                // Station startStation = network.stations.get(start);
                // Station endStation = network.stations.get(end);

                System.out.println(start);
                System.out.println(end);
                System.out.println(trips);
            }

            sc.close();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {

        // sample usage
        Network network = new Network();
        DemandSet demandSet = new DemandSet();
        demandSet.loadTrips("WMATA-network/data.csv", network);
    }
}
