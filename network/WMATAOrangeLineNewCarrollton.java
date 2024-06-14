/*
 * WMATA Blue Line
 */

 public class WMATAOrangeLineNewCarrollton extends Line {

    public WMATAOrangeLineNewCarrollton(Network network) {

        this.name = "orange line vienna -> new carrollton";

        this.addStation(network.stations.get("vienna"), null);
        this.addStation(network.stations.get("dunn loring"), 2.44);
        this.addStation(network.stations.get("west falls church"), 2.32);
        this.addStation(network.stations.get("east falls church"), 2.08);
        this.addStation(network.stations.get("ballston-mu"), 2.65);
        this.addStation(network.stations.get("virginia sq-gmu"), 0.41);
        this.addStation(network.stations.get("clarendon"), 0.47);
        this.addStation(network.stations.get("court house"),0.57);
        this.addStation(network.stations.get("rosslyn"), 0.96);
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
        this.addStation(network.stations.get("potomac ave"), 0.59);
        this.addStation(network.stations.get("stadium-armory"), 0.73);
        this.addStation(network.stations.get("minnesota ave"), 1.95);
        this.addStation(network.stations.get("deanwood"), 0.89);
        this.addStation(network.stations.get("cheverly"), 1.18);
        this.addStation(network.stations.get("landover"), 1.83);
        this.addStation(network.stations.get("new carrollton"), 1.00);

        this.setDestination(network.stations.get("new carrollton"));
    }
}