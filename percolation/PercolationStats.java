import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double CONST_1_96 = 1.96;
    private final double trials;
    private final int gridSize;
    private final int numberOfAllSites;

    private double cachedmean = 0;
    private double cachedStddev = 0;
    private double cachedConfidenceLow = 0;
    private double cachedConfidenceHigh = 0;

    private double[] openedSitesWhenPercolatedList;

    /**
     * perform trials independent experiments on an n-by-n grid The constructor should throw a
     * java.lang.IllegalArgumentException if either n ≤ 0 or trials ≤ 0.
     *
     * @param n      grid size (both rows and columns)
     * @param trials number of trials
     */
    public PercolationStats(int n, int trials) {
        if (n < 1 || trials < 1) {
            throw new IllegalArgumentException("n and trials should be greater than 0");
        }
        this.gridSize = n;
        this.trials = trials;
        openedSitesWhenPercolatedList = new double[trials];
        this.numberOfAllSites = n * n;
        performExperiments();
    }

    public double mean() {
        if (cachedmean == 0) {
            cachedmean = StdStats.mean(openedSitesWhenPercolatedList);
        }
        return cachedmean;
    }


    // sample standard deviation of percolation threshold
    public double stddev() {
        if (cachedStddev == 0) {
            cachedStddev = StdStats.stddev(openedSitesWhenPercolatedList);
        }
        return cachedStddev;
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        if (cachedConfidenceLow == 0) {
            cachedConfidenceLow = mean() - ((CONST_1_96 * stddev()) / Math.sqrt(this.trials));
        }
        return cachedConfidenceLow;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        if (cachedConfidenceHigh == 0) {
            cachedConfidenceHigh = mean() + ((CONST_1_96 * stddev()) / Math.sqrt(this.trials));
        }
        return cachedConfidenceHigh;
    }

    private int simulate() {
        Percolation percolation = new Percolation(gridSize);
        int row;
        int col;
        while (!percolation.percolates()) {

            // find a pair which is blocked
            do {
                row = StdRandom.uniform(1, this.gridSize + 1);
                col = StdRandom.uniform(1, this.gridSize + 1);
            } while (percolation.isOpen(row, col));

            percolation.open(row, col);
        }
        return percolation.numberOfOpenSites();
    }

    private void performExperiments() {
        for (int i = 0; i < this.trials; i++) {

            int openedSites = simulate();
            double fractionOfOpenedSites = (double) openedSites / (double) numberOfAllSites;
            openedSitesWhenPercolatedList[i] = fractionOfOpenedSites;
        }
    }

    // test client (described below)
    public static void main(String[] args) {
        int gridSize = Integer.parseInt(args[0]);
        int experiments = Integer.parseInt(args[1]);
        PercolationStats percolationStats = new PercolationStats(gridSize, experiments);
        StdOut.println("mean                    = " + percolationStats.mean());
        StdOut.println("stddev                  = " + percolationStats.stddev());
        StdOut.println("95% confidence interval = [" + percolationStats.confidenceLo() + ", "
                               + percolationStats.confidenceHi() + "]");
    }
}
