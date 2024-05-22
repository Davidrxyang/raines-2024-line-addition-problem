/*
 * an individual metro station in the network
 */

public class Station extends Object {

    String name; // full name of the station

    /*
     * geographical location of the station is stored using long/lat coordinates
     * along with an address string
     */

    Double longitude; 
    Double latitude;
    String address;

    String comment; 

    public Station(String name, Double longitude, Double latitude) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = "";
        this.comment = "";
    }

    /*
     * calculates the geographical distance in miles between two stations
     */
    
    public Double getDistance(Station station) {

        double EARTH_RADIUS = 3958.8; // for calculation

        // Convert latitude and longitude from degrees to radians
        Double lat1 = Math.toRadians(this.latitude);
        Double lon1 = Math.toRadians(this.longitude);
        Double lat2 = Math.toRadians(station.latitude);
        Double lon2 = Math.toRadians(station.longitude);

        // Haversine formula
        double dlat = lat2 - lat1;
        double dlon = lon2 - lon1;
        double a = Math.sin(dlat / 2) * Math.sin(dlat / 2) +
                   Math.cos(lat1) * Math.cos(lat2) *
                   Math.sin(dlon / 2) * Math.sin(dlon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Distance in miles
        return EARTH_RADIUS * c;
    }
}

