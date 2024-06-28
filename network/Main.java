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

        for (Line line : newNetwork.lines) {
            System.out.println("\n" + line.name + " Line");
            if (line.origin.equals(newNetwork.getStation("glenmont"))) {
                // System.out.println(line.connections);
            }
        }

        for (Line line : newNetwork.getStation("glenmont").lines) {
            System.out.println("glenmont lines: " + line.name);
        }
        
        PathPlanning pp = new PathPlanning(newNetwork);
        Path path = pp.pathPlan(newNetwork.getStation("takoma"), newNetwork.getStation("farragut west"));
        
        for (Station station : path.stations) {
            System.out.println(station.name);
        }


        // System.out.println("New Network Efficiency: " + eval.networkEfficiency(newNetwork, demandSet));
    
        /*
         * the issue is that the lines in the old network are still
         * being remembered by the station itself, so the station in
         * the new nework thinks it belongs on the old WMATA line
         * as well as the newly generated line. Since pathplanning
         * uses the stations lines, it is trying to access old WMATA 
         * connections, which are now null in the connections collection
         * in the new network. 
         * 
         * fix:
         * remove old lines from each station when adding new lines
         * 
         * or reallocated new stations?? 
         */
    }
}
