// evaluates the cost and efficiency of a network, see README.md for details

package NetworkEvaluation;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;

import Network.Line;
import Network.Path;
import Network.Network;
import Network.DemandSet;
import Network.Demand;
import Network.WMATA;
import ExistingAlgorithms.PathPlanning;

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
        Double costMode = 0.0;

        switch(config.get("cost-mode")) {
            case "global-average":
                costMode = averageCost();
                break;

            case "state-average-DC":
                costMode = stateAverage("DC");
                break;

            // add other cost calculation schemes here

            default:
                System.out.println("Invalid cost mode, defaulting to global average");
                costMode = averageCost();
                break;
        }

        cost = costMode * line.getLength();

        return cost;
    }

    /*
     * calculates the construction cost of a path
     * 
     * returns total cost in millions of dollars
     */

     public Double constructionCost(Path path) {

        Double cost = 0.0;
        Double costMode = 0.0;

        switch(config.get("cost-mode")) {
            case "global-average":
                costMode = averageCost();
                break;

            case "state-average-DC":
                costMode = stateAverage("DC");
                break;

            // add other cost calculation schemes here

            default:
                System.out.println("Invalid cost mode, defaulting to global average");
                costMode = averageCost();
                break;
        }

        cost = costMode * path.getLength();

        return cost;
    }

    /*
     * calculates the efficiency of a route based on weights specified by
     * config parameters
     * 
     * units are arbitrary 
     */

    public Double routeEfficiency(Path path) {

        // default values
        Double transferWeight = 1.0;
        Double stationWeight = 1.0;
        Double distanceWeight = 1.0;

        if (config.get("transfer-weight") != null) {
            transferWeight = 1 + Double.parseDouble(config.get("transfer-weight"));
        }

        if (config.get("station-weight") != null) {
            stationWeight = 1 + Double.parseDouble(config.get("station-weight"));
        }

        if (config.get("distance-weight") != null) {
            distanceWeight = 1 + Double.parseDouble(config.get("distance-weight"));
        }

        return (transferWeight * path.nTransfers + stationWeight * path.stations.size() + distanceWeight * path.getLength());
    }

    /*
     * calculates the efficiency of a route adjusted for direct distance
     * 
     * units are arbitrary 
     */

    public Double adjustedRouteEfficiency(Path path) {
        Double simpleEfficiency = routeEfficiency(path);
        Double geographicalDistance = path.origin.getDistance(path.destination);
        Double adjustmentWeight = 0.5;

        if (config.get("adjustment-weight") != null) {
            adjustmentWeight = Double.parseDouble(config.get("adjustment-weight"));
        }

        return 100 * ((simpleEfficiency) / ((1 + adjustmentWeight) * geographicalDistance));
    }

    /*
     * calculates the efficiency of a network based on route demand data
     */

    public Double networkEfficiency(Network network, DemandSet demandSet) {

        Double networkEfficiency = 0.0;
        PathPlanning pp = new PathPlanning(network);
        Double tripsEfficiency = 0.0;
        String evaluationMode = "standard"; // default evaluation mode

        if (config.get("eval-mode") != null) {
            evaluationMode = config.get("eval-mode");
        }

        // for each trip in the demand set, calculate the efficiency of that route/trip
        // and weigh it based on the "trip" demand.

        // if the trip is not contained within the demandSet, then the demand is 0,
        // so it is fine to ignore it within efficiency calculations based on our
        // definition of efficiency

        for (Demand d : demandSet.trips) {
            Path path = pp.pathPlan(d.start, d.end);
            Double efficiency = 0.0;
            
            switch (evaluationMode) {
                case "standard":
                    efficiency = routeEfficiency(path);
                    break;

                case "adjusted":
                    efficiency = adjustedRouteEfficiency(path);
                    break;

                default:
                    System.out.println("Invalid evaluation mode, defaulting to standard");
                    tripsEfficiency += d.trips * routeEfficiency(path);
                    break;
            }   

            System.out.println(tripsEfficiency);
            tripsEfficiency += (double)d.trips * efficiency;
        }

        // calculate the total construction cost of the network, per line

        double totalCost = 0.0;
        for (Line line : network.lines) {
            totalCost += constructionCost(line);
        }

        // calculate the efficiency of the network as a whole based on regression mode

        switch (config.get("regression")) {
            case "linear-linear":
                networkEfficiency = tripsEfficiency / totalCost;
                break;

            case "log-linear":
                networkEfficiency = Math.log(tripsEfficiency) / totalCost;
                break;

            case "linear-log":
                networkEfficiency = tripsEfficiency / Math.log(totalCost);
                break;

            case "log-log":
                networkEfficiency = Math.log(tripsEfficiency) / Math.log(totalCost);
                break;

            default:
                System.out.println("Invalid regression mode, defaulting to linear-linear");
                networkEfficiency = tripsEfficiency / totalCost;
                break;
        }

        return networkEfficiency;
    }

    /*
     * calculates the percentage change in efficiency between two networks
     */

    public Double compareNetworks(Network oldNetwork, Network newNetwork, DemandSet demandSet) {
        Double oldEfficiency = networkEfficiency(oldNetwork, demandSet);
        Double newEfficiency = networkEfficiency(newNetwork, demandSet);

        return 100.0 * ((newEfficiency - oldEfficiency) / oldEfficiency);
    }

    /*
     * simple math helper function to calculate percent change
     */
    public Double calculatePercentChange(Double oldEfficiency, Double newEfficiency) {
        return 100.0 * ((newEfficiency - oldEfficiency) / oldEfficiency);
    }
    
    // averages construction costs
    public Double averageCost() {
        Double totalCost = 0.0;
        for (Double cost : costData.costs) {
            totalCost += cost;
        }
        return (totalCost / costData.costs.size());
    }

    // averages construction costs by state
    public Double stateAverage(String state) {
        Double totalCost = 0.0;
        int count = 0;

        // iterate through states and find corresponding costs
        for (int i = 0; i < costData.states.size(); i++) {
            if (costData.states.get(i).equals(state)) {
                totalCost += costData.costs.get(i);
                count++;
            }
        }
 
        return (totalCost / count);
    }

    public static void main(String[] args) {
        Evaluation eval = new Evaluation("NetworkEvaluation/config");
        WMATA WMATA = new WMATA();
        DemandSet demandSet = new DemandSet();
        DemandSet protectedDemandSet = new DemandSet();
        demandSet.loadTrips("Network/data.csv", WMATA.WMATA);
        protectedDemandSet.loadTrips("Network/data.csv", WMATA.WMATA);

        


    }
}