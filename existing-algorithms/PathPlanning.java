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
        connectivityMatrix = new int[network.nLines][network.nLines];

        for (int i = 0; i < network.nLines; i++) {
            for (int j = 0; j < network.nLines; j++) {
                connectivityMatrix[i][j] = network.lines.get(i).commonStations(network.lines.get(j)).size();
                if (i == j) {
                    // the same way Liu, Pai, Chang, Hsieh sets up their algorithm
                    connectivityMatrix[i][j] = 0;
                }
            }
        }
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

    // TODO : WORK ON THIS

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

        PathPlanning pp = new PathPlanning(null);

        int[][] matrix = {
            {0, 1, 0},
            {0, 0, 2},
            {1, 0, 0}
        };

        int[][] T2 = pp.matrixPower(matrix, 3);
        pp.printMatrix(T2);
    }
}
