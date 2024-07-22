package Algorithm;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

import ExistingAlgorithms.*;
import Network.*;
import NetworkEvaluation.*;
import java.util.ArrayList;

public class LineAdditionAlgorithm {
    Network G;
    Network networkCopy; // copy of the network with the line candidates added
    DemandSet D;
    PriorityQueue<Efficiency> E;
    ArrayList<Line> lineCandidates;
    Evaluation eval;
    double pMax = 1.5; // circuity factor
    double maxLength = 40; // maximum length of a line in miles
    double minLength = 10; // minimum length of a line in miles
    Line bestLine;
    
    public LineAdditionAlgorithm(Network network, DemandSet demandSet, double targetEfficiency) {
        bestLine = null;
        G = network;
        networkCopy = new Network(G);
        D = demandSet;
        eval = new Evaluation("NetworkEvaluation/config");


        lineCandidates = new ArrayList<Line>();
        E = new PriorityQueue<>();
        
        // TODO: could consider making this a factor of
        // the distance shortened through a new connection vs the total distance of the line
        // as the percentage of distance shortened represents the desirability of a new connection there
        Double additionalDemandCoefficient = 0.3; 
        updateEfficienciesAndDemand(additionalDemandCoefficient);

        while (!targetEfficiencySatisfied(targetEfficiency) && E.size() > 0) {
            
            // the worst efficiency is the one with the highest value
            Efficiency worstEfficiency = E.poll();

            System.out.println(worstEfficiency + " " + worstEfficiency.efficiency);
            
            Station v_i = worstEfficiency.origin;
            Station v_j = worstEfficiency.destination;
            Line r_m_prime = null;
            Line r_n_prime = null;
            Line remove = null;
            Line r = null;
            
            ArrayList<Line> addLines = new ArrayList<>();
            for (Line r_m : lineCandidates) {
                if (r_m.stations.contains(v_i)) {
                    r_m_prime = addToLine(v_j, r_m);
                    remove = r_m;
                    
                    if (r_m_prime != null) {
                        for (Line r_k : lineCandidates) {
                            Line r_k_prime = joinLine(r_k, r_m_prime);
                            if (r_k_prime != null) {
                                addLines.add(r_k_prime);
                            }
                        }
                    }
                    
                }
            }
            if (r_m_prime != null) {
                lineCandidates.add(r_m_prime);
                lineCandidates.remove(remove);
            }

            for (Line r_n : lineCandidates) {
                if (r_n.stations.contains(v_j)) {
                    r_n_prime = addToLine(v_i, r_n);
                    remove = r_n;
                    
                    if (r_n_prime != null) {
                        for (Line r_k : lineCandidates) {
                            Line r_k_prime = joinLine(r_k, r_m_prime);
                            if (r_k_prime != null) {
                                addLines.add(r_k_prime);
                            }
                        }
                    }
                }
            }
            if (r_n_prime != null) {
                lineCandidates.add(r_n_prime);
                lineCandidates.remove(remove);
            }
            // to avoid concurrent line issues
            for (Line l : addLines) {
                lineCandidates.add(l);
            }
            
            if (r_m_prime == null && r_n_prime == null) {
                Double corridorHeight = 0.3;
                r = new Line();
                constructLine(v_i, v_j, network.stationList, r, corridorHeight);
                lineCandidates.add(r);
            }
            
            ArrayList<Line> relevantLines = new ArrayList<>();
            relevantLines.add(r);
            relevantLines.add(r_m_prime);
            relevantLines.add(r_n_prime);
            updateEfficienciesAndDemand(additionalDemandCoefficient);
            removeNodePairsFromD(relevantLines);
            removeSubsetLines(relevantLines);
            
            System.out.println("line candidates: " + lineCandidates);
        }
        // find the best line
        findBestLine();
    }   

    public void removeSubsetLines(ArrayList<Line> relevantLines) {
        for (Line line : relevantLines) {
            if (line != null) {
                for (Line l : lineCandidates) {
                    if (line.hasSubsetLine(l)) {
                        lineCandidates.remove(l);
                    }
                }
            }
        }
    }

    public boolean targetEfficiencySatisfied(Double targetEfficiency) {
        for (Line r : lineCandidates) {
            if (eval.lineEfficiency(networkCopy, r, D) < targetEfficiency && r.getLength() > minLength) {
                return true;
            }
        }
        return false;
    }

    public Line getBestLine() {
        return bestLine;
    }

    public void findBestLine() {
        // the best line is the line with the lowest number for efficiency
        Double bestEfficiency = Double.MAX_VALUE;
        for (Line line : lineCandidates) {
            Double efficiency = eval.lineEfficiency(networkCopy, line, D);
            if (efficiency < bestEfficiency) {
                bestEfficiency = efficiency;
                bestLine = line;
            }
        }
    }

    // optimization idea: affected path-aware paths
    // so that when one path is updated, only the affected paths are changed
    // ideally, we want to only recalculate modified demand for affected paths,
    // however, due to the constantly changing nature of the network, subset-superset
    // relationships are constantly changing and this efficiency improvement would 
    // introduce some inaccuracies in the calculation. The current comprehensive
    // (but slow) approach exists for completeness sake. 
    
    public void updateEfficienciesAndDemand(Double c) {
        E = new PriorityQueue<>(); // reset E
        DemandSet modifiedDemand = new DemandSet();
        ArrayList<Path> paths = new ArrayList<>();

        // make a copy of the existing network and add the line candidates to it
        networkCopy = new Network(G);
        for (Line l : lineCandidates) {
            networkCopy.addLine(l);
            networkCopy.addLine(l.generateReverseDirection(l.name));
        }
        
        // using astar for now: least transfers does not make sense for WMATA
        PathPlanning pp = new AStar(networkCopy);
        
        // use demandset instead of the entire station list to keep track of removed stations
        for (Demand d : D.trips) {
            if (d.start != d.end){
                paths.add(pp.pathPlan(d.start, d.end));
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
            E.add(new Efficiency(i.origin, i.destination, e));
        }
    }

    public void removeNodePairsFromD(ArrayList<Line> lines) {
        ArrayList<Demand> toRemove = new ArrayList<>();
        for (Line line : lines) {
            if (line != null) {
                for (Demand d : D.trips) {
                    if (line.stations.contains(d.start) && line.stations.contains(d.end)) {
                        // to avoid concurrent modification
                        toRemove.add(d);
                    }
                }
            }
        }
        for (Demand d : toRemove) {
            D.trips.remove(d);
        }
    }

    // constructs a line between two stations vi and vj
    // height is a percentage of the distance between vi and vj
    public void constructLine(Station vi, Station vj, ArrayList<Station> stations, Line l, double height) {
        constructLineHelper(vi, vj, stations, l, height);
        l.setOrigin(vi);
        l.setDestination(vj);
        l.sort();
    }

    private void constructLineHelper(Station vi, Station vj, ArrayList<Station> stations, Line l, double height) {
        // the stations that are considered in a range between vi and vj
        ArrayList<Station> s = new ArrayList<>();

        for (Station station : stations) {
            if (stationInCorridor(vi, vj, station, height) && station != vi && station != vj) {
                s.add(station);
            }
        }

        // System.out.println("vi: " + vi.name + " vj: " + vj.name);

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
            l.addStationUnordered(vi);
            l.addStationUnordered(vk);
            l.addStationUnordered(vj);

            constructLineHelper(vi, vk, s, l, height);
            constructLineHelper(vk, vj, s, l, height);
        }
    }

    // adds a station into the most suitable position (shortest total distance) in the line
    // returns a new line
    public Line addToLine(Station vj, Line l) {
        Line temp = null;
        double distance = Double.MAX_VALUE;
        for (int i = 0; i <= l.stations.size(); i++) {
            Line temp2 = new Line(l);
            Line c1 = null;
            if (i > 0) {
                c1 = new Line();
                constructLine(l.stations.get(i - 1), vj, l.stations, c1, 0.3);
                if (c1 != null) {
                    c1.stations.remove(c1.stations.size() - 1);
                    c1.stations.remove(0);
                }
            }
            Line c2 = null;
            if (i < l.stations.size()) {
                c2 = new Line();
                constructLine(vj, l.stations.get(i), l.stations, c2, 0.3);
                if (c2 != null) {
                    c2.stations.remove(c2.stations.size() - 1);
                    c2.stations.remove(0);
                }
            }
            temp2.insertStation(vj, i);
            temp2.insertLine(c2, i + 1);
            temp2.insertLine(c1, i);
            
            if (temp2.getLength() < distance && constraintsSatisfied(temp2)) {
                temp = temp2;
                distance = temp2.getLength();
            }
        }
        return temp;
    }

    public Line joinLine(Line l1, Line l2) {
        Line newLine = joinLineHelper(l1, l2);
        if (newLine == null) {
            newLine = joinLineHelper(l2, l1);
        }
        if (newLine == null) {
            l1.reverse(l1.name);
            newLine = joinLineHelper(l1, l2);
        }
        if (newLine == null) {
            newLine = joinLineHelper(l2, l1);
        }
        return newLine;
    }

    public Line joinLineHelper(Line l1, Line l2) {
        Line newLine = null;

        Station l2origin = l2.origin;

        boolean overlap = false;
        int startindex = 0;
        for (int i = 0; i < l1.stations.size(); i++) {
            if (startindex > 0 && i - startindex < l2.stations.size() && l1.stations.get(i) != l2.stations.get(i - startindex)) {
                overlap = false;
            }
            if (l1.stations.get(i) == l2origin) {
                startindex = i;
                overlap = true;
            }
        }

        if (overlap) {
            newLine = new Line(l1);
            for (int i = l1.stations.size() - startindex; i < l2.stations.size(); i++) {
                newLine.addStation(l2.stations.get(i), l2.stations.get(i).getDistance(newLine.stations.get(newLine.stations.size() - 1)));
            }
            if (!constraintsSatisfied(newLine)) {
                newLine = null;
            }
        }

        return newLine;
    }

    // removes all the routes from network R
    // that are a subset of another route
    public void removeSubsetRoutes(ArrayList<Line> lines) {
        ArrayList<Line> toRemove = new ArrayList<Line>();
        for (Line l1 : lines) {
            if (l1 != null) {
                for (Line l2 : lineCandidates) {
                    if (l1 != l2 && l1.connections.containsAll(l2.connections)) {
                        toRemove.add(l2);
                    }
                }
            }
        }
        for (Line l : toRemove) {
            lineCandidates.remove(l);
        }
    }

    // checks if a line satisfies the constraints on length and circuity
    public boolean constraintsSatisfied(Line l) {
        double p = 0;
        for (int i = 0; i < l.stations.size() - 1; i++) {
            for (int j = i + 1; j < l.stations.size(); j++) {
                double directDistance = l.stations.get(i).getDistance(l.stations.get(j));
                double lineDistance = l.travelCost(l.stations.get(i), l.stations.get(j));
                double d = lineDistance / directDistance;
                if (d > p) {
                    p = d;
                }
            }
        }
        if (p > pMax) {
            // System.out.println("circuity constraint not satisfied for line: " + l.name);
            return false;
        }

        if (l.getLength() > maxLength || l.getLength() < minLength) {
            // System.out.println("length constraint not satisfied for line: " + l.name);
            return false;
        }

        return true;
    }

    // function to calculate if a station vk is in the corridor between vi and vj
    // height = [0, 1] is a percentage of the length between the stations vi and vj
    // note: latitude and longitude should be swapped around, but functionality is not changed
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

        LineAdditionAlgorithm laa = new LineAdditionAlgorithm(wmata.WMATA, d, 0);
        System.out.println(laa.getBestLine());
    }
}
