public class Main {
    public static void main(String[] args) {
        Station rosslyn = new Station("rosslyn", 38.8969, -77.0720);
        Station eastern_market = new Station("eastern market", 38.8844, -76.9958);

        System.out.println(rosslyn.getDistance(eastern_market));
    }
}
