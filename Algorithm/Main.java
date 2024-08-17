package Algorithm;

import Network.*;

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
        laa.SaveResults();
    }
}
