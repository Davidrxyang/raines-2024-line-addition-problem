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
}

