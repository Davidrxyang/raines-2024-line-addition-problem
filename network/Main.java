import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        WMATA wmata = new WMATA();
        DemandSet demandSet = new DemandSet();
        DemandSet unmodifiedDemand = new DemandSet();
        demandSet.loadTrips("network/data.csv", wmata.WMATA);
        unmodifiedDemand.loadTrips("network/data.csv", wmata.WMATA);
        Evaluation eval = new Evaluation("network-evaluation/config");
        
        Double oldNetworkEfficiency = eval.networkEfficiency(wmata.WMATA, unmodifiedDemand);
                
        PIA pia = new PIA(demandSet, wmata.WMATA);
        Network newNetwork = pia.R;

        Double newNetworkEfficiency = eval.networkEfficiency(newNetwork, unmodifiedDemand);

        Double percentChange = eval.calculatePercentChange(oldNetworkEfficiency, newNetworkEfficiency);

        System.out.println("Old network efficiency: " + oldNetworkEfficiency);
        System.out.println("New network efficiency: " + newNetworkEfficiency);
        System.out.println("Improvement " + percentChange + "%");

        // using log-linear regression scheme, which makes the most sense from an economics perspective
        // PIA is improving the network by 114.8 percent.
    }
}
