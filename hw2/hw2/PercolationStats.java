package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {
    private double[] stats;
    private final double C = 1.96;
    private final int Times;

    /** perform T independent experiments on an N-by-N grid */
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new java.lang.IllegalArgumentException("bot N and should be positive int!");
        }
        Times = T;
        stats = new double[T];
        for (int i = 0; i < T; i += 1) {
            Percolation per = pf.make(N);
            while (!per.percolates()) {
                int randX = StdRandom.uniform(N);
                int randY = StdRandom.uniform(N);
                per.open(randX, randY);
            }
            stats[i] = ((double) per.numberOfOpenSites()) / (N * N);
        }
    }

    /** sample mean of percolation threshold */
    public double mean() {
        return StdStats.mean(stats);
    }

    /** sample standard deviation of percolation threshold */
    public double stddev() {
        return StdStats.stddev(stats);
    }

    /** low endpoint of 95% confidence interval */
    public double confidenceLow() {
        return mean() - C * stddev() / Math.sqrt(Times);
    }

    /** high endpoint of 95% confidence interval */
    public double confidenceHigh() {
        return mean() + C * stddev() / Math.sqrt(Times);
    }
}
