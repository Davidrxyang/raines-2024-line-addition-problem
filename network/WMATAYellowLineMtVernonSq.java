/*
 * WMATA Blue Line
 */

public class WMATAYellowLineMtVernonSq extends Line {

    public WMATAYellowLineMtVernonSq(Network network) {

        this.name = "yellow line huntington -> mt vernon sq";

        this.addStation(network.stations.get("huntington"), null);
        this.addStation(network.stations.get("eisenhower ave"), 0.52);
        this.addStation(network.stations.get("king st old town"), 0.72);
        this.addStation(network.stations.get("braddock rd"), 0.54);
        this.addStation(network.stations.get("potomac yard"), 1.0);
        this.addStation(network.stations.get("ronald reagan washington international"), 1.48);
        this.addStation(network.stations.get("crystal city"), 0.55);
        this.addStation(network.stations.get("pentagon city"), 0.71);
        this.addStation(network.stations.get("pentagon"), 0.55);
        this.addStation(network.stations.get("lenfant plaza"), 2.38);
        this.addStation(network.stations.get("archives"), 0.55);
        this.addStation(network.stations.get("gallery place"), 0.36);
        this.addStation(network.stations.get("mt vernon sq"), 0.49);

        this.setDestination(network.stations.get("mt vernon sq"));
    }
}