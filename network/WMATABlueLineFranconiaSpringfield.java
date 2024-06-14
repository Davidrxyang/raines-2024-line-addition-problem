/*
 * WMATA Blue Line
 */

 public class WMATABlueLineFranconiaSpringfield extends Line {
    public WMATABlueLineFranconiaSpringfield(Network network) {

        /*
         * there must be a better way to generate the reverse line than this ... 
         */
        
        this.name = "blue line downtown largo -> franconia-springfield";

        this.addStation(network.stations.get("downtown largo"), null);
        this.addStation(network.stations.get("morgan blvd"), 1.33);
        this.addStation(network.stations.get("addison rd"), 1.60);
        this.addStation(network.stations.get("capitol heights"), 0.92);
        this.addStation(network.stations.get("benning rd"), 1.37);
        this.addStation(network.stations.get("stadium-armory"), 2.48);
        this.addStation(network.stations.get("potomac ave"), 0.73);
        this.addStation(network.stations.get("eastern market"), 0.59);
        this.addStation(network.stations.get("capitol south"), 0.52);
        this.addStation(network.stations.get("federal center sw"), 0.58);
        this.addStation(network.stations.get("lenfant plaza"), 0.38);
        this.addStation(network.stations.get("smithsonian"), 0.54);
        this.addStation(network.stations.get("federal triangle"), 0.38);
        this.addStation(network.stations.get("metro center"), 0.30);
        this.addStation(network.stations.get("farragut west"), 0.45);
        this.addStation(network.stations.get("mcpherson square"), 0.50);
        this.addStation(network.stations.get("foggy bottom"), 0.40);
        this.addStation(network.stations.get("rosslyn"), 1.30);
        this.addStation(network.stations.get("arlington cemetery"), 0.94);
        this.addStation(network.stations.get("pentagon"), 1.00);
        this.addStation(network.stations.get("pentagon city"), 0.55);
        this.addStation(network.stations.get("crystal city"), 0.71);
        this.addStation(network.stations.get("ronald reagan washington international"), 0.55);
        this.addStation(network.stations.get("potomac yard"), 1.48);
        this.addStation(network.stations.get("braddock rd"), 1.00);
        this.addStation(network.stations.get("king st old town"), 0.54);
        this.addStation(network.stations.get("van dorn st"), 3.76);
        this.addStation(network.stations.get("franconia-springfield"), 3.51);

        this.setDestination(network.stations.get("franconia-springfield"));
    }
}