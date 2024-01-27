package hw4.puzzle;

import edu.princeton.cs.algs4.Queue;

public class Board implements WorldState{
    private int[][] board;
    private final int BLANK = 0;
    private final int N;

    /** Constructs a board from an N-by-N array of tiles where
     tiles[i][j] = tile at row i, column j */
    public Board(int[][] tiles) {
        N = tiles.length;
        board = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                board[i][j] = tiles[i][j];
            }
        }
    }

    /** Returns value of tile at row i, column j (or 0 if blank) */
    public int tileAt(int i, int j) {
        if (i < 0 || i > size() - 1 || j < 0 || j > size() - 1) {
            throw new IndexOutOfBoundsException();
        }
        return board[i][j];
    }

    /** Returns the board size N */
    public int size() {
        return N;
    }

    /** Returns the neighbors of the current board
     * @author josh.hug
     * @source https://joshh.ug/neighbors.html */
    @Override
    public Iterable<WorldState> neighbors() {
        Queue<WorldState> neighbors = new Queue<>();
        int N = size();
        int blankRow = -1;
        int blankCol = -1;
        /* find the blank coordinate (x,y) */
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tileAt(i, j) == BLANK) {
                    blankRow = i;
                    blankCol = j;
                }
            }
        }

        /* copy the board to maintain immutability */
        int[][] copyBoard = new int[N][N];
        for (int x = 0; x < N; x++) {
            for (int y = 0; y < N; y++) {
                copyBoard[x][y] = tileAt(x, y);
            }
        }

        /* enqueue all possible board after move the BLANK tile */
        for (int row = 0; row < N; row++) {
            for (int column = 0; column < N; column++) {
                if (Math.abs(-blankRow + row) + Math.abs(column - blankCol) - 1 == 0) {
                    copyBoard[blankRow][blankCol] = copyBoard[row][column];
                    copyBoard[row][column] = BLANK;
                    Board neighbor = new Board(copyBoard);
                    neighbors.enqueue(neighbor);
                    copyBoard[row][column] = copyBoard[blankRow][blankCol];
                    copyBoard[blankRow][blankCol] = BLANK;
                }
            }
        }
        return neighbors;
    }

    /** Hamming estimate described below */
    public int hamming() {
        int count = 0;
        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                if (tileAt(row, col) != row * N + col +1) {
                    count += 1;
                }
            }
        }
        return count - 1;
    }

    /** Manhattan estimate described below */
    public int manhattan() {
        int totalDis = 0;
        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                if (tileAt(row, col) != BLANK) {
                    totalDis += tileDis(row, col);
                }
            }
        }
        return totalDis;
    }

    /* A helper method to calculate the move distance
     betweent current position and target position */
    private int tileDis(int row, int col) {
        int targetRow = (tileAt(row, col) - 1) / N;
        int targetCol = (tileAt(row, col) - 1) % N;
        return Math.abs(row - targetRow) + Math.abs(col - targetCol);
    }

    /** Estimated distance to goal. This method should
     simply return the results of manhattan() when submitted to
     Gradescope. */
    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    /** Returns true if this board's tile values are the same
     position as y's */
    @Override
    public boolean equals(Object y) {
        if (!(y instanceof Board)) {
            return false;
        } else if (N != ((Board) y).N) {
            return false;
        }
        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                if (tileAt(row, col) != ((Board) y).tileAt(row, col)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        String str = board.toString();
        return str.hashCode();
    }


    /** Returns the string representation of the board.
      * Uncomment this method. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i,j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

}
