/*
 * WMATA Blue Line
 */

package Network; 

public class WMATABlueLineDowntownLargo extends Line {

    public WMATABlueLineDowntownLargo(Network network) {

        this.name = "blue line franconia-springfield -> downtown largo";

        this.addStation(network.stations.get("franconia-springfield"), null);
        this.addStation(network.stations.get("van dorn street"), 3.51);
        this.addStation(network.stations.get("king street"), 3.76);
        this.addStation(network.stations.get("braddock road"), 0.54);
        this.addStation(network.stations.get("potomac yard"), 1.0);
        this.addStation(network.stations.get("reagan washington national airport"), 1.48);
        this.addStation(network.stations.get("crystal city"), 0.55);
        this.addStation(network.stations.get("pentagon city"), 0.71);
        this.addStation(network.stations.get("pentagon"), 0.55);
        this.addStation(network.stations.get("arlington cemetery"), 1.0);
        this.addStation(network.stations.get("rosslyn"), 0.94);
        this.addStation(network.stations.get("foggy bottom"), 1.3);
        this.addStation(network.stations.get("farragut west"), 0.5);
        this.addStation(network.stations.get("mcpherson square"), 0.4);
        this.addStation(network.stations.get("metro center"), 0.45);
        this.addStation(network.stations.get("federal triangle"), 0.3);
        this.addStation(network.stations.get("smithsonian"), 0.38);
        this.addStation(network.stations.get("lenfant plaza"), 0.54);
        this.addStation(network.stations.get("federal center sw"), 0.38);
        this.addStation(network.stations.get("capitol south"), 0.58);
        this.addStation(network.stations.get("eastern market"), 0.52);
        this.addStation(network.stations.get("potomac avenue"), 0.59);
        this.addStation(network.stations.get("stadium-armory"), 0.73);
        this.addStation(network.stations.get("benning road"), 2.48);
        this.addStation(network.stations.get("capitol heights"), 1.37);
        this.addStation(network.stations.get("addison road"), 0.92);
        this.addStation(network.stations.get("morgan blvd"), 1.60);
        this.addStation(network.stations.get("downtown largo"), 1.33);

        this.setDestination(network.stations.get("downtown largo"));
    }
}