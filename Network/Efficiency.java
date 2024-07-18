package Network;

import Network.Station;

public class Efficiency {
    public Station origin;
    public Station destination;
    public Double efficiency;

    public Efficiency(Station origin, Station destination, Double efficiency) {
        this.origin = origin;
        this.destination = destination;
        this.efficiency = efficiency;
    }
}