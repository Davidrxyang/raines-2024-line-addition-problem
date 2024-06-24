public class WMATARedLineGlenmont extends Line {

    public WMATARedLineGlenmont(Network network) {

        this.name = "red line shady grove -> glenmont";

        this.addStation(network.stations.get("shady grove"), null);
        this.addStation(network.stations.get("rockville"), 2.64);
        this.addStation(network.stations.get("twinbrook"), 2.02);
        this.addStation(network.stations.get("north bethesda"), 1.13);
        this.addStation(network.stations.get("grosvenor"), 1.38);
        this.addStation(network.stations.get("medical center"), 2.14);
        this.addStation(network.stations.get("bethesda"), 1.05);
        this.addStation(network.stations.get("friendship heights"), 1.62);
        this.addStation(network.stations.get("tenleytown-au"), 0.84);
        this.addStation(network.stations.get("van ness-udc"), 1.07);
        this.addStation(network.stations.get("cleveland park"), 0.61);
        this.addStation(network.stations.get("woodley park-zoo"), 0.76);
        this.addStation(network.stations.get("dupont circle"), 1.13);
        this.addStation(network.stations.get("farragut north"), 0.55);
        this.addStation(network.stations.get("metro center"), 0.78);
        this.addStation(network.stations.get("gallery place-chinatown"), 0.28);
        this.addStation(network.stations.get("judiciary square"), 0.31);
        this.addStation(network.stations.get("union station"), 0.71);
        this.addStation(network.stations.get("new york ave"), 0.66);
        this.addStation(network.stations.get("rhode island avenue"), 1.07);
        this.addStation(network.stations.get("brookland"), 0.86);
        this.addStation(network.stations.get("fort totten"), 1.36);
        this.addStation(network.stations.get("takoma"), 1.88);
        this.addStation(network.stations.get("silver spring"), 1.43);
        this.addStation(network.stations.get("forest glen"), 1.61);
        this.addStation(network.stations.get("wheaton"), 1.61);
        this.addStation(network.stations.get("glenmont"), 1.65);

        this.setDestination(network.stations.get("glenmont"));

    }
}
