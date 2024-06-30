import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        WMATA wmata = new WMATA();
        DemandSet demandSet = new DemandSet();
        demandSet.loadTrips("network/data.csv", wmata.WMATA);
        Evaluation eval = new Evaluation("network-evaluation/config");
        
        System.out.println("Old Network Efficiency: " + eval.networkEfficiency(wmata.WMATA, demandSet));
        
        PIA pia = new PIA(demandSet, wmata.WMATA);
        Network piaNetwork = pia.R;
        System.out.println("PIA Network Efficiency: " + eval.networkEfficiency(piaNetwork, demandSet));

        RGA rga = new RGA(demandSet, wmata.WMATA, 9);
        Network rgaNetwork = rga.R;
        System.out.println("RGA Network Efficiency: " + eval.networkEfficiency(rgaNetwork, demandSet));
    }
}
