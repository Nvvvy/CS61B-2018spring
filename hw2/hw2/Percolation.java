package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.HashSet;

public class Percolation {
    private boolean isPercolated;
    private WeightedQuickUnionUF site;
    private int N;
    private int openSites;
    private int[] siteStatus; // 0: locked; 1: open;

    /** create N-by-N grid, with all sites initially blocked */
    public Percolation(int edgeLength) {
        if (edgeLength <= 0) {
            throw new java.lang.IllegalArgumentException("N should be a positive int!");
        }
        this.N = edgeLength;
        // 0 and N*N+1 index item represents water source or whatever
        site = new WeightedQuickUnionUF(N * N + 2);
        siteStatus = new int[N * N + 2];
        openSites = 0;
        isPercolated = false;
    }

    // convert the 2D coordinate of site to the index of WQU obj.
    private int coToIndex(int row, int col) {
        return N * row + col + 1;
    }

    // Returns true if the coordinate is on top
    private boolean onTop(int row, int col) {
        return (row == 0);
    }

    // Returns true if the coordinate is on bottom
    private boolean onBottom(int row, int col) {
        return (row == N - 1);
    }

    private HashSet<Integer> findOpenNeighbour(int row, int col) {
        int up = coToIndex(Math.max(0, row - 1), col);
        int down = coToIndex(Math.min(row + 1, N - 1), col);
        int left = coToIndex(row, Math.max(0, col - 1));
        int right = coToIndex(row, Math.min(col + 1, N - 1));

        HashSet<Integer> neighbours = new HashSet<>();
        neighbours.add(up);
        neighbours.add(down);
        neighbours.add(left);
        neighbours.add(right);
        return neighbours;
    }

    /** open the site (row, col) if it is not open already */
    public void open(int row, int col) {
        if (row >= N || col >= N) {
            throw new java.lang.IndexOutOfBoundsException("Out of Bound");
        }

        int ind = coToIndex(row, col);
        if (siteStatus[ind] == 0) {
            siteStatus[ind] = 1;
            openSites += 1;
        }

        // check if this site is on top or bottom
        if (onTop(row, col)) {
            site.union(0, ind);
        }
        if (onBottom(row, col)) {
            site.union(N * N + 1, ind);
        }

        // union neighbours
        HashSet<Integer> neighbours = findOpenNeighbour(row, col);
        for (int i : neighbours) {
            if (siteStatus[i] == 1) {
                site.union(i, ind);
            }
        }
    }

    /** is the site (row, col) open? */
    public boolean isOpen(int row, int col) {
        if (row >= N || col >= N) {
            throw new java.lang.IndexOutOfBoundsException("Out of Bound");
        } else {
            return siteStatus[coToIndex(row, col)] == 1;
        }
    }

    /** is the site (row, col) full? */
    public boolean isFull(int row, int col) {
        if (row >= N || col >= N) {
            throw new java.lang.IndexOutOfBoundsException("Out of Bound");
        } else {
            int ind = coToIndex(row, col);
            return site.connected(ind, 0);
        }
    }

    /** number of open sites */
    public int numberOfOpenSites() {
        return openSites;
    }

    /** does the system percolate? */
    public boolean percolates() {
        return site.connected(N * N + 1, 0);
    }

    // use for unit testing (not required)
    public static void main(String[] args) {
    }
}
