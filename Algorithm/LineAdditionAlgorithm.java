package Algorithm;

import java.util.ArrayList;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

import ExistingAlgorithms.PathPlanning;
import Network.DemandSet;
import Network.Line;
import Network.Network;
import Network.Path;
import Network.Station;
import Network.WMATA;
import Network.Connection;
import Network.Demand;
import Network.Line;
import NetworkEvaluation.*;
import Network.Demand;
import java.util.ArrayList;

public class LineAdditionAlgorithm {
    Network G;
    DemandSet D;
    ArrayList<Double> E;
    ArrayList<Line> lineCandidates;
    Evaluation eval;
    
    public LineAdditionAlgorithm(Network network, DemandSet demandSet, double targetEfficiency) {
        G = network;
        D = demandSet;
        eval = new Evaluation("src/NetworkEvaluation/config");


        lineCandidates = new ArrayList<Line>();
        E = new ArrayList<>();


    }

    public void updateEfficienciesAndDemand(Double c) {
        E = new ArrayList<>(); // reset E
        DemandSet modifiedDemand = new DemandSet();
        ArrayList<Path> paths = new ArrayList<>();
        
        PathPlanning pp = new PathPlanning(G);
        
        for (Station a : G.stationList) {
            for (Station b : G.stationList) {
                paths.add(pp.pathPlan(a, b));
            }
        }
        
        for (Path i : paths) {
            Double additionalDemand = 0.0;
            for (Path j : paths) {
                if (i.hasSubpath(j)) {
                    additionalDemand += c * D.getDemand(j.origin, j.destination).trips;
                }
            }
            Demand d = new Demand(D.getDemand(i.origin, i.destination));
            d.trips = (int) Math.ceil(additionalDemand) + D.getDemand(i.origin, i.destination).trips;
            modifiedDemand.trips.add(d);
        }

        for (Path i : paths) {
            Double e = eval.routeEfficiency(i) * modifiedDemand.getDemand(i.origin, i.destination).trips;
            E.add(e);
        }
    }

    public Line constructLine(Station vi, Station vj) {
        return null;
    }
}
