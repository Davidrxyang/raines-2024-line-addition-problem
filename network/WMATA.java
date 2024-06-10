import java.util.ArrayList;

public class WMATA {
    public static void main(String[] args) {

        ArrayList<Station> WMATAStations = new ArrayList<>();

        WMATAStations.add( new Station("franconia-springfield", 38.7667, -77.1679));
        WMATAStations.add( new Station("van dorn st", 38.8000, -77.1288));
        WMATAStations.add( new Station("king st old town", 38.8071, -77.0597));
        WMATAStations.add( new Station("braddock rd", 38.8138, -77.0541));
        WMATAStations.add( new Station("potomac yard", 38.8332, -77.0462)); // note this one is "new"??
        WMATAStations.add( new Station("ronald reagan washington international", 38.8536, -77.0440));
        WMATAStations.add( new Station("crystal city", 38.8579, -77.0505));
        WMATAStations.add( new Station("pentagon city", 38.8619, -77.0594));
        WMATAStations.add( new Station("pentagon", 38.8685, -77.0547));
        WMATAStations.add( new Station("arlington cemetery", 38.8846, -77.0631));
        WMATAStations.add( new Station("rosslyn", 38.8969, -77.0720));
        WMATAStations.add( new Station("foggy bottom", 38.9009, -77.0505));
        WMATAStations.add( new Station("farragut west", 38.9016, -77.0420));
        WMATAStations.add( new Station("mcpherson square", 38.9013, -77.0322));
        WMATAStations.add( new Station("metro center", 38.8987, -77.0278));
        WMATAStations.add( new Station("federal triangle", 38.8940, -77.0283));
        WMATAStations.add( new Station("smithsonian", 38.8892, -77.0282));
        WMATAStations.add( new Station("lenfant plaza", 38.8851, -77.0219));
        WMATAStations.add( new Station("federal center sw", 38.8852, -77.0156));
        WMATAStations.add( new Station("capitol south", 38.8858, -77.0060));
        WMATAStations.add( new Station("eastern market", 38.8844, -76.9958));
        WMATAStations.add( new Station("potomac ave", 38.8813, -76.9853));
        WMATAStations.add( new Station("stadium-armory", 38.8874, -76.9771));
        WMATAStations.add( new Station("benning rd", 38.8901, -76.9377));
        WMATAStations.add( new Station("capitol heights", 38.8894, -76.9122));
        WMATAStations.add( new Station("addison rd", 38.8867, -76.8955));
        WMATAStations.add( new Station("morgan blvd", 38.8940, -76.8677));
        WMATAStations.add( new Station("downtown largo", 38.9006, -76.8445));
        
        Network WMATA = new Network("WMATA", WMATAStations);

        Line blueLineDowntownLargo = new WMATABlueLineDowntownLargo(WMATA);
        Line blueLineFranconiaSpringfield = new WMATABlueLineFranconiaSpringfield(WMATA);

        /*
        
        Line orange_line = new Line("orange line");
        orange_line.addStation(capitol_south, null);
        orange_line.addStation(eastern_market, 0.52);
        
        System.out.println("eastern market lines");
        System.out.println(eastern_market);
        
        WMATAStations.addAll(orange_line.stations);
        */
        

        WMATA.addLine(blueLineDowntownLargo);
        // WMATA.addLine(orange_line);

        System.out.println(WMATA);

        PathPlanning pp = new PathPlanning(WMATA);
        pp.printMatrix(pp.connectivityMatrix);
    }
}
