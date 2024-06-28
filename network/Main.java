import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        WMATA WMATA = new WMATA();

        PathPlanning pp = new PathPlanning(WMATA.WMATA);
        Path path = pp.pathPlan(WMATA.WMATA.getStation("glenmont"), WMATA.WMATA.getStation("king street"));
        System.out.println(path);
    }
}
