import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private static final int TOP = 0;
    private final boolean[][] grid; // n-by-n grid declaration
    private int openSites; // Number of open sites declaration
    private final int size;
    private final int bottom;
    private final WeightedQuickUnionUF uf; // Union-Find data structure declaration
    private final WeightedQuickUnionUF ufFull;


    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Size n must be greater than 0");
        }

        this.size = n;
        this.grid = new boolean[size][size]; // Creates boolean 2-D array grid of size n-by-n
        this.bottom = size * size + 1; // Bottom is one below/more than the n-by-n grid

        this.uf = new WeightedQuickUnionUF(size * size + 2); // Sends n to create n-by-n UF grid with virtual top and bottom
        this.ufFull = new WeightedQuickUnionUF(size * size + 1); // ditto ^, but just virtual top so checking for fulls doesn't bug out
        this.openSites = 0;

        // Populates the whole grid as closed (false) in n^2 time
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                grid[row][col] = false;
            }
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        exceptionHandling(row, col, "open()");

        if (!isOpen(row, col)) {
            grid[row - 1][col - 1] = true; // Open the specified site with 0-based-indexing in mind (hence why row -1 and col - 1)
            openSites++;
        }
        else return;

        // Edge case top, union any opened top row site to the TOP point to be able to check for percolation and if it's full
        if (row == 1) {
            uf.union(getUFIndex(row, col), TOP);
            ufFull.union(getUFIndex(row, col), TOP);
        }

        // Edge case bottom, union any opened bottom row sire to the bottom point for same reasons ^
        if (row == size)
            uf.union(getUFIndex(row, col), bottom);

        // Middle cases (any except top and bottom row), check for neighbouring open sites and union with them, avoiding out of bounds index
        if (row > 1 && isOpen(row - 1, col)) {
            uf.union(getUFIndex(row, col), getUFIndex(row - 1, col));
            ufFull.union(getUFIndex(row, col), getUFIndex(row - 1, col));
        }

        if (row < size && isOpen(row + 1, col)) {
            uf.union(getUFIndex(row, col), getUFIndex(row + 1, col));
            ufFull.union(getUFIndex(row, col), getUFIndex(row + 1, col));
        }

        if (col > 1 && isOpen(row, col - 1)) {
            uf.union(getUFIndex(row, col), getUFIndex(row, col - 1));
            ufFull.union(getUFIndex(row, col), getUFIndex(row, col - 1));
        }

        if (col < size && isOpen(row, col + 1)) {
            uf.union(getUFIndex(row, col), getUFIndex(row, col + 1));
            ufFull.union(getUFIndex(row, col), getUFIndex(row, col + 1));
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        exceptionHandling(row, col, "isOpen()");
        return grid[row - 1][col - 1]; // Grid zero-based-indexing, hence why row - 1 and col - 1
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        exceptionHandling(row, col, "isFull()");
        return ufFull.find(TOP) == ufFull.find(getUFIndex(row, col)); // Different way of writing the connected method since it's deprecated
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.find(TOP) == uf.find(bottom);
    }

    // Gets the index of the Union-Find structure relative to the grid (1,1 on grid will be 1 on Union-Find for example)
    private int getUFIndex(int row, int col) {
        return size * (row - 1) + col;
    }

    private void exceptionHandling(int row, int col, String operation) {
        if (row < 1 || col < 1)
            throw new IllegalArgumentException("Row: " + row + " is less than 1 on grid of size: " + grid.length + " in operation: " + operation);
        if (row > size || col > size)
            throw new IllegalArgumentException("Row: " + row + " is greater than size on grid of size: " + grid.length + " in operation: " + operation);
    }

    // test client (optional)
    public static void main(String[] args) {

    }
}
