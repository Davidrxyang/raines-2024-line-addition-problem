/*
 * a trip through the network
 * in the form of a start and end station
 */

public class Demand {
    public Station start;
    public Station end;
    public int trips;

    public Demand(Station start, Station end) {
        this.start = start;
        this.end = end;
        this.trips = 0;
    }

    public void addTrip() {
        trips++;
    }
}
