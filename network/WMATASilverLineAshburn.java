public class WMATASilverLineAshburn extends Line {

    public WMATASilverLineAshburn(Network network) {

        this.name = "silver line ashburn -> downtown largo";


        this.addStation(network.stations.get("ashburn"), null);
        this.addStation(network.stations.get("loudoun gateway"), 1.92);
        this.addStation(network.stations.get("washington dulles international airport"), 2.85);
        this.addStation(network.stations.get("innovation center"), 2.10);
        this.addStation(network.stations.get("herndon"), 1.77);
        this.addStation(network.stations.get("reston town center"), 1.35);
        this.addStation(network.stations.get("wiehle"), 1.12);
        this.addStation(network.stations.get("spring hill"), 5.84);
        this.addStation(network.stations.get("greensboro"), 0.71);
        this.addStation(network.stations.get("tysons corner"), 0.70);
        this.addStation(network.stations.get("mclean"), 0.66);
        this.addStation(network.stations.get("east falls church"), 4.18);
        this.addStation(network.stations.get("ballston"), 2.65);
        this.addStation(network.stations.get("virginia square-gmu"), 0.41);
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
        this.addStation(network.stations.get("potomac avenue"), 0.59);
        this.addStation(network.stations.get("stadium-armory"), 0.73);
        this.addStation(network.stations.get("benning road"), 2.48);
        this.addStation(network.stations.get("capitol heights"), 1.37);
        this.addStation(network.stations.get("addison road"), 0.92);
        this.addStation(network.stations.get("morgan blvd"), 1.60);
        this.addStation(network.stations.get("downtown largo"), 1.33);

        this.setDestination(network.stations.get("downtown largo"));

        this.reverse("silver line downtown largo -> ashburn");

    }
}
