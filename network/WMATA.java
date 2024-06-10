import java.util.ArrayList;

public class WMATA {
    public static void main(String[] args) {

        ArrayList<Station> WMATAStations = new ArrayList<>();

        Station franconia_springfield = new Station("franconia-springfield", 38.7667, -77.1679);
        Station van_dorn_st = new Station("van dorn st", 38.8000, -77.1288);
        Station king_st_old_town = new Station("king st old town", 38.8071, -77.0597);
        Station braddock_rd = new Station("braddock rd", 38.8138, -77.0541);
        Station potomac_yard = new Station("potomac yard", 38.8332, -77.0462); // note this one is "new"??
        Station ronald_reagan_washington_international = new Station("ronald reagan washington international", 38.8536, -77.0440);
        Station crystal_city = new Station("crystal city", 38.8579, -77.0505);
        Station pentagon_city = new Station("pentagon city", 38.8619, -77.0594);
        Station pentagon = new Station("pentagon", 38.8685, -77.0547);
        Station arlington_cemetery = new Station("arlington cemetery", 38.8846, -77.0631);
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
        Station potomac_ave = new Station("potomac ave", 38.8813, -76.9853);
        Station stadium_armory = new Station("stadium-armory", 38.8874, -76.9771);
        Station benning_rd = new Station("benning rd", 38.8901, -76.9377);
        Station capitol_heights = new Station("capitol heights", 38.8894, -76.9122);
        Station addison_rd = new Station("addison rd", 38.8867, -76.8955);
        Station morgan_blvd = new Station("morgan blvd", 38.8940, -76.8677);
        Station downtown_largo = new Station("downtown largo", 38.9006, -76.8445);

        Line blue_line = new Line("blue line");

        blue_line.addStation(franconia_springfield, null);
        blue_line.addStation(van_dorn_st, 3.51);
        blue_line.addStation(king_st_old_town, 3.76);
        blue_line.addStation(braddock_rd, 0.54);
        blue_line.addStation(potomac_yard, 1.0);
        blue_line.addStation(ronald_reagan_washington_international, 1.48);
        blue_line.addStation(crystal_city, 0.55);
        blue_line.addStation(pentagon_city, 0.71);
        blue_line.addStation(pentagon, 0.55);
        blue_line.addStation(arlington_cemetery, 1.0);
        blue_line.addStation(rosslyn, 0.94);
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
        blue_line.addStation(potomac_ave, 0.59);
        blue_line.addStation(stadium_armory, 0.73);
        blue_line.addStation(benning_rd, 2.48);
        blue_line.addStation(capitol_heights, 1.37);
        blue_line.addStation(addison_rd, 0.92);
        blue_line.addStation(morgan_blvd, 1.60);
        blue_line.addStation(downtown_largo, 1.33);

        blue_line.setDestination(downtown_largo);
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

        PathPlanning pp = new PathPlanning(WMATA);
        pp.printMatrix(pp.connectivityMatrix);
        System.out.println(pp.K(blue_line, metro_center));
    }
}
