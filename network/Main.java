import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        ArrayList<Station> WMATAStations = new ArrayList<>();

        Station rosslyn = new Station("rosslyn", 38.8969, -77.0720);
        Station foggy_bottom = new Station("foggy bottom", 38.9009, -77.0505);
        Station farragut_west = new Station("farragut west", 38.9016, -77.0420);
        Station mcpherson_square = new Station("mcpherson square", 38.9013, -77.0322);
        Station metro_center = new Station("metro center", 38.8987, -77.0278);
        Station federal_triangle = new Station("federal triangle", 38.8940, -77.0283);
        Station smithsonian = new Station("smithsonian", 38.8892, -77.0282);
        Station lenfant_plaza = new Station("lenfant plaza", 38.8851, -77.0219);
        Station federal_center_sw = new Station("federal center sw", 38.8852, -77.0156);
        Station capitol_south = new Station("capitol south", 38.8858, -77.0060);
        Station eastern_market = new Station("eastern market", 38.8844, -76.9958);

        Line blue_line = new Line("blue line");

        blue_line.addStation(rosslyn, null);
        blue_line.addStation(foggy_bottom, 1.3);
        blue_line.addStation(farragut_west, 0.5);
        blue_line.addStation(mcpherson_square, 0.4);
        blue_line.addStation(metro_center, 0.45);
        blue_line.addStation(federal_triangle, 0.3);
        blue_line.addStation(smithsonian, 0.38);
        blue_line.addStation(lenfant_plaza, 0.54);
        blue_line.addStation(federal_center_sw, 0.38);
        blue_line.addStation(capitol_south, 0.58);
        blue_line.addStation(eastern_market, 0.52);

        blue_line.setDestination(eastern_market);
        System.out.println(blue_line.getLength());
        System.out.println(blue_line);

        Line orange_line = new Line("orange line");
        orange_line.addStation(capitol_south, null);
        orange_line.addStation(eastern_market, 0.52);

        System.out.println("eastern market lines");
        System.out.println(eastern_market);

        WMATAStations.addAll(blue_line.stations);
        WMATAStations.addAll(orange_line.stations);

        Network WMATA = new Network("WMATA", WMATAStations);

        WMATA.addLine(blue_line);
        WMATA.addLine(orange_line);

        System.out.println(WMATA);
    }
}
