/*
 * simple dataset created for testing purposes
 */
package Network; 

import java.util.ArrayList;
import java.util.Arrays;

import Network.Station;
import Network.Line;

import ExistingAlgorithms.PathPlanning;

public class SimpleNetwork {
    public static void main(String[] args) {
        // simple network 

        
        Station A = new Station("A", 0.0, 0.0);
        Station B = new Station("B", 1.0, 0.0);
        Station C = new Station("C", 2.0, 0.0);
        Station D = new Station("D", 0.0, 1.0);
        Station E = new Station("E", 1.0, 1.0);
        Station F = new Station("F", 2.0, 1.0);
        Station G = new Station("G", 3.0, 1.0);
        Station H = new Station("H", 2.0, -1.0);
        
        ArrayList<Station> allStations = new ArrayList<Station>(Arrays.asList(A, B, C, D, E, F, G, H));

        Line red_AE = new Line("red A -> E");
        Line green_AE = new Line("green A -> E");
        Line blue_EF = new Line("blue E -> F");
        Line orange_AD = new Line("orange A -> D");
        Line silver_FG = new Line("silve F -> D");
        Line purple_BH = new Line("purple B -> H");

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

        orange_AD.addStation(A, null);
        orange_AD.addStation(D, 1.0);
        orange_AD.setDestination(D);

        silver_FG.addStation(F, null);
        silver_FG.addStation(G, 1.0);
        silver_FG.setDestination(G);

        purple_BH.addStation(B, null);
        purple_BH.addStation(H, 1.0);
        purple_BH.setDestination(H);

        // generate reverse lines
        Line red_EA = red_AE.generateReverseDirection("red E -> A");
        Line green_EA = green_AE.generateReverseDirection("green E -> A");
        Line blue_FE = blue_EF.generateReverseDirection("blue F -> E");
        Line orange_DA = orange_AD.generateReverseDirection("orange D -> A");
        Line silver_GF = silver_FG.generateReverseDirection("silver G -> F");
        Line purple_HB = purple_BH.generateReverseDirection("purple H -> B");
        
        // add lines to network
        
        Network simpleNetwork = new Network("simple network", allStations);
        simpleNetwork.addLine(red_AE);
        simpleNetwork.addLine(green_AE);
        simpleNetwork.addLine(blue_EF);
        simpleNetwork.addLine(red_EA);
        simpleNetwork.addLine(green_EA);
        simpleNetwork.addLine(blue_FE);
        simpleNetwork.addLine(orange_AD);
        simpleNetwork.addLine(orange_DA);
        simpleNetwork.addLine(silver_FG);
        simpleNetwork.addLine(silver_GF);
        simpleNetwork.addLine(purple_BH);
        simpleNetwork.addLine(purple_HB);

        PathPlanning pathPlanning = new PathPlanning(simpleNetwork);
        pathPlanning.printMatrix(pathPlanning.connectivityMatrix);
        pathPlanning.printMatrix(pathPlanning.connectivityMatrixPower(pathPlanning.connectivityMatrix, 2));

        System.out.println(pathPlanning.pathPlan(C, A).connections);
        System.out.println(pathPlanning.pathPlan(F, A).connections);
        System.out.println(pathPlanning.pathPlan(B, G).connections);
        System.out.println(pathPlanning.pathPlan(H, G).connections);


        System.out.println(orange_AD.transferLines(simpleNetwork));

    }
}
