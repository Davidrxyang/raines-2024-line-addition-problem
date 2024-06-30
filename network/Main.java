import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        WMATA wmata = new WMATA();
        DemandSet demandSet = new DemandSet();
        demandSet.loadTrips("network/data.csv", wmata.WMATA);
        Evaluation eval = new Evaluation("network-evaluation/config");
        
        Double oldNetworkEfficiency = eval.networkEfficiency(wmata.WMATA, demandSet);
        
        PIA pia = new PIA(demandSet, wmata.WMATA);
        Network piaNetwork = pia.R;
        Double piaNetworkEfficiency = eval.networkEfficiency(piaNetwork, demandSet);
        Double piaPercentChange = eval.calculatePercentChange(oldNetworkEfficiency, piaNetworkEfficiency);

        RGA rga = new RGA(demandSet, wmata.WMATA, 9);
        Network rgaNetwork = rga.R;
        Double rgaNetworkEfficiency = eval.networkEfficiency(rgaNetwork, demandSet);
        Double rgaPercentChange = eval.calculatePercentChange(oldNetworkEfficiency, rgaNetworkEfficiency);

        System.out.println("Old network efficiency: " + oldNetworkEfficiency);
        System.out.println("PIA network efficiency: " + piaNetworkEfficiency);
        System.out.println("Improvement " + piaPercentChange + "%");
        System.out.println("RGA network efficiency: " + rgaNetworkEfficiency);
        System.out.println("Improvement " + rgaPercentChange + "%");

        // using log-linear regression scheme, which makes the most sense from an economics perspective
        // PIA is improving the network by 114.8 percent.
    }
}
