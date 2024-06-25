import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        WMATA WMATA = new WMATA();

        PathPlanning pp = new PathPlanning(WMATA.WMATA);
        Path bethesdaToAnacostia = pp.pathPlan(WMATA.WMATA.getStation("bethesda"), WMATA.WMATA.getStation("anacostia"));
        System.out.println(bethesdaToAnacostia);
    }
}
