/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

public class Percolation {
    private int[][] percolationSystem;
    private int[][] treeSizes;
    final private int size;
    private int numOpenSites;

    private int topVirtualSite = -1;
    private int bottomVirtualSite = -1;

    public Percolation(int n) {
        if (n < 1) {
            throw new java.lang.IllegalArgumentException("Grid must be at least 1 x 1");
        }
        size = n;

        percolationSystem = new int[n][n];
        // Fill with invalid values
        for (int[] row : percolationSystem) {
            for (int i = 0; i < row.length; i++) {
                row[i] = -1;
            }
        }

        // Initialize sizes to 1
        treeSizes = new int[n][n];
        for (int[] row : treeSizes) {
            for (int i = 0; i < row.length; i++) {
                row[i] = 1;
            }
        }

        numOpenSites = 0;
    }

    private int make1D(int row, int col) {
        return row * size + col;
    }

    private int findRoot(int row, int col) {
        int root = percolationSystem[row][col];
        while (root != make1D(row, col)) {
            row = root / size;
            col = root % size;
            root = percolationSystem[row][col];
        }

        return root;
    }

    private void connectSites(int target, int origin) {
        int targetRow = target / size;
        int targetCol = target % size;
        int originRow = origin / size;
        int originCol = origin % size;

        int originRoot = findRoot(originRow, originCol);
        int targetRoot = findRoot(targetRow, targetCol);

        int originTreeSize = treeSizes[originRoot / size][originRoot % size];
        int targetTreeSize = treeSizes[targetRoot / size][targetRoot % size];

        if (targetTreeSize > originTreeSize) {
            percolationSystem[originRoot / size][originRoot % size] = targetRoot;
            treeSizes[targetRoot / size][targetRoot % size] += originTreeSize;
        } else {
            percolationSystem[targetRoot / size][targetRoot % size] = originRoot;
            treeSizes[originRoot / size][originRoot % size] += targetTreeSize;
        }
    }

    /************ Public functions **********/

    public void open(int row, int col) {
        // Why tf do they use 1-indexed arrays? This isn't Matlab
        row--;
        col--;
        if (row < 0 || row >= size || col < 0 || col >= size) {
            throw new java.lang.IllegalArgumentException("Access out of bounds");
        }

        // if the site is already open, don't do anything
        if (percolationSystem[row][col] != -1) {
            return;
        }

        // By default, set its root to its own index
        percolationSystem[row][col] = make1D(row, col);
        // Check row above
        if (row == 0) {
            // Initialize top virtual site if not already initialized
            if (topVirtualSite == -1) {
                int root = findRoot(row, col);
                topVirtualSite = percolationSystem[root / size][root % size];
                treeSizes[root / size][root % size] += 1;
            } else {
                // Connect this site with the parent of the virtual site
                connectSites(topVirtualSite, percolationSystem[row][col]);
            }
        } else if (isOpen((row + 1) - 1, (col + 1))) { // have to do this insane indexing because ARRAYS SHOULD START AT 1
            connectSites(percolationSystem[row - 1][col], percolationSystem[row][col]);
        }

        // Bottom row
        if (row == size - 1) {
            if (bottomVirtualSite == -1) {
                int root = findRoot(row, col);
                bottomVirtualSite = percolationSystem[root / size][root % size];
                treeSizes[root / size][root % size] += 1;
            } else {
                connectSites(bottomVirtualSite, percolationSystem[row][col]);
            }
        } else if (isOpen((row + 1) + 1, (col + 1))) {
            connectSites(percolationSystem[row + 1][col], percolationSystem[row][col]);
        }

        // left
        if (col > 0 && isOpen((row + 1), (col + 1) - 1)) {
            connectSites(percolationSystem[row][col - 1], percolationSystem[row][col]);
        }

        // right
        if (col < size - 1 && isOpen((row + 1), (col + 1) + 1)) {
            connectSites(percolationSystem[row][col + 1], percolationSystem[row][col]);
        }

        numOpenSites++;
    }

    public boolean isOpen(int row, int col) {
        // Why tf do they use 1-indexed arrays? This isn't Matlab
        row--;
        col--;
        if (row < 0 || row >= size || col < 0 || col >= size) {
            throw new java.lang.IllegalArgumentException("Access out of bounds");
        }

        return percolationSystem[row][col] != -1;
    }

    public boolean isFull(int row, int col) {
        // Why tf do they use 1-indexed arrays? This isn't Matlab
        row--;
        col--;
        if (row < 0 || row >= size || col < 0 || col >= size) {
            throw new java.lang.IllegalArgumentException("Access out of bounds");
        }

        if (percolationSystem[row][col] == -1 || topVirtualSite == -1) {
            return false;
        } else {
            return findRoot(row, col) == findRoot(topVirtualSite / size, topVirtualSite % size);
        }
    }

    public int numberOfOpenSites() {
        return numOpenSites;
    }

    public boolean percolates() {
        if (topVirtualSite == -1 || bottomVirtualSite == -1) {
            // Not connected
            return false;
        } else {
            return findRoot(topVirtualSite / size, topVirtualSite % size) ==
                    findRoot(bottomVirtualSite / size, bottomVirtualSite % size);
        }
    }

    /************ Checker *************/
    // public void printSystem() {
    //     for (int i = 0; i < size; i++) {
    //         System.out.print("[ ");
    //         for (int j = 0; j < size; j++) {
    //             System.out.print(percolationSystem[i][j] + " ");
    //         }
    //         System.out.println("]");
    //     }
    //
    //     System.out.println("Top virtual site: " + topVirtualSite + "; bottom virtual site: " + bottomVirtualSite);
    //     System.out.println(numberOfOpenSites() + " sites open");
    //     System.out.println("Percolates: " + percolates());
    // }
    // public static void main(String[] args) {
    //     // Create new percolation
    //     Percolation perc = new Percolation(8);
    //
    //     perc.open(1, 3);
    //     perc.open(2, 6);
    //     perc.open(3, 3);
    //     perc.open(4, 6);
    //     perc.open(3, 2);
    //     perc.open(5, 6);
    //     perc.open(7, 6);
    //     perc.open(4, 7);
    //     perc.open(3, 1);
    //     perc.open(7, 8);
    //     perc.open(2, 7);
    //     perc.open(2, 1);
    //     perc.open(4, 3);
    //     perc.open(7, 1);
    //     perc.open(6, 8);
    //     // System.out.print(perc.isOpen(6, 8));
    //     // perc.printSystem();
    // }
}
