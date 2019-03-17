/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;

public class Board {
    private final int[][] blocks;
    private final int[][] blocksTwin;
    private final int hamming;
    private final int manhattan;
    private final int maxNumDigits;
    private final String repr;

    public Board(int[][] blocks) {
        this.blocks = blocks;
        hamming = computeHamming();
        manhattan = computeManhattan();
        maxNumDigits = maxNumDigits();
        blocksTwin = makeTwin();
        repr = repr();
    }

    private int numDigits(int input) {
        if (input == 0) {
            return 1;
        }

        int digits = 0;
        while (input != 0) {
            input = input / 10;
            digits++;
        }
        return digits;
    }

    private int maxNumDigits() {
        int maxValue = dimension() * dimension() - 1;
        return numDigits(maxValue);
    }

    private int computeHamming() {
        int sum = 0;
        for (int row = 0; row < dimension(); row++) {
            for (int col = 0; col < dimension(); col++) {
                if (blocks[row][col] != 0 && blocks[row][col] != row * dimension() + col + 1) {
                    sum++;
                }
            }
        }

        return sum;
    }

    private int computeManhattan() {
        int sum = 0;
        for (int row = 0; row < dimension(); row++) {
            for (int col = 0; col < dimension(); col++) {
                // ignore empty space
                if (blocks[row][col] != 0) {
                    int val = blocks[row][col] - 1; // minus one for true value (0 indexed)
                    int targetRow = val / dimension();
                    int targetCol = val % dimension();

                    int distance = Math.abs(targetRow - row) + Math.abs(targetCol - col);
                    sum += distance;
                }
            }
        }

        return sum;
    }

    private void swap(int[][] board, int rowA, int colA, int rowB, int colB) {
        int tmp = board[rowA][colA];
        board[rowA][colA] = board[rowB][colB];
        board[rowB][colB] = tmp;
    }

    private boolean safeSwap(int[][] board, int rowA, int colA, int rowB, int colB) {
        if ((rowA >= 0 && rowA < board.length) && (colA >= 0 && colA < board.length) &&
                (rowB >= 0 && rowB < board.length) && (colB >= 0 && colB < board.length)) {
            swap(board, rowA, colA, rowB, colB);
            return true;
        }
        return false;
    }

    private int[][] boardCopy(int[][] board) {
        int[][] copy = new int[board.length][board.length];
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board.length; col++) {
                copy[row][col] = board[row][col];
            }
        }
        return copy;
    }

    private int[][] makeTwin() {
        // edge cases
        if (dimension() == 0) {
            return new int[0][0];
        }
        if (dimension() == 1) {
            int[][] twinBoard = new int[1][1];
            twinBoard[0][0] = blocks[0][0];
            return twinBoard;
        }

        // Generate random variables
        int[] indices = new int[dimension() * dimension() - 1];
        int[][] twinBlocks = new int[dimension()][dimension()];
        int i = 0;
        for (int rows = 0; rows < dimension(); rows++) {
            for (int cols = 0; cols < dimension(); cols++) {
                if (blocks[rows][cols] != 0) {
                    indices[i] = rows * dimension() + cols;
                    i++;
                }
                twinBlocks[rows][cols] = blocks[rows][cols];
            }
        }

        // Shuffle and swap first two
        StdRandom.shuffle(indices);
        int rowA = indices[0] / dimension();
        int rowB = indices[1] / dimension();
        int colA = indices[0] % dimension();
        int colB = indices[1] % dimension();

        swap(twinBlocks, rowA, colA, rowB, colB);

        return twinBlocks;
    }

    private String repr() {
        StringBuilder s = new StringBuilder();
        s.append(Integer.toString(dimension()) + "\n");

        for (int row = 0; row < dimension(); row++) {
            for (int col = 0; col < dimension(); col++) {
                int value = blocks[row][col];
                // Get the number of digits in this value
                int digits = numDigits(value);
                int prependSpaces = maxNumDigits - digits;
                for (int sp = 0; sp < prependSpaces; sp++) {
                    s.append(" ");
                }
                s.append(Integer.toString(value) + " ");

            }
            s.append("\n");
        }

        return s.toString();
    }

    /************** Public functions ****************/
    public int dimension() {
        return blocks.length;
    }

    public int hamming() {
        return hamming;
    }

    public int manhattan() {
        return manhattan;
    }

    public boolean isGoal() {
        return manhattan == 0;
    }

    public Board twin() {
        return new Board(blocksTwin);
    }

    public boolean equals(Object y) {
        if (y == null || this.getClass() != y.getClass()) {
            return false;
        }

        Board other = (Board) y;
        if (other.dimension() != dimension()) {
            return false;
        }

        for (int row = 0; row < dimension(); row++) {
            for (int col = 0; col < dimension(); col++) {
                if (blocks[row][col] != other.blocks[row][col]) {
                    return false;
                }
            }
        }

        return true;
    }

    public Iterable<Board> neighbors() {
        ArrayList<Board> allNeighbors = new ArrayList<Board>();
        // find the zero
        for (int row = 0; row < dimension(); row++) {
            for (int col = 0; col < dimension(); col++) {
                if (blocks[row][col] == 0) {
                    // Try swapping left, right, up, and down
                    int[][] leftBoard = boardCopy(blocks);
                    if (safeSwap(leftBoard, row, col, row, col - 1)) {
                        allNeighbors.add(new Board(leftBoard));
                    }

                    int[][] rightBoard = boardCopy(blocks);
                    if (safeSwap(rightBoard, row, col, row, col + 1)) {
                        allNeighbors.add(new Board(rightBoard));
                    }

                    int[][] upBoard = boardCopy(blocks);
                    if (safeSwap(upBoard, row, col, row - 1, col)) {
                        allNeighbors.add(new Board(upBoard));
                    }

                    int[][] downBoard = boardCopy(blocks);
                    if (safeSwap(downBoard, row, col, row + 1, col)) {
                        allNeighbors.add(new Board(downBoard));
                    }
                }
            }
        }

        return allNeighbors;
    }

    public String toString() {
        return repr;
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        StdOut.println(initial.toString());
        StdOut.println("Hamming distance: " + initial.hamming() + "\nManhattan distance: " + initial
                .manhattan());

        for (Board b : initial.neighbors()) {
            StdOut.println(b.toString());
            StdOut.println(
                    "Hamming distance: " + b.hamming() + "\nManhattan distance: " + b.manhattan());
        }
    }
}
