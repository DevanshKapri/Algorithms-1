import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final int n;
    private final WeightedQuickUnionUF unionFindGrid;
    private final WeightedQuickUnionUF isFullGrid;
    private final int virtualTop;
    private final int virtualBottom;
    private int openSites;

    private boolean[][] openGrid;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int a) {

        if (a <= 0) throw new IllegalArgumentException("N must be more than zero");
        n = a;
        unionFindGrid = new WeightedQuickUnionUF(
                n * n + 2); // unionFind ds to include top and bottom virtual classes
        isFullGrid = new WeightedQuickUnionUF(
                n * n + 2);
        virtualTop = n * n; // virtual top class
        virtualBottom = n * n + 1; // virtual bottom class
        openGrid = new boolean[n][n];
        openSites = 0;

        for (int i = 0; i < n; i++) {
            unionFindGrid.union(virtualTop, i);
            isFullGrid.union(virtualTop, i);
            unionFindGrid.union(virtualBottom, ((n * n) - n + i));
        }


    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!validate(row, col)) {
            throw new IllegalArgumentException("Row/column values must be between 1 & n");
        }
        if (isOpen(row, col)) {
            return;
        }
        openSites++;

        openGrid[row - 1][col - 1] = true;

        int index = (n * (row - 1)) + (col - 1);

        if (validate(row, col - 1) && isOpen(row, col - 1)) {
            unionFindGrid.union(index, index - 1);
            isFullGrid.union(index, index - 1);
        }
        if (validate(row, col + 1) && isOpen(row, col + 1)) {
            unionFindGrid.union(index, index + 1);
            isFullGrid.union(index, index + 1);
        }
        if (validate(row - 1, col) && isOpen(row - 1, col)) {
            unionFindGrid.union(index, index - n);
            isFullGrid.union(index, index - n);
        }
        if (validate(row + 1, col) && isOpen(row + 1, col)) {
            unionFindGrid.union(index, index + n);
            isFullGrid.union(index, index + n);
        }

    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        return openGrid[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (openGrid[row - 1][col - 1]) {
            int index = n * (row - 1) + (col - 1);
            return (isFullGrid.find(virtualTop) == isFullGrid.find(index));
        }
        else {
            return false;
        }
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return (unionFindGrid.find(virtualTop) == unionFindGrid.find(virtualBottom));
    }

    // validates site
    private boolean validate(int row, int column) {
        return (!(row > n || row < 1 || column > n || column < 1));
    }
}
