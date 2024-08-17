package Algorithm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

import ExistingAlgorithms.*;
import Network.*;
import NetworkEvaluation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class LineAdditionAlgorithm {
    Network G;
    Network networkCopy; // copy of the network with the line candidates added
    DemandSet unmodifiedDemand;
    DemandSet D;
    DemandSet modifiedDemand;
    PriorityQueue<Efficiency> E;
    ArrayList<Line> lineCandidates;
    Evaluation eval;
    double pMax = 1.5; // circuity factor
    double maxLength = 35; // maximum length of a line in miles
    double minLength = 10; // minimum length of a line in miles
    Line bestLine;
    double corridorHeight = 0.3;
    double demandAdjustmentWeight = 10;
    double targetEfficiency = 120;
    String experimentName = "experiment";
    String evalConfig = "NetworkEvaluation/config";
    int totalEpochs = 0;
    long startTime = 0;

    HashMap<String, String> config;

    boolean log = false;

    public LineAdditionAlgorithm(Network network, DemandSet demandSet, String filename) {
        
        startTime = System.currentTimeMillis();

        config = new HashMap<>();
        readConfig(filename);

        bestLine = null;
        G = network;
        networkCopy = new Network(G);
        unmodifiedDemand = demandSet;
        D = new DemandSet(demandSet);
        eval = new Evaluation(evalConfig);

        lineCandidates = new ArrayList<Line>();
        E = new PriorityQueue<>();
        ArrayList<Line> relevantLines = new ArrayList<>();

        if (log) {
            System.out.println("LOG || CONFIGURATION: ");
            System.out.println("LOG || experiment name: " + experimentName);
            System.out.println("LOG || network name: " + G.getName());
            System.out.println("LOG || pMax: " + pMax);
            System.out.println("LOG || max length: " + maxLength);
            System.out.println("LOG || min length: " + minLength);
            System.out.println("LOG || corridor height: " + corridorHeight);
            System.out.println("LOG || demand adjustment weight: " + demandAdjustmentWeight);
            System.out.println("LOG || target efficiency: " + targetEfficiency);
            System.out.println("LOG ||");
            System.out.println("LOG || starting algorithm...");
            System.out.println("LOG ||");
        }

        createEfficiencyMatrix();


        int epoch = 0;

        while (!targetEfficiencySatisfied(targetEfficiency) && E.size() > 0) {

            epoch++;

            if (log) {
                System.out.println("LOG ||");
                System.out.println("LOG || EPOCH: " + epoch);
                System.out.println("LOG ||");
                System.out.println("LOG || elapsed time in minutes: " + (System.currentTimeMillis() - startTime) / 60000);
            }

            Efficiency worstEfficiency = E.poll();

            if (log) {
                System.out.println("LOG || epoch worst efficiency line: " + worstEfficiency + ", efficiency value: " + worstEfficiency.efficiency);
            }

            Station v_i = worstEfficiency.origin;
            Station v_j = worstEfficiency.destination;
            Line r_m_prime = null;
            Line r_n_prime = null;
            Line remove = null;
            Line r = null;

            ArrayList<Line> addLines = new ArrayList<>();
            ArrayList<Line> removeLines = new ArrayList<>();
            for (Line r_m : lineCandidates) {
                if (r_m.stations.contains(v_i)) {
                    r_m_prime = addToLine(v_j, r_m);

                    if (r_m_prime != null) {
                        addLines.add(r_m_prime);
                        removeLines.add(r_m);
                        for (Line r_k : lineCandidates) {
                            Line r_k_prime = joinLine(r_k, r_m_prime);
                            if (r_k_prime != null) {
                                addLines.add(r_k_prime);
                                relevantLines.add(r_k_prime);
                            }
                        }
                    }

                }
            }

            for (Line r_n : lineCandidates) {
                if (r_n.stations.contains(v_j)) {
                    r_n_prime = addToLine(v_i, r_n);

                    if (r_n_prime != null) {
                        addLines.add(r_n_prime);
                        removeLines.add(r_n);
                        for (Line r_k : lineCandidates) {
                            Line r_k_prime = joinLine(r_k, r_n_prime);
                            if (r_k_prime != null) {
                                addLines.add(r_k_prime);
                                relevantLines.add(r_k_prime);
                            }
                        }
                    }
                }
            }
            if (r_n_prime != null) {
                lineCandidates.add(r_n_prime);
                lineCandidates.remove(remove);
            }
            // to avoid concurrent modification issues
            for (Line l : addLines) {
                lineCandidates.add(l);
            }
            for (Line l : removeLines) {
                lineCandidates.remove(l);
            }

            if (r_m_prime == null && r_n_prime == null) {
                r = new Line();
                constructLine(v_i, v_j, network.stationList, r, corridorHeight);
                lineCandidates.add(r);
                relevantLines.add(r);
            }

            relevantLines.addAll(addLines);
            removeNodePairsFromE(relevantLines);
            removeSubsetLines(relevantLines);

            if (log) {
                System.out.println("LOG || epoch line candidates: " + lineCandidates);
            }

            updateEfficiency();

        }
        totalEpochs = epoch;
        // find the best line
        findBestLine();
        System.out.println("LOG || best line: " + bestLine);
    }

    // reads parameters from config file
    public void readConfig(String filename) {
        try {
            Scanner scanner = new Scanner(new File(filename));
            
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(":");
                config.put(parts[0], parts[1]);
            }
            scanner.close();
        }
        catch (Exception e) {
            System.out.println("Error reading config file");
        }

        // extract parameters from config
        if (config.get("p-max") != null) {
            pMax = Double.parseDouble(config.get("p-max"));
        }
        if (config.get("max-length") != null) {
            maxLength = Double.parseDouble(config.get("max-length"));
        }
        if (config.get("min-length") != null) {
            minLength = Double.parseDouble(config.get("min-length"));
        }
        if (config.get("corridor-height") != null) {
            corridorHeight = Double.parseDouble(config.get("corridor-height"));
        }
        if (config.get("demand-adjustment-weight") != null) {
            demandAdjustmentWeight = Double.parseDouble(config.get("demand-adjustment-weight"));
        }
        if (config.get("target-efficiency") != null) {
            targetEfficiency = Double.parseDouble(config.get("target-efficiency"));
        }
        if (config.get("experiment-name") != null) {
            experimentName = config.get("experiment-name");
        }
        if (config.get("evaluation-config") != null) {
            evalConfig = config.get("evaluation-config");
        }
        if (config.get("logging") != null) {
            if (config.get("logging").equals("true")) {
                log = true;
            }
        }
    }

    public void removeSubsetLines(ArrayList<Line> relevantLines) {
        ArrayList<Line> toRemove = new ArrayList<>();
        for (int i = 0; i < lineCandidates.size() - 1; i++) {
            for (int j = i + 1; j < lineCandidates.size(); j++) {
                if (lineCandidates.get(i).hasSubsetLine(lineCandidates.get(j))) {
                    for (Station s : lineCandidates.get(j).stations) {
                        s.removeLine(lineCandidates.get(j));
                    }
                    toRemove.add(lineCandidates.get(j));
                } else if (lineCandidates.get(j).hasSubsetLine(lineCandidates.get(i))) {
                    for (Station s : lineCandidates.get(i).stations) {
                        s.removeLine(lineCandidates.get(i));
                    }
                    toRemove.add(lineCandidates.get(i));
                }
            }
        }
        for (Line l : toRemove) {
            lineCandidates.remove(l);
        }
    }

    public boolean targetEfficiencySatisfied(Double targetEfficiency) {
        for (Line r : lineCandidates) {

            // check if min length constraint is satisfied FIRST
            if (r.getLength() < minLength) {
                continue;
            }
            
            Double efficiency = eval.lineEfficiency(networkCopy, r, unmodifiedDemand);
            
            if (log) {
                System.out.println("LOG || evaluating termination target efficiency satisfiability");
                System.out.println("LOG || line: " + r);
                System.out.println("LOG || efficiency: " + efficiency + ", length: " + r.getLength());
            }

            if (efficiency < targetEfficiency && r.getLength() > minLength) {
                System.out.println("LOG || target efficiency and length satisfied");
                return true;
            }
        }
        if (log) {
            System.out.println("LOG || target efficiency not satisfied");
        }
        return false;
    }

    public Line getBestLine() {
        return bestLine;
    }

    public void findBestLine() {
        System.out.println("LOG || finding best line");
        // the best line is the line with the lowest number for efficiency
        // that satisfies line constraints
        Double bestEfficiency = Double.MAX_VALUE;
        for (Line line : lineCandidates) {
            if (line.getLength () < minLength || line.getLength() > maxLength || !constraintsSatisfied(line)) {
                continue;
            }
            Double efficiency = eval.lineEfficiency(networkCopy, line, D);
            if (efficiency < bestEfficiency && line.getLength() > minLength && constraintsSatisfied(line) ) {
                bestEfficiency = efficiency;
                bestLine = line;
            }
        }
    }

    // optimization idea: affected path-aware paths
    // so that when one path is updated, only the affected paths are changed
    // ideally, we want to only recalculate modified demand for affected paths,
    // however, due to the constantly changing nature of the network,
    // subset-superset
    // relationships are constantly changing and this efficiency improvement would
    // introduce some inaccuracies in the calculation. The current comprehensive
    // (but slow) approach exists for completeness sake.

    // note here thar demand is unchanged, this is because
    // modified demand is a representation of how "impoerant" the station is
    // in the network, so it is not necessary to update it everytime
    public void updateEfficiency() {
        System.out.println("LOG || updating efficiency");
        networkCopy = new Network(G);
        for (int i = 0; i < lineCandidates.size(); i++) {
            lineCandidates.get(i).name = "candidate r" + i;
            networkCopy.addLine(lineCandidates.get(i));
            networkCopy
                    .addLine(lineCandidates.get(i).generateReverseDirection(lineCandidates.get(i).name + " reverse"));
        }

        Efficiency worstEfficiency;
        do {
            worstEfficiency = E.poll();
            PathPlanning pp = new AStar(networkCopy);
            Path p = pp.pathPlan(worstEfficiency.origin, worstEfficiency.destination);
            Double e = eval.routeEfficiency(p) * modifiedDemand.getDemand(p.origin, p.destination).trips;
            e -= modifiedDemand.getDemand(p.origin, p.destination).trips; // TODO: update in paper
            worstEfficiency.efficiency = e;
            E.add(worstEfficiency);
        } while (worstEfficiency != E.peek());
    }

    public void createEfficiencyMatrix() {
        modifiedDemand = new DemandSet();
        ArrayList<Path> paths = new ArrayList<>();

        // make a copy of the existing network and add the line candidates to it
        networkCopy = new Network(G);
        for (int i = 0; i < lineCandidates.size(); i++) {
            lineCandidates.get(i).name = "candidate r" + i;
            networkCopy.addLine(lineCandidates.get(i));
            networkCopy
                    .addLine(lineCandidates.get(i).generateReverseDirection(lineCandidates.get(i).name + " reverse"));
        }

        // using astar for now: least transfers does not make sense for WMATA
        PathPlanning pp = new AStar(networkCopy);

        // temporary fix for line tracking in station
        for (Station s : G.stationList) {
            ArrayList<Line> toRemove = new ArrayList<>();
            for (Line l : s.lines) {
                if (!networkCopy.lines.contains(l)) {
                    toRemove.add(l);
                }
            }
            for (Line l : toRemove) {
                s.lines.remove(l);
            }
        }

        for (Demand d : D.trips) {
            if (d.start != d.end) {
                paths.add(pp.pathPlan(d.start, d.end));
            }
        }

        for (Path i : paths) {
            Double additionalDemand = 0.0;
            for (Path j : paths) {
                if (j.hasSubpath(i)) {
                    additionalDemand += calculateDemandWeight(j, i)
                            * unmodifiedDemand.getDemand(j.origin, j.destination).trips;
                }
            }
            Demand d = new Demand(unmodifiedDemand.getDemand(i.origin, i.destination));
            d.trips = (int) Math.ceil(additionalDemand) + unmodifiedDemand.getDemand(i.origin, i.destination).trips;
            modifiedDemand.trips.add(d);
        }

        for (Path i : paths) {
            Double e = eval.routeEfficiency(i) * modifiedDemand.getDemand(i.origin, i.destination).trips;
            e -= modifiedDemand.getDemand(i.origin, i.destination).trips; // TODO: update in paper
            E.add(new Efficiency(i.origin, i.destination, e));
        }
    }

    public void removeNodePairsFromE(ArrayList<Line> lines) {
        ArrayList<Efficiency> toRemove = new ArrayList<>();
        for (Line line : lines) {
            if (line != null) {
                for (Efficiency e : E) {
                    if (line.stations.contains(e.origin) && line.stations.contains(e.destination)) {
                        // to avoid concurrent modification
                        toRemove.add(e);
                    }
                }
            }
        }
        for (Efficiency e : toRemove) {
            E.remove(e);
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
        // System.out.println("vi: " + vi.name + " vj: " + vj.name);

        for (Station station : stations) {
            if (stationInCorridor(vi, vj, station, height) && station != vi && station != vj) {
                s.add(station);
                // System.out.println(station.name);
            }
        }

        // System.out.println();

        if (s.size() == 0) {
            if (G.connectionMap.get(vi.name + " -> " + vj.name) != null) {
                l.addConnection(G.connectionMap.get(vi.name + " -> " + vj.name));
            } else {
                Connection newConnection = new Connection(vi, vj, vi.getDistance(vj));
                l.addConnection(newConnection);
            }
        } else {
            int maxDemand = Integer.MIN_VALUE;
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

    // adds a station into the most suitable position (shortest total distance) in
    // the line
    // returns a new line
    public Line addToLine(Station vj, Line l) {
        Line temp = null;
        double distance = Double.MAX_VALUE;
        for (int i = 0; i <= l.stations.size(); i++) {
            Line temp2 = new Line(l);
            Line c1 = null;
            if (i > 0) {
                c1 = new Line();
                constructLine(l.stations.get(i - 1), vj, G.stationList, c1, corridorHeight);
                if (c1.stations.size() > 1) {
                    c1.stations.remove(c1.stations.size() - 1);
                    c1.stations.remove(0);
                } else if (c1.stations.size() > 0) {
                    c1.stations.remove(0);
                }
            }
            Line c2 = null;
            if (i < l.stations.size()) {
                c2 = new Line();
                constructLine(vj, l.stations.get(i), G.stationList, c2, corridorHeight);
                if (c2.stations.size() > 1) {
                    c2.stations.remove(c2.stations.size() - 1);
                    c2.stations.remove(0);
                } else if (c2.stations.size() > 0) {
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
        if (newLine != null) {
            for (Station s : newLine.stations) {
                s.addLine(newLine);
            }
        }
        return newLine;
    }

    public Line joinLineHelper(Line l1, Line l2) {
        Line newLine = null;

        Station l2origin = l2.origin;

        boolean overlap = false;
        int startindex = 0;
        for (int i = 0; i < l1.stations.size(); i++) {
            if (startindex > 0 && i - startindex < l2.stations.size()
                    && l1.stations.get(i) != l2.stations.get(i - startindex)) {
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
                newLine.addStation(l2.stations.get(i),
                        l2.stations.get(i).getDistance(newLine.stations.get(newLine.stations.size() - 1)));
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
            // System.out.println("circuity constraint not satisfied for line: " + l);
            return false;
        }

        if (l.getLength() > maxLength) {
            // System.out.println("length constraint not satisfied for line: " + l);
            return false;
        }

        return true;
    }

    // function to calculate if a station vk is in the corridor between vi and vj
    // height = [0, 1] is a percentage of the length between the stations vi and vj
    // note: latitude and longitude should be swapped around, but functionality is
    // not changed
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

    // path j is the subset path of path i
    // calculates the weight of the demand to add to the connection of path i
    // TODO: update in paper
    public double calculateDemandWeight(Path i, Path j) {
        // double bestLength = i.origin.getDistance(i.destination);
        // double actualLength = i.travelCost(i.origin, j.origin) +
        // j.origin.getDistance(j.destination) + i.travelCost(j.destination,
        // i.destination);

        double distance = i.travelCost(i.origin, j.origin) + i.travelCost(j.destination, i.destination);
        return 1.0 / (1.0 + (demandAdjustmentWeight * distance));
    }

    public void SaveResults() {

        File directory = new File("Results");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        // Create the file with the specified experiment name
        File file = new File("Results" + File.separator + experimentName + ".results");

        // Create a list of strings to store the results
        ArrayList<String> results = new ArrayList<>();
        results.add(bestLine.toString());
        results.add("\ntotal algorithm runtime in minutes: " + (System.currentTimeMillis() - startTime) / 60000);
        results.add("network: " + G.getName());
        results.add("pMax: " + pMax);
        results.add("maxLength: " + maxLength);
        results.add("minLength: " + minLength);
        results.add("corridorHeight: " + corridorHeight);
        results.add("demandAdjustmentWeight: " + demandAdjustmentWeight);
        results.add("targetEfficiency: " + targetEfficiency);
        results.add("totalEpochs: " + totalEpochs);

        StringBuilder evalConfig = new StringBuilder();

        // Read the contents of the source file into a string
        try (BufferedReader reader = new BufferedReader(new FileReader("NetworkEvaluation/config"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                evalConfig.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }

        StringBuilder constructionConfig = new StringBuilder();

        // Read the contents of the source file into a string
        try (BufferedReader reader = new BufferedReader(new FileReader("Algorithm/config"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                constructionConfig.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }

        results.add("\nevaluation config: \n" + evalConfig.toString());
        results.add("construction config: \n" + constructionConfig.toString());

        // write to output file

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Write each result into the file
            for (String result : results) {
                writer.write(result);
                writer.newLine(); // Add a new line after each string
            }
            System.out.println("Results saved successfully to " + file.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }

        
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
        System.out.println("----- Line Addition Algorithm -----");
        System.out.println("Loading WMATA network ...");
        WMATA wmata = new WMATA();
        System.out.println("Loading demand set ...");
        DemandSet d = new DemandSet();
        System.out.println("Loading trips data ...");
        d.loadTrips("Network/data.csv", wmata.WMATA);
        System.out.println("Running Line Addition Algorithm ...");
        LineAdditionAlgorithm laa = new LineAdditionAlgorithm(wmata.WMATA, d, "Algorithm/config");
        System.out.println("Algorithm complete");
        System.out.println("Best line: ");
        System.out.println(laa.getBestLine());
        laa.SaveResults();
    }
}
