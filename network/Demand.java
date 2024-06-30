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

    public Demand(Station start, Station end, int trips) {
        this.start = start;
        this.end = end;
        this.trips = trips;
    }

    public Demand(Demand d) {
        this.start = d.start;
        this.end = d.end;
        this.trips = d.trips;
    }

    public void addTrip(int i) {
        trips+=i;
    }

    public String toString() {
        return start.name + " to " + end.name + ": " + trips + "\n";
    }
}
