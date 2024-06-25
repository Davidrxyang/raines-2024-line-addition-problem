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

    public Double routeEfficiency(Path path) {
        Double transferWeight = Double.parseDouble(config.get("transferWeight"));
        Double stationWeight = Double.parseDouble(config.get("stationWeight"));
        Double distanceWeight = Double.parseDouble(config.get("distanceWeight"));   

        return (transferWeight * path.nTransfers + stationWeight * path.stations.size() + distanceWeight * path.getLength());
    }

    public Double networkEfficiency(Network network) {
        return 0.0;
    }
    
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

        PathPlanning pp = new PathPlanning(WMATA.WMATA);
    
        Path bethesdaToAnacostia = pp.pathPlan(WMATA.WMATA.getStation("bethesda"), WMATA.WMATA.getStation("anacostia"));

        System.out.println(eval.constructionCost(WMATA.WMATA.lines.get(0)));
        System.out.println(eval.routeEfficiency(bethesdaToAnacostia));
    }
}