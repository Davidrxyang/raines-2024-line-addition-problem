import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        WMATA WMATA = new WMATA();
        WMATA protectedWMATA = new WMATA();
        DemandSet demandSet = new DemandSet();
        DemandSet protectedDemandSet = new DemandSet();
        demandSet.loadTrips("network/data.csv", WMATA.WMATA);
        protectedDemandSet.loadTrips("network/data.csv", WMATA.WMATA);
        Evaluation eval = new Evaluation("network-evaluation/config");
                
        PIA pia = new PIA(demandSet, WMATA.WMATA);

        Network newNetwork = pia.R;

        System.out.println("PIA improvement: " + eval.compareNetworks(protectedWMATA.WMATA, newNetwork, protectedDemandSet) + "%");

    }
}
