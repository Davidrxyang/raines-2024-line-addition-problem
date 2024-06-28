import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        WMATA wmata = new WMATA();
        DemandSet demandSet = new DemandSet();
        demandSet.loadTrips("network/data.csv", wmata.WMATA);
        // Evaluation eval = new Evaluation("network-evaluation/config");
        
        // System.out.println("Old Network Efficiency: " + eval.networkEfficiency(wmata.WMATA, demandSet));
        
        PIA pia = new PIA(demandSet, wmata.WMATA);

        Network newNetwork = pia.R;
        
        PathPlanning pp = new PathPlanning(newNetwork);
        Path path = pp.pathPlan(newNetwork.getStation("takoma"), newNetwork.getStation("farragut west"));
        

        // System.out.println("New Network Efficiency: " + eval.networkEfficiency(newNetwork, demandSet));
    
    }
}
