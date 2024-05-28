/*
 * the demand from one station to another
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

    public void addTrip(int i) {
        trips+=i;
    }

    public String toString() {
        return start.name + " to " + end.name + ": " + trips + "\n";
    }
}
