/*
 * WMATA Blue Line
 */

public class WMATAGreenLineBranchAve extends Line {

    public WMATAGreenLineBranchAve(Network network) {

        this.name = "yellow line huntington -> mt vernon sq";

        this.addStation(network.stations.get("branch ave"), null);
        this.addStation(network.stations.get("suitland"), 1.69);
        this.addStation(network.stations.get("naylor rd"), 1.44);
        this.addStation(network.stations.get("southern ave"), 1.26);
        this.addStation(network.stations.get("congress heights"), 1.03);
        this.addStation(network.stations.get("anacostia"), 1.25);
        this.addStation(network.stations.get("navy yard-ballpark"), 1.17);
        this.addStation(network.stations.get("waterfront"), 0.66);
        this.addStation(network.stations.get("lenfant plaza"), 0.80);
        this.addStation(network.stations.get("archives"), 0.55);
        this.addStation(network.stations.get("gallery place"), 0.36);
        this.addStation(network.stations.get("mt vernon sq"), 0.49);
        this.addStation(network.stations.get("shaw-howard u"), 0.51);
        this.addStation(network.stations.get("u st"), 0.51);
        this.addStation(network.stations.get("columbia heights"), 0.91);
        this.addStation(network.stations.get("georgia ave-petworth"), 0.88);
        this.addStation(network.stations.get("fort totten"), 1.62);
        this.addStation(network.stations.get("west hyattsville"), 2.00);
        this.addStation(network.stations.get("hyattsville crossing"), 1.25);
        this.addStation(network.stations.get("college park-u of md"), 2.03);
        this.addStation(network.stations.get("greenbelt"), 2.48);

        this.setDestination(network.stations.get("greenbelt"));

        this.reverse("green line greenbelt -> branch ave");
    }
}