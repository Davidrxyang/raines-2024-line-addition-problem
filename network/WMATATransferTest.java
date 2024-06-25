public class WMATATransferTest {
    public double D0;
    public double D01;
    public double totalTrips;
    public DemandSet l;
    public Network R;

    public WMATATransferTest(DemandSet l, Network R) {
        this.l = l;
        this.R = R;
        totalTrips = l.totalTrips();
        updateD0();
    }

    public void updateD0() {
        // remove all trips that are covered by one route;
        deleteVertices();
        int oneTransfer = 0;
        for (Demand d : l.trips) {
            for (Line r1 : R.lines) {
                boolean found = false;
                for (Line r2: R.lines) {
                    if (r1.commonStations(r2).size() > 0) {
                        if (r1.stations.contains(d.start) && r2.stations.contains(d.end)
                        || r1.stations.contains(d.end) && r2.stations.contains(d.start)) {
                            oneTransfer += d.trips;
                            found = true;
                            break;
                        }
                    }
                }
                if (found) {
                    break;
                }
            }
        }

        int noTransfers = (int) totalTrips - l.totalTrips();
        D0 = noTransfers / totalTrips;
        D01 = (noTransfers + oneTransfer) / totalTrips;
    }

    public void deleteVertices() {
        for (Line line : R.lines) {
            for (int i = 0; i < l.trips.size(); i++) {
                Demand t = l.trips.get(i);
                // if both start and end of a demand (trip) is on the same line,
                // remove that from the demand matrix (demand set)
                if (line.stations.contains(t.start) && line.stations.contains(t.end)) {
                    l.trips.remove(t);
                    i--;
                }
            }
        }
    }

    public String toString() {
        return "D0: " + D0 + "\nD01: " + D01;
    }

    public static void main(String[] args) {
        WMATA wmata = new WMATA();
        DemandSet demandSet = new DemandSet();
        demandSet.loadTrips("network/data.csv", wmata.WMATA);
        WMATATransferTest test = new WMATATransferTest(demandSet, wmata.WMATA);
        System.out.println(test);
    }
}
