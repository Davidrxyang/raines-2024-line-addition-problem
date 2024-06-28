// evaluates the cost and efficiency of a network, see README.md for details

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

public class Evaluation {

    CostData costData;
    HashMap<String, String> config;

    // pass in a config file 
    public Evaluation(String filename) {
        costData = new CostData();
        config = new HashMap<>();

        // read in the config file

        try {
            Scanner scanner = new Scanner(new File(filename));
            
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(":");
                config.put(parts[0], parts[1]);
            }
            scanner.close();
        }
        catch (Exception e) {
            System.out.println("Error reading config file");
        }
    }

    /*
     * calculates the construction cost of a line
     * 
     * returns total cost in millions of dollars
     */

    public Double constructionCost(Line line) {

        Double cost = 0.0;

        switch(config.get("costMode")) {
            case "globalAverage":
                cost = (averageCost() * line.getLength());
                break;

            // add other cost calculation schemes here

            default:
                System.out.println("Invalid cost mode");
                break;
        }
        return cost;
    }

    /*
     * calculates the construction cost of a path
     * 
     * returns total cost in millions of dollars
     */

    public Double constructionCost(Path path) {

        Double cost = 0.0;

        switch(config.get("costMode")) {
            case "globalAverage":
                cost = (averageCost() * path.getLength());
                break;

            // add other cost calculation schemes here

            default:
                System.out.println("Invalid cost mode");
                break;
        }
        return cost;
    }

    /*
     * calculates the efficiency of a route based on weights specified by
     * config parameters
     */

    public Double routeEfficiency(Path path) {
        Double transferWeight = Double.parseDouble(config.get("transferWeight"));
        Double stationWeight = Double.parseDouble(config.get("stationWeight"));
        Double distanceWeight = Double.parseDouble(config.get("distanceWeight"));   

        return (transferWeight * path.nTransfers + stationWeight * path.stations.size() + distanceWeight * path.getLength());
    }

    /*
     * calculates the efficiency of a network based on route demand data
     */

    public Double networkEfficiency(Network network, DemandSet demandSet) {

        PathPlanning pp = new PathPlanning(network);
        double tripsEfficiency = 0.0;

        // for each trip in the demand set, calculate the efficiency of that route/trip
        // and weigh it based on the "trip" demand.

        // if the trip is not contained within the demandSet, then the demand is 0,
        // so it is fine to ignore it within efficiency calculations based on our
        // definition of efficiency

        for (Demand d : demandSet.trips) {
            Path path = pp.pathPlan(d.start, d.end);
            double efficiency = routeEfficiency(path); // calculate the efficiency of the route for one user/trip
            tripsEfficiency += efficiency * d.trips; // weigh the efficiency by the number of trips
        }

        // calculate the total construction cost of the network, per line

        double totalCost = 0.0;
        for (Line line : network.lines) {
            totalCost += constructionCost(line);
        }

        // calculate the efficiency of the network as a whole

        double networkEfficiency = tripsEfficiency / totalCost;

        return networkEfficiency;
    }
    
    // averages construction costs
    public Double averageCost() {
        Double totalCost = 0.0;
        for (Double cost : costData.costs) {
            totalCost += cost;
        }
        return (totalCost / costData.costs.size());
    }

    public static void main(String[] args) {
        Evaluation eval = new Evaluation("network-evaluation/config");
        WMATA WMATA = new WMATA();
        DemandSet demandSet = new DemandSet();
        demandSet.loadTrips("network/data.csv", WMATA.WMATA);

        System.out.println(eval.networkEfficiency(WMATA.WMATA, demandSet));

    }
}