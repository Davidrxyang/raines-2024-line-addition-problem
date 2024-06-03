public class PathPlanning {

    Network network;

    public PathPlanning(Network network) {
        this.network = network;
    }

    public int[][] Multiply(int[][] matrix) {

        int n = matrix.length;
        int[][] T = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                T[i][j] = 0;
                for (int k = 0; k < n; k++) {
                    T[i][j] += matrix[i][k] * matrix[k][j];
                }
            }
        }

        return T;
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

        int[][] T2 = pp.Multiply(matrix);
        pp.printMatrix(T2);
    }
}
