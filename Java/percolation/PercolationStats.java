/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {

    private static final double CONFIDENCE = 1.96;
    private final double[] trialResults;
    private final int numTrials;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException("n and number of trials must be positive");

        this.numTrials = trials;
        this.trialResults = new double[numTrials];
        double openSites;

        for (int trial = 0; trial < numTrials; trial++) {
            Percolation perc = new Percolation(n);
            openSites = 0.0;
            while (!perc.percolates()) {
                int i = StdRandom.uniformInt(1, n + 1);
                int j = StdRandom.uniformInt(1, n + 1);
                // if (!perc.isOpen(i, j))
                perc.open(i, j);
            }
            openSites = perc.numberOfOpenSites();
            trialResults[trial] = openSites / (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(trialResults);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(trialResults);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - CONFIDENCE * stddev() / Math.sqrt(numTrials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + CONFIDENCE * stddev() / Math.sqrt(numTrials);
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        Stopwatch time = new Stopwatch(); // Start stopwatch time for CPU to perform percolation stats
        PercolationStats ps = new PercolationStats(n, t);

        String confidence = ps.confidenceLo() + ", " + ps.confidenceHi();
        StdOut.println("mean                    = " + ps.mean());
        StdOut.println("stddev                  = " + ps.stddev());
        StdOut.println("95% confidence interval = [" + confidence + "]");
        StdOut.println("Time: " + time.elapsedTime() + " seconds | n: " + n + " | t: " + t);
    }

}
