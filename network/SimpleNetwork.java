/*
 * simple dataset created for testing purposes
 */

import java.util.ArrayList;
import java.util.Arrays;

public class SimpleNetwork {
    public static void main(String[] args) {
        // simple network 

        
        Station A = new Station("A", 0.0, 0.0);
        Station B = new Station("B", 1.0, 0.0);
        Station C = new Station("C", 2.0, 0.0);
        Station D = new Station("D", 0.0, 1.0);
        Station E = new Station("E", 1.0, 1.0);
        Station F = new Station("F", 2.0, 1.0);
        
        ArrayList<Station> allStations = new ArrayList<Station>(Arrays.asList(A, B, C, D, E, F));

        Line red_AE = new Line("red A -> E");
        Line green_AE = new Line("green A -> E");
        Line blue_EF = new Line("blue E -> F");

        red_AE.addStation(A, null);
        red_AE.addStation(D, 1.0);
        red_AE.addStation(E, 1.0);
        red_AE.setDestination(E);

        
        green_AE.addStation(A, null);
        green_AE.addStation(B, 1.0);
        green_AE.addStation(C, 1.0);
        green_AE.addStation(D, 1.0);
        green_AE.addStation(E, 1.0);
        green_AE.setDestination(E);
        
        blue_EF.addStation(E, null);
        blue_EF.addStation(F, 1.0);
        blue_EF.setDestination(F); 

        // generate reverse lines
        Line red_EA = red_AE.generateReverseDirection("red E -> A");
        Line green_EA = green_AE.generateReverseDirection("green E -> A");
        Line blue_FE = blue_EF.generateReverseDirection("blue F -> E");
        
        Network simpleNetwork = new Network("simple network", allStations);
        simpleNetwork.addLine(red_AE);
        simpleNetwork.addLine(green_AE);
        simpleNetwork.addLine(blue_EF);
        simpleNetwork.addLine(red_EA);
        simpleNetwork.addLine(green_EA);
        simpleNetwork.addLine(blue_FE);

        PathPlanning pathPlanning = new PathPlanning(simpleNetwork);
        pathPlanning.printMatrix(pathPlanning.connectivityMatrix);

        System.out.println(pathPlanning.pathPlan(C, A).connections);
    }
}
