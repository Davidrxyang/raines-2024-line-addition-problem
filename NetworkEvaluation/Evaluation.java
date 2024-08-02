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
import Network.Station;
import Network.DemandSet;
import Network.Demand;
import Network.WMATA;
import ExistingAlgorithms.PathPlanning;
import ExistingAlgorithms.AStar;
import ExistingAlgorithms.LeastTransfers;

public class Evaluation {

    CostData costData;
    HashMap<String, String> config;
    double evalPower = 4; // power to raise the route complexity to in order to penalize low efficiency routes

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
     * calculates the complexity of a route based on weights specified by
     * config parameters
     * 
     * units are arbitrary 
     */

    public Double routeComplexity(Path path) {

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

        // TODO: updated so that the origin and destination stations are not included in station complexity calculation
        return (transferWeight * path.nTransfers + stationWeight * (path.stations.size() - 2) + distanceWeight * path.getLength());
    }

    /*
     * calculates the complexity of a route adjusted for direct distance
     * 
     * units are arbitrary 
     */

    public Double adjustedRouteComplexity(Path path) {
        Double simpleComplexity = routeComplexity(path);

        Double factor = routeEfficiency(path);

        return factor * simpleComplexity;
    }

    public Double routeEfficiency(Path path) {
        Double simpleComplexity = routeComplexity(path);
        Double geographicalDistance = path.origin.getDistance(path.destination);
        Double adjustmentWeight = 0.5;

        if (config.get("adjustment-weight") != null) {
            adjustmentWeight = Double.parseDouble(config.get("adjustment-weight"));
        }

        if(path.stations.size() <= 3) { // there is no complexity for any connection that has only two stations
            return 1.0;
        }

        // TODO: change in paper
        // raised to power so that low efficiency routes are penalized more
        return Math.pow(simpleComplexity / (adjustmentWeight * geographicalDistance), evalPower);
    }
    
    /*
     * calculates the efficiency of a network based on route demand data
     */

    public Double networkEfficiency(Network network, DemandSet demandSet) {

        Double networkEfficiency = 0.0;
        PathPlanning pp = new LeastTransfers(network);
        Double tripsEfficiency = 0.0;

        // for each trip in the demand set, calculate the efficiency of that route/trip
        // and weigh it based on the "trip" demand.

        // if the trip is not contained within the demandSet, then the demand is 0,
        // so it is fine to ignore it within efficiency calculations based on our
        // definition of complexity

        for (Demand d : demandSet.trips) {
            Path path = pp.pathPlan(d.start, d.end);
            Double efficiency = 0.0;
            efficiency = routeEfficiency(path);

            if (Double.isNaN(efficiency)) {
                efficiency = 0.0;
            }
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
                networkEfficiency = tripsEfficiency * totalCost;
                break;

            case "log-linear":
                networkEfficiency = Math.log(tripsEfficiency) * totalCost;
                break;

            case "linear-log":
                networkEfficiency = tripsEfficiency * Math.log(totalCost);
                break;

            case "log-log":
                networkEfficiency = Math.log(tripsEfficiency) * Math.log(totalCost);
                break;

            default:
                System.out.println("Invalid regression mode, defaulting to linear-linear");
                networkEfficiency = tripsEfficiency * totalCost;
                break;
        }

        return networkEfficiency;
    }

    public Double lineEfficiency(Network network, Line line, DemandSet demandSet) {
        Double lineEfficiency = 0.0;
        PathPlanning pp = new AStar(network);
        Double tripsEfficiency = 0.0;

        // for each trip in the demand set, calculate the efficiency of that route/trip
        // and weigh it based on the "trip" demand.

        // if the trip is not contained within the demandSet, then the demand is 0,
        // so it is fine to ignore it within efficiency calculations based on our
        // definition of complexity

        for (Demand d : demandSet.trips) {
            Path path = pp.pathPlan(d.start, d.end);
            Double efficiency = 0.0;
            efficiency = routeEfficiency(path);

            if (Double.isNaN(efficiency)) {
                efficiency = 0.0;
            }
            tripsEfficiency += (double)d.trips * efficiency;
        }

        Double totalCost = constructionCost(line);

        switch (config.get("line-regression")) {
            case "linear-linear":
                lineEfficiency = tripsEfficiency * totalCost;
                break;

            case "log-linear":
                lineEfficiency = Math.log(tripsEfficiency) * totalCost;
                break;

            case "linear-log":
                lineEfficiency = tripsEfficiency * Math.log(totalCost);
                break;

            case "log-log":
                lineEfficiency = Math.log(tripsEfficiency) * Math.log(totalCost);
                break;

            default:
                System.out.println("Invalid regression mode, defaulting to linear-linear");
                lineEfficiency = tripsEfficiency * totalCost;
                break;
        }

        return lineEfficiency;
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

        WMATA wmata = new WMATA();
        DemandSet demandSet = new DemandSet();
        DemandSet unmodifiedDemand = new DemandSet();
        demandSet.loadTrips("Network/data.csv", wmata.WMATA);
        unmodifiedDemand.loadTrips("Network/data.csv", wmata.WMATA);
        Evaluation eval = new Evaluation("NetworkEvaluation/config");
        
        System.out.println("line efficiency" + eval.lineEfficiency(wmata.WMATA, wmata.WMATA.lines.get(0), unmodifiedDemand));
    }
}