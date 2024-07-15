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
        eval = new Evaluation("NetworkEvaluation/config");


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

    public void constructLine(Station vi, Station vj, ArrayList<Station> stations, Line l, double height) {
        // the stations that are considered in a range between vi and vj
        ArrayList<Station> s = new ArrayList<>();

        for (Station station : stations) {
            if (stationInCorridor(vi, vj, station, height) && station != vi && station != vj) {
                s.add(station);
            }
        }

        System.out.println("vi: " + vi.name + " vj: " + vj.name);

        if (s.size() == 0) {
            if (G.connectionMap.get(vi.name + " -> " + vj.name) != null) {
                l.addConnection(G.connectionMap.get(vi.name + " -> " + vj.name)); 
            } else {
                Connection newConnection = new Connection(vi, vj, vi.getDistance(vj));
                G.connections.add(newConnection);
                G.connectionMap.put(newConnection.toString(), newConnection);
                l.addConnection(newConnection);
            }
        } else {
            int maxDemand = 0;
            Station vk = null;
            for (Station station : s) {
                int demand = 0;
                Demand d1 = D.getDemand(vi, station);
                Demand d2 = D.getDemand(station, vi);
                Demand d3 = D.getDemand(vj, station);
                Demand d4 = D.getDemand(station, vj);

                if (d1 != null) {
                    demand += d1.trips;
                }
                if (d2 != null) {
                    demand += d2.trips;
                }
                if (d3 != null) {
                    demand += d3.trips;
                }
                if (d4 != null) {
                    demand += d4.trips;
                }
                
                if (demand > maxDemand && station != vi && station != vj) {
                    vk = station;
                    maxDemand = demand;
                }
            }
            constructLine(vi, vk, s, l, height);
            constructLine(vk, vj, s, l, height);
        }
    }

    // function to calculate if a station vk is in the corridor between vi and vj
    // height = [0, 1] is a percentage of the length between the stations vi and vj
    private boolean stationInCorridor(Station vi, Station vj, Station vk, double height) {
        Point target = new Point(vk.longitude, vk.latitude);
        ArrayList<Point> vertices = new ArrayList<>();
        vertices.add(new Point(vj.longitude, vj.latitude));
        
        
        Point midPoint = new Point((vi.longitude + vj.longitude) / 2, (vi.latitude + vj.latitude) / 2);
        Vector v = new Vector((midPoint.x - vi.longitude) * height, (midPoint.y - vi.latitude) * height);

        v.rotate(Math.PI / 2);
        vertices.add(new Point(midPoint.x + v.x, midPoint.y + v.y));

        vertices.add(new Point(vi.longitude, vi.latitude));

        v.rotate(Math.PI);
        vertices.add(new Point(midPoint.x + v.x, midPoint.y + v.y));

        Path2D path = new Path2D.Double();

        // Move to the first point in the polygon
        path.moveTo(vertices.get(0).x, vertices.get(0).y);

        // Connect the points in the polygon
        for (int i = 1; i < vertices.size(); i++) {
            path.lineTo(vertices.get(i).x, vertices.get(i).y);
        }

        // Close the path
        path.closePath();

        // Create a Point2D object for the test point
        Point2D testPoint = new Point2D.Double(target.x, target.y);

        // Check if the test point is inside the polygon
        return path.contains(testPoint);
    }

    // aux class
    class Point {
        double x, y;

        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    class Vector {
        double x, y;

        Vector(double x, double y) {
            this.x = x;
            this.y = y;
        }

        // rotate in radians
        void rotate(double n) {
            double rx = (this.x * Math.cos(n)) - (this.y * Math.sin(n));
            double ry = (this.x * Math.sin(n)) + (this.y * Math.cos(n));
            x = rx;
            y = ry;
        }
    }

    public static void main(String[] args) {
        WMATA wmata = new WMATA();
        DemandSet d = new DemandSet();
        d.loadTrips("Network/data.csv", wmata.WMATA);
        Line l = new Line();
        LineAdditionAlgorithm laa = new LineAdditionAlgorithm(wmata.WMATA, d, 0);
        laa.constructLine(wmata.WMATA.getStation("anacostia"), wmata.WMATA.getStation("vienna"), wmata.WMATA.stationList, l, 0.3);
        System.out.println(l);
    }
}
