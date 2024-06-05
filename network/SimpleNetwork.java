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

        Line red = new Line("red");
        Line green = new Line("green");
        Line blue = new Line("blue");

        red.addStation(A, null);
        red.addStation(D, 1.0);
        red.addStation(E, 1.0);
        red.setDestination(E);

        green.addStation(A, null);
        green.addStation(B, 1.0);
        green.addStation(C, 1.0);
        green.addStation(D, 1.0);
        green.addStation(E, 1.0);
        green.setDestination(E);

        blue.addStation(E, null);
        blue.addStation(F, 1.0);
        blue.setDestination(F); 
        
        Network simpleNetwork = new Network("simple network", allStations);
        simpleNetwork.addLine(red);
        simpleNetwork.addLine(green);
        simpleNetwork.addLine(blue);
        PathPlanning pathPlanning = new PathPlanning(simpleNetwork);
        pathPlanning.printMatrix(pathPlanning.connectivityMatrix);

    }
}
