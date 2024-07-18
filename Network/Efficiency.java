package Network;

import Network.Station;

public class Efficiency implements Comparable<Efficiency>{
    public Station origin;
    public Station destination;
    public Double efficiency;

    public Efficiency(Station origin, Station destination, Double efficiency) {
        this.origin = origin;
        this.destination = destination;
        this.efficiency = efficiency;
    }

    public String toString() {
        return (new StringBuilder()).append(origin.name).append(" -> ").append(destination.name).toString();
    }

    // negative to invert the ordering for priority queue
    @Override
    public int compareTo(Efficiency o) {
        return -this.efficiency.compareTo(o.efficiency);
    }
}