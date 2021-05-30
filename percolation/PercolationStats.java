import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;


public class PercolationStats {

    private static final double CONFIDENCE_95 = 1.96;

    private double mean, stddev;
    private final int totalTrials;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {

        if (n <= 0 || trials <= 0) throw new IllegalArgumentException("n and trials must be > 0.");

        totalTrials = trials;
        double[] trialResults = new double[trials];


        for (int i = 0; i < trials; i++) {
            Percolation testCase = new Percolation(n);
            while (!testCase.percolates()) {
                int row = StdRandom.uniform(n) + 1;
                int col = StdRandom.uniform(n) + 1;
                testCase.open(row, col);
            }

            int openSites = testCase.numberOfOpenSites();
            double result = (double) openSites / (n * n);

            trialResults[i] = result;
        }
        mean = StdStats.mean(trialResults);
        stddev = StdStats.stddev(trialResults);

    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean - (CONFIDENCE_95 * stddev) / Math.sqrt(totalTrials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean + (CONFIDENCE_95 * stddev) / Math.sqrt(totalTrials);
    }

    // test client (see below)
    public static void main(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("Provide grid side and number of trials.");
        }
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats stats = new PercolationStats(n, trials);
        System.out.printf("mean = %f \n", stats.mean());
        System.out.printf("stddev = %f \n", stats.stddev());

        String cfLevel = "[" + stats.confidenceLo() + ", " + stats.confidenceHi() + "]";

        StdOut.println("95% confidence interval = " + cfLevel);

    }
}
