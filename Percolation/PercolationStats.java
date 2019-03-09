/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    final private double mean, stddev, confidence25, confidence975;


    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new java.lang.IllegalArgumentException("Grid size and trials count must be greater than 0");
        }

        double[] threshold = new double[trials];

        // Evaluate percolation
        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);
            // System.out.println("Created percolator");
            while (!percolation.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                // System.out.println("Opening site (" + row + ", " + col + ")");
                percolation.open(row, col);
            }
            // System.out.println("Percolated!");
            threshold[i] = (double) percolation.numberOfOpenSites() / (double) (n * n);
        }

        mean = StdStats.mean(threshold);
        stddev = StdStats.stddev(threshold);
        double confidence = (1.96 * stddev) / Math.sqrt(trials);
        confidence25 = mean - confidence;
        confidence975 = mean + confidence;
    }

    public double mean() {
        return mean;
    }

    public double stddev() {
        return stddev;
    }

    public double confidenceLo() {
        return confidence25;
    }

    public double confidenceHi() {
        return confidence975;
    }

    public static void main(String[] args) {
        String error = "Invalid number of arguments used when calling PercolationStats";
        if (args.length != 2) { throw new java.lang.IllegalArgumentException(error); }
        int size = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats stats = new PercolationStats(size, trials);

        StdOut.println("mean                    = " + stats.mean());
        StdOut.println("stddev                  = " + stats.stddev());
        StdOut.println("95% confidence interval = [" + stats.confidenceLo() + ", " + stats.confidenceHi() + "]");
    }
}
