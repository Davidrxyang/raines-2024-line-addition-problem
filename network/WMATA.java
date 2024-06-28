import java.util.ArrayList;

public class WMATA {
    ArrayList<Station> WMATAStations;
    Network WMATA;

    Line blueLineDowntownLargo;
    Line blueLineFranconiaSpringfield;
    Line orangeLineNewCarrollton;
    Line orangeLineVienna;
    Line silverLineDowntownLargo;
    Line silverLineAshburn;
    Line redLineGlenmont;
    Line redLineShadyGrove;
    Line yellowLineMtVernonSq;
    Line yellowLineHuntington;
    Line greenLineBranchAve;
    Line greenLineGreenbelt;

    public WMATA() {

        WMATAStations = new ArrayList<>();

        /*
         * Blue line streetations
         */

        WMATAStations.add(new Station("franconia-springfield", 38.7667, -77.1679));
        WMATAStations.add(new Station("van dorn street", 38.8000, -77.1288));
        WMATAStations.add(new Station("king street", 38.8071, -77.0597));
        WMATAStations.add(new Station("braddock road", 38.8138, -77.0541));
        WMATAStations.add(new Station("potomac yard", 38.8332, -77.0462)); // note this one is "new"??
        WMATAStations.add(new Station("reagan washington national airport", 38.8536, -77.0440));
        WMATAStations.add(new Station("crystal city", 38.8579, -77.0505));
        WMATAStations.add(new Station("pentagon city", 38.8619, -77.0594));
        WMATAStations.add(new Station("pentagon", 38.8685, -77.0547));
        WMATAStations.add(new Station("arlington cemetery", 38.8846, -77.0631));
        WMATAStations.add(new Station("rosslyn", 38.8969, -77.0720));
        WMATAStations.add(new Station("foggy bottom", 38.9009, -77.0505));
        WMATAStations.add(new Station("farragut west", 38.9016, -77.0420));
        WMATAStations.add(new Station("mcpherson square", 38.9013, -77.0322));
        WMATAStations.add(new Station("metro center", 38.8987, -77.0278));
        WMATAStations.add(new Station("federal triangle", 38.8940, -77.0283));
        WMATAStations.add(new Station("smithsonian", 38.8892, -77.0282));
        WMATAStations.add(new Station("lenfant plaza", 38.8851, -77.0219));
        WMATAStations.add(new Station("federal center sw", 38.8852, -77.0156));
        WMATAStations.add(new Station("capitol south", 38.8858, -77.0060));
        WMATAStations.add(new Station("eastern market", 38.8844, -76.9958));
        WMATAStations.add(new Station("potomac avenue", 38.8813, -76.9853));
        WMATAStations.add(new Station("stadium-armory", 38.8874, -76.9771));
        WMATAStations.add(new Station("benning road", 38.8901, -76.9377));
        WMATAStations.add(new Station("capitol heights", 38.8894, -76.9122));
        WMATAStations.add(new Station("addison road", 38.8867, -76.8955));
        WMATAStations.add(new Station("morgan blvd", 38.8940, -76.8677));
        WMATAStations.add(new Station("downtown largo", 38.9006, -76.8445));

        /*
         * Orange line stations
         */

        WMATAStations.add(new Station("vienna", 38.8770, -77.2714));
        WMATAStations.add(new Station("dunn loring", 38.8840, -77.2710));
        WMATAStations.add(new Station("west falls church", 38.9011, -77.18927));
        WMATAStations.add(new Station("east falls church", 38.8861, -77.1570));
        WMATAStations.add(new Station("ballston", 38.8820, -77.1122));
        WMATAStations.add(new Station("virginia square-gmu", 38.8831, -77.1034));
        WMATAStations.add(new Station("clarendon", 38.8867, -77.0954));
        WMATAStations.add(new Station("court house", 38.8906, -77.0860));

        // common stations with blue line

        WMATAStations.add(new Station("minnesota avenue", 38.8992, -76.9468));
        WMATAStations.add(new Station("deanwood", 38.9082, -76.9352));
        WMATAStations.add(new Station("cheverly", 38.9165, -76.9164));
        WMATAStations.add(new Station("landover", 38.93345, -76.8913));
        WMATAStations.add(new Station("new carrollton", 38.9480, -76.8718));

        /*
         * silver line stations
         */

        WMATAStations.add(new Station("ashburn", 39.0049, -77.4923));
        WMATAStations.add(new Station("loudoun gateway", 38.9915, -77.4606));
        WMATAStations.add(new Station("washington dulles international airport", 38.9560, -77.4480));
        WMATAStations.add(new Station("innovation center", 38.9617, -77.4167));
        WMATAStations.add(new Station("herndon", 38.9529, -77.3849));
        WMATAStations.add(new Station("reston town center", 38.9527, -77.3601));
        WMATAStations.add(new Station("wiehle", 38.9479, -77.3403));
        WMATAStations.add(new Station("spring hill", 38.9292, -77.2420));
        WMATAStations.add(new Station("greensboro", 38.9209, -77.2338));
        WMATAStations.add(new Station("tysons corner", 38.9207, -77.2215));
        WMATAStations.add(new Station("mclean", 38.9243, -77.2105));

        // common stations with blue line

        /*
         * red line stations
         */

        WMATAStations.add(new Station("shady grove", 39.1206, -77.1650));
        WMATAStations.add(new Station("rockville", 39.0843, -77.1464));
        WMATAStations.add(new Station("twinbrook", 39.0627, -77.1213));
        WMATAStations.add(new Station("north bethesda", 39.0478, -77.1127));
        WMATAStations.add(new Station("grosvenor", 39.0296, -77.1040));
        WMATAStations.add(new Station("medical center", 38.9987, -77.0969));
        WMATAStations.add(new Station("bethesda", 38.9834, -77.0937));
        WMATAStations.add(new Station("friendship heights", 38.9603, -77.0857));
        WMATAStations.add(new Station("tenleytown-au", 38.9489, -77.0797));
        WMATAStations.add(new Station("van ness-udc", 38.9436, -77.0632));
        WMATAStations.add(new Station("cleveland park", 38.9354, -77.0585));
        WMATAStations.add(new Station("woodley park-zoo", 38.9253, -77.0527));
        WMATAStations.add(new Station("dupont circle", 38.9103, -77.0440));
        WMATAStations.add(new Station("farragut north", 38.9032, -77.0397));
        // metro center
        WMATAStations.add(new Station("gallery place-chinatown", 38.8983, -77.0228));
        WMATAStations.add(new Station("judiciary square", 38.8967, -77.0177));
        WMATAStations.add(new Station("union station", 38.8975, -77.0075));
        WMATAStations.add(new Station("new york ave", 38.9066, -77.0033));
        WMATAStations.add(new Station("rhode island avenue", 38.9209, -76.9960));
        WMATAStations.add(new Station("brookland", 38.9333, -76.9945));
        WMATAStations.add(new Station("fort totten", 38.9519, -77.0023));
        WMATAStations.add(new Station("takoma", 38.9760, -77.0182));
        WMATAStations.add(new Station("silver spring", 38.9939, -77.0313));
        WMATAStations.add(new Station("forest glen", 39.0155, -77.0430));
        WMATAStations.add(new Station("wheaton", 39.0380, -77.0504));
        WMATAStations.add(new Station("glenmont", 39.0620, -77.0536));

        /*
         * yellow line stations
         */

        WMATAStations.add(new Station("huntington", 38.7939, -77.0754));
        WMATAStations.add(new Station("eisenhower avenue", 38.8004, -77.0712));

        // blue line stations

        // lenfant plaze

        WMATAStations.add(new Station("archives-navy memorial", 38.8932, -77.0219));

        // gallery place

        WMATAStations.add(new Station("mt vernon sq", 38.9052, -77.0221));

        /*
         * green line stations
         */

        WMATAStations.add(new Station("branch avenue", 38.8269, -76.9122));
        WMATAStations.add(new Station("suitland", 38.8448, -76.9321));
        WMATAStations.add(new Station("naylor road", 38.8510, -76.9562));
        WMATAStations.add(new Station("southern avenue", 38.8410, -76.9751));
        WMATAStations.add(new Station("congress heights", 38.8457, -76.9883));
        WMATAStations.add(new Station("anacostia", 38.8625, -76.9953));
        WMATAStations.add(new Station("navy yard", 38.8765, -77.0055));
        WMATAStations.add(new Station("waterfront", 38.8765, -77.0175));

        // yellow line stations

        WMATAStations.add(new Station("shaw-howard university", 38.8136, -77.0219));
        WMATAStations.add(new Station("u street-cardozo", 38.9169, -77.0281));
        WMATAStations.add(new Station("columbia heights", 38.9282, -77.0326));
        WMATAStations.add(new Station("georgia avenue-petworth", 38.9374, -77.0235));

        // fort totten

        WMATAStations.add(new Station("west hyattsville", 38.9554, -76.9694));
        WMATAStations.add(new Station("hyattsville crossing", 38.9655, -76.9562));
        WMATAStations.add(new Station("college park-u of md", 38.9733, -76.92814));
        WMATAStations.add(new Station("greenbelt", 39.0110, -76.9114));

        // generating the network

        WMATA = new Network("WMATA", WMATAStations);

        blueLineDowntownLargo = new WMATABlueLineDowntownLargo(WMATA);
        blueLineFranconiaSpringfield = new WMATABlueLineFranconiaSpringfield(WMATA);
        orangeLineNewCarrollton = new WMATAOrangeLineNewCarrollton(WMATA);
        orangeLineVienna = new WMATAOrangeLineVienna(WMATA);
        silverLineDowntownLargo = new WMATASilverLineDowntownLargo(WMATA);
        silverLineAshburn = new WMATASilverLineAshburn(WMATA);
        redLineGlenmont = new WMATARedLineGlenmont(WMATA);
        redLineShadyGrove = new WMATARedLineShadyGrove(WMATA);
        yellowLineMtVernonSq = new WMATAYellowLineMtVernonSq(WMATA);
        yellowLineHuntington = new WMATAYellowLineHuntington(WMATA);
        greenLineBranchAve = new WMATAGreenLineBranchAve(WMATA);
        greenLineGreenbelt = new WMATAGreenLineGreenbelt(WMATA);

        WMATA.addLine(blueLineDowntownLargo);
        WMATA.addLine(blueLineFranconiaSpringfield);
        WMATA.addLine(orangeLineNewCarrollton);
        WMATA.addLine(orangeLineVienna);
        WMATA.addLine(silverLineDowntownLargo);
        WMATA.addLine(silverLineAshburn);
        WMATA.addLine(redLineGlenmont);
        WMATA.addLine(redLineShadyGrove);
        WMATA.addLine(yellowLineMtVernonSq);
        WMATA.addLine(yellowLineHuntington);
        WMATA.addLine(greenLineBranchAve);
        WMATA.addLine(greenLineGreenbelt);
    }

    public static void main(String[] args) {
        WMATA WMATA = new WMATA();
    }
}
