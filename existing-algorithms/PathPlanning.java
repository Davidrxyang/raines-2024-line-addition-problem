public class PathPlanning {

    Network network;

    public PathPlanning(Network network) {
        this.network = network;
    }

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
