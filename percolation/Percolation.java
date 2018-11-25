import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * <p>By convention, the row and column indices are integers between <code>1</code> and
 * <code>n</code>, where <code>(1,
 * 1)</code> is the upper-left site.</p>
 *
 * <p>Throw a <code>java.lang.IllegalArgumentException</code> if any argument to
 * <code>open()</code>, <code>isOpen()</code>, or <code>isFull()</code> is outside its prescribed
 * range. The constructor should throw a
 * <code>java.lang.IllegalArgumentException</code> if <code>n ≤ 0</code>.</p>
 */
public class Percolation {

    /**
     * Index of the virtual top site
     */
    private static final int VIRTUAL_TOP_SITE_INDEX = 0;

    /**
     * bit flag indicating that the site is open
     */
    private static final int SITE_OPEN = 1;

    /**
     * bit flag indicationg that the site is connected to the top
     */
    private static final int SITE_CONNECTED_TO_TOP = 2;

    /**
     * bit flag indicating that the site is connecte to the bottom
     */
    private static final int SITE_CONNECTED_TO_BOTTOM = 4;

    /**
     * Connected to the top and bottom - means percolation
     */
    private static final int CONNECTED_TO_BOTH_TOP_AND_BOTTOM = SITE_CONNECTED_TO_TOP
            | SITE_CONNECTED_TO_BOTTOM;

    /**
     * Becomes true when the system percolates
     */
    private boolean percolation = false;

    /**
     * Size of the grid both row and column
     */
    private final int gridSize;

    /**
     * Available flags:
     * <ul>
     * <li>SITE_CONNECTED_TO_BOTTOM = 4</li>
     * <li>SITE_CONNECTED_TO_TOP = 2</li>
     * <li>SITE_OPEN = 1</li>
     * </ul>
     */
    private byte[][] grid;

    /**
     * Number of open sites in the grid
     */
    private int numberOfOpenSites = 0;

    /**
     * The union-find algorithm implementation
     */
    private final WeightedQuickUnionUF unionFind;

    /**
     * Create n-by-n grid, with all sites blocked
     *
     * @param n grid dimension
     * @throws IllegalArgumentException if n < 1
     */
    public Percolation(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("grid size should be > 0");
        }
        this.gridSize = n;
        this.grid = new byte[n][n];
        this.unionFind = new WeightedQuickUnionUF(gridSize * gridSize + 2);
    }

    /**
     * Returns the index of site for the union-find
     *
     * @param row the row index (1-gridSize)
     * @param col the column index (1-gridSize)
     * @return the index of the site for the union-find
     */
    private int getSiteIndex(int row, int col) {

        return (row - 1) * gridSize + (col - 1) + 1;
    }

    /**
     * Throws an {@link IllegalArgumentException} if the row or col is less than 1 or greater than
     * gridSize
     *
     * @param row the row coordinate
     * @param col the column coordinate
     */
    private void checkCoordinates(int row, int col) {
        if (!isValidCoordinate(row, col)) {
            throw new IllegalArgumentException("Invalid coordinate: (" + row + ", " + col
                                                       + ") The row and col should be >= 1 and <= grid dimension");
        }
    }

    /**
     * is site (row, col) full?
     *
     * @param row row index
     * @param col column index
     * @return true if the site is full - the fluid can penetrate from top to bottom, false if the
     * site is non null.
     */
    public boolean isFull(int row, int col) {
        checkCoordinates(row, col);
        return unionFind.connected(VIRTUAL_TOP_SITE_INDEX, getSiteIndex(row, col));
    }

    /**
     * Returns true if (row,col) is valid coordinate
     *
     * @param row the row index (1-gridSize)
     * @param col the column index (1-gridSize)
     * @return true if the coordinates are valid
     */
    private boolean isValidCoordinate(int row, int col) {
        return row >= 1 && row <= gridSize && col >= 1 && col <= gridSize;
    }

    /**
     * Returns the flags for the site
     *
     * @param row row index (1-gridSize)
     * @param col column index (1-gridSize)
     * @return the flag
     */
    private byte getGridValue(int row, int col) {
        if (!isValidCoordinate(row, col)) {
            return SITE_OPEN;
        }
        else {
            return grid[row - 1][col - 1];
        }
    }

    /**
     * Returns true if the site (row, col) open
     *
     * @param row row index (1-gridSize)
     * @param col column index (1-gridSize)
     * @return true if the site is open (non blocked), false if the site is blocked
     */
    public boolean isOpen(int row, int col) {
        checkCoordinates(row, col);
        return (grid[row - 1][col - 1] & SITE_OPEN) == SITE_OPEN;
    }


    /**
     * Returns true if the system percolate?
     *
     * @return true, if the system percolates
     */
    public boolean percolates() {
        return percolation;
    }

    /**
     * Returns the flag of the root of the site (row,col)
     *
     * @param row the row index
     * @param col the column index
     * @return the flag of the root of the site
     */
    private int getRootFlag(int row, int col) {

        int siteIndex = getSiteIndex(row, col);

        int siteRootIndex = unionFind.find(siteIndex);

        int rootRow = (siteRootIndex - 1) / gridSize + 1;
        int rootCol = (siteRootIndex - 1) % gridSize + 1;

        int result = grid[rootRow - 1][rootCol - 1];

        return result;
    }

    /**
     * Updates (by OR-ing the old and new value) the root of the site (row,col) with the given
     * flags
     *
     * @param row  the row index
     * @param col  the column index
     * @param flag the flag
     * @return the new value (old | flag)
     */
    private int updateRoot(int row, int col, int flag) {
        int siteIndex = getSiteIndex(row, col);

        int siteRootIndex = unionFind.find(siteIndex);

        int rootRow = (siteRootIndex - 1) / gridSize + 1;
        int rootCol = (siteRootIndex - 1) % gridSize + 1;

        grid[rootRow - 1][rootCol - 1] |= flag;

        int updatedValue = grid[rootRow - 1][rootCol - 1];

        return updatedValue;
    }

    /**
     * open site (row, col) if it is not open already
     *
     * @param row row index (1-gridSize)
     * @param col column index (1-gridSize)
     */
    public void open(int row, int col) {
        checkCoordinates(row, col);

        if (isOpen(row, col)) {
            return;
        }

        grid[row - 1][col - 1] |= SITE_OPEN;

        this.numberOfOpenSites++;

        int northOpen = getGridValue(row - 1, col) & SITE_OPEN;
        int southOpen = getGridValue(row + 1, col) & SITE_OPEN;
        int westOpen = getGridValue(row, col - 1) & SITE_OPEN;
        int eastOpen = getGridValue(row, col + 1) & SITE_OPEN;

        int centerSiteIndex = getSiteIndex(row, col);

        int northRootFlag = 0;
        int southRootFlag = 0;
        int eastRootFlag = 0;
        int westRootFlag = 0;
        int centerFlag = 0;

        // last row connects to virtual bottom
        if (row == gridSize) {
            centerFlag |= SITE_CONNECTED_TO_BOTTOM;
        }
        // connect opened site with virtual top
        if (row == 1) {

            centerFlag |= SITE_CONNECTED_TO_TOP;

            unionFind.union(centerSiteIndex, VIRTUAL_TOP_SITE_INDEX);
        }

        // NORTH connection
        if (isValidCoordinate(row - 1, col) && northOpen == SITE_OPEN) {
            int northSiteIndex = getSiteIndex(row - 1, col);
            northRootFlag = getRootFlag(row - 1, col);

            // connect to the north neighbour
            unionFind.union(centerSiteIndex, northSiteIndex);
        }

        // SOUTH connection
        if (isValidCoordinate(row + 1, col) && southOpen == SITE_OPEN) {
            int southSiteIndex = getSiteIndex(row + 1, col);
            southRootFlag = getRootFlag(row + 1, col);

            unionFind.union(centerSiteIndex, southSiteIndex);
        }

        // WEST
        if (isValidCoordinate(row, col - 1) && westOpen == SITE_OPEN) {
            int westSiteIndex = getSiteIndex(row, col - 1);
            westRootFlag = getRootFlag(row, col - 1);

            unionFind.union(centerSiteIndex, westSiteIndex);
        }

        // EAST
        if (isValidCoordinate(row, col + 1) && eastOpen == SITE_OPEN) {
            int eastSiteIndex = getSiteIndex(row, col + 1);
            eastRootFlag = getRootFlag(row, col + 1);

            unionFind.union(centerSiteIndex, eastSiteIndex);
        }

        int unionFlag = centerFlag | northRootFlag | southRootFlag | westRootFlag | eastRootFlag;

        unionFlag = updateRoot(row, col, unionFlag);

        if ((unionFlag & CONNECTED_TO_BOTH_TOP_AND_BOTTOM) == CONNECTED_TO_BOTH_TOP_AND_BOTTOM) {
            percolation = true;
        }

    }

    /**
     * Returns the number of open sites
     *
     * @return the number of open sites
     */
    public int numberOfOpenSites() {
        return this.numberOfOpenSites;
    }
}
