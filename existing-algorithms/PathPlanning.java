/*
 * PathPlanning Algorithm (implementation by Ruoxing Yang)
 * 
 * Chao-Lin Liu, Tun-Wen Pai, Chun-Tien Chang, Chang-Ming Hsieh
 * 
 * PathPlanning generates a path between an origin station and
 * destination station in a public transportation network. The 
 * original paper uses a bus network as an example but this 
 * implementation generalizes the algorithm for metro networks, 
 * which are the main focus of this research. The path is 
 * generated taking into account transfers between lines/routes.
 */

public class PathPlanning {

    Network network;

    int[][] connectivityMatrix;

    public PathPlanning(Network network) {
        this.network = network;
        connectivityMatrix = new int[network.lines.size()][network.lines.size()];

        for (int i = 0; i < network.lines.size(); i++) {
            network.lines.get(i).index = i; // self aware line object
            for (int j = 0; j < network.lines.size(); j++) {
                connectivityMatrix[i][j] = network.lines.get(i).commonStations(network.lines.get(j)).size();
                if (i == j) {
                    // the same way Liu, Pai, Chang, Hsieh sets up their algorithm
                    connectivityMatrix[i][j] = 0;
                }
            }
        }
    }

    /*
     * This algorithm prioritizes minimizing the amount of transfers required
     */

    public Path pathPlan(Station origin, Station destination) {

        System.out.println("Path Planning from " + origin.name + " to " + destination.name);
        Path path = new Path();

        // look at this - if no path is found, return NULL
        path.setOrigin(origin);
        path.setDestination(destination);

        path.nTransfers = 0;

        // PathPlanning algorithm

        // step 1 - for completeness, check if origin is destination
        if (origin.equals(destination)) {
            path.lines.add(origin.lines.get(0));
            return path;
        }

        // step 2 - check if the commute can be completed with direct connection
        
        // iterate through each line that origin belongs to
        for (Line line : origin.lines) {
            for (Station station : line.stations) {
                if (destination.equals(station) &&
                K(line, origin) < K(line, destination)) {
                    // generate a path 
                    path.buildPath(network, line, origin, destination);

                    path.sort();
                    path.nTransfers = 0;
                    return path;
                }
            }
        }

        // step 3 - check if the commute can be completed using one transfer

        // use the line connectivity matrix

        /*
         * for each line that belongs to each station, we know that
         * none of these lines overlap otherwise we would have found 
         * a direct connection commute, hence we find look at the 
         * line connectivity matrix to determine if you can transfer
         * from one line to the other line
         */

        for (Line originLine : origin.lines) {
            for (Line destinationLine : destination.lines) {


                // first two conditionals check if the line actually exists in the network
                if (originLine.index > -1 &&
                destinationLine.index > -1 &&
                connectivityMatrix[originLine.index][destinationLine.index] >= 1) {
                    // this indicates the two lines have COMMON STOPS 
                    // and we can take a transfer at any of these common stops
                    // but we have to check K values
                    
                    // check k values in common stations
                    for (Station commonStation : originLine.commonStations(destinationLine)) {
                        // we grab the first common station that satisfies the K
                        // constraint to use as our transfer station
                        
                        if ((K(originLine, origin) < K(originLine, commonStation)) &&
                        (K(destinationLine, commonStation) < K(destinationLine, destination))) {
                            // use commonStation to generate a path 

                            path.buildPath(network, destinationLine, commonStation, destination);
                            path.buildPath(network, originLine, origin, commonStation);

                            path.sort();
                            path.nTransfers = 1;
                            return path;
                        }
                    }
                }
            }
        }

        // step 4 - two transfers

        // use T2 matrix

        int[][] T2 = connectivityMatrixPower(connectivityMatrix, 2);

        for (Line originLine : origin.lines) {
            for (Line destinationLine : destination.lines) {

                // first two conditionals check if the line actually exists in the network
                if (originLine.index > -1 &&
                destinationLine.index > -1 &&
                T2[originLine.index][destinationLine.index] >= 1) {
                    // this indicates the two lines is connected degree 2
                    // and we can take a transfer to some other line and
                    // then transfer again

                    for (Line commonLine : originLine.commonLines(network, destinationLine)) {
                        // s is the first transfer station, t is the second
                        // following paper jargon
                        for (Station s : originLine.commonStations(commonLine)) {
                            for (Station t : commonLine.commonStations(destinationLine)) {
                                if ((K(originLine, origin) < K(originLine, s)) &&
                                (K(commonLine, s) < K(commonLine, t)) &&
                                (K(destinationLine, t) < K(destinationLine, destination))) {
                                    path.buildPath(network, destinationLine, t, destination);
                                    path.buildPath(network, commonLine, s, t);
                                    path.buildPath(network, originLine, origin, s);
                                    path.sort();
                                    path.nTransfers = 2;
                                    return path;
                                }
                            }
                        }
                    }
                }
            }
        }

        // step 5 - three transfers!

        int[][] T3 = connectivityMatrixPower(connectivityMatrix, 3);

        for (Line originLine : origin.lines) {
            for (Line destinationLine : destination.lines) {

                // first two conditionals check if the line actually exists in the network
                if (originLine.index > -1 &&
                destinationLine.index > -1 &&
                T3[originLine.index][destinationLine.index] >= 1) {

                    // for each line we can transfer to from the originLine, apply step 4

                    for (Line firstTransferLine : originLine.transferLines(network)) {
                        for (Line secondTransferLine : firstTransferLine.commonLines(network, destinationLine)) {
                            for (Station x : originLine.commonStations(firstTransferLine)) {
                                for (Station y : firstTransferLine.commonStations(secondTransferLine)) {
                                    for (Station z : secondTransferLine.commonStations(destinationLine)) {
                                        if ((K(originLine, origin) < K(originLine, x)) &&
                                        (K(firstTransferLine, x) < K(firstTransferLine, y)) &&
                                        (K(secondTransferLine, y) < K(secondTransferLine, z)) &&
                                        (K(destinationLine, z) < K(destinationLine, destination))) {
                                            path.buildPath(network, destinationLine, z, destination);
                                            path.buildPath(network, secondTransferLine, y, z);
                                            path.buildPath(network, firstTransferLine, x, y);
                                            path.buildPath(network, originLine, origin, x);
                                            path.sort();
                                            path.nTransfers = 3;
                                            return path;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // no path was found
        path.nTransfers = 0;
        return null;
    }

    /*
     * THIS FUNCTION IS 0 INDEXED
     * 
     * represents which station is this station in the order of stations in the line
     */
    public int K(Line line, Station station) {
        
        int i = 0;

        for (Station s : line.stations) {
            if (s.name.equals(station.name)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    /*
     * power functions generated with help from chatgpt
     * 
     * https://chatgpt.com/share/ce4a0433-823f-4983-9f9d-a7d9070476e8
     */

    public int[][] matrixPower(int[][] matrix, int power) {
        int n = matrix.length;
        int[][] result = new int[n][n];

        // Initialize result as the identity matrix
        for (int i = 0; i < n; i++) {
            result[i][i] = 1;
        }

        int[][] base = matrix;

        while (power > 0) {
            if ((power & 1) == 1) {
                result = multiplyMatrix(result, base);
            }
            base = multiplyMatrix(base, base);
            power >>= 1;
        }

        return result;
    }

    /*
     * special function for connectivity matrix which takes
     * into account the K value of the station-line pair
     */

    public int[][] connectivityMatrixPower(int[][] matrix, int power) {
        int n = matrix.length;
        int[][] result = new int[n][n];

        // Initialize result as the identity matrix
        for (int i = 0; i < n; i++) {
            result[i][i] = 1;
        }

        int[][] base = matrix;

        while (power > 0) {
            if ((power & 1) == 1) {
                result = multiplyMatrix(result, base);
            }
            base = multiplyMatrix(base, base);
            power >>= 1;
        }

        return result;
    }

    public int[][] multiplyMatrix(int[][] a, int[][] b) {
        int n = a.length;
        int[][] result = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result[i][j] = 0;
                for (int k = 0; k < n; k++) {
                    result[i][j] += a[i][k] * b[k][j];
                }
            }
        }

        return result;
    }

    public void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int val : row) {
                System.out.print(val + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        WMATA WMATA = new WMATA();

        PathPlanning pp = new PathPlanning(WMATA.WMATA);

        Path path = pp.pathPlan(WMATA.WMATA.getStation("glenmont"), WMATA.WMATA.getStation("king street"));
        System.out.println(path);
    }
}
