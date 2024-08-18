package Algorithm;

import Network.*;
import NetworkEvaluation.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        // extract args
        if (args.length != 1) {
            System.out.println("ERROR: missing CLI argument - please pass in path and name of config file");
            System.exit(1);
        }

        String config = args[0];

        System.out.println("----- Line Addition Algorithm -----");
        System.out.println("Running with config file: " + config);
        System.out.println("Loading WMATA network ...");
        WMATA wmata = new WMATA();
        System.out.println("Loading demand set ...");
        DemandSet d = new DemandSet();
        System.out.println("Loading trips data ...");
        d.loadTrips("Network/data.csv", wmata.WMATA);
        System.out.println("Running Line Addition Algorithm ...");
        LineAdditionAlgorithm laa = new LineAdditionAlgorithm(wmata.WMATA, d, config);
        System.out.println("Algorithm complete");
        System.out.println("Best line: ");
        System.out.println(laa.getBestLine());

        // now we want to add the new line to the network and evaluate network efficiency 
        System.out.println("Evaluating Old Network Efficiency ... ");
        Evaluation eval = new Evaluation("NetworkEvaluation/config");
        Double oldNetworkEfficiency = eval.networkEfficiency(wmata.WMATA, d);

        System.out.println("Adding New Line to Network ... ");

        Line bestLine = laa.getBestLine();
        bestLine.name = "New Line";
        wmata.WMATA.addLine(bestLine);
        wmata.WMATA.addLine(bestLine.generateReverseDirection("New Line Reverse"));

        System.out.println("Evaluating New Network Efficiency ... ");
        Double newNetworkEfficiency = eval.networkEfficiency(wmata.WMATA, d);
        ArrayList<String> additionalText = new ArrayList<>();
        additionalText.add("\nOld network efficiency: " + oldNetworkEfficiency + "\n");
        additionalText.add("New network efficiency: " + newNetworkEfficiency + "\n");

        laa.SaveResults(additionalText);
    }
}
