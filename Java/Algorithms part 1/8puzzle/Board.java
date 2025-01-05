/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    private final int tiles[][];
    private final int n;
    private int hammingDistance = 0, manhattanDistance = 0;
    private int blankRow = 0, blankCol = 0;
    private int[] rows, cols;


    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles.length;
        this.tiles = new int[n][n];
        for (int row = 0; row < this.n; row++)
            this.tiles[row] = tiles[row].clone();

        for (int row = 0; row < n; row++)
            for (int col = 0; col < n; col++) {
                int element = this.tiles[row][col];

                // Get the row and col of blank square
                if (element == 0) {
                    blankRow = row;
                    blankCol = col;
                    continue;
                }

                // Calculate Hamming and Manhattan distance in constructor to reduce number of loops
                // Hamming distance calculation: (Exclude the blank element of goal board, done with the if statement above, or the comments below)
                if (element != row * n + col + 1) // && (element != 0)) Can also write it like: !((row == n - 1) && (col == n - 1)))
                    hammingDistance++;

                // Manhattan distance calculation:
                int goalBoardRow = (element - 1) / n;
                int goalBoardCol = element - goalBoardRow * n - 1;
                manhattanDistance += Math.abs(row - goalBoardRow) + Math.abs(col - goalBoardCol);
            }

        // Find a random pair of tiles to get a twin
        do {
            rows = StdRandom.permutation(n, 2);
            cols = StdRandom.permutation(n, 2);
        } while ((rows[0] == blankRow) && (cols[0] == blankCol) || (rows[1] == blankRow) && (cols[1] == blankCol));
    }

    // string representation of this board
    public String toString() {
        String stringTiles = String.valueOf(n) + "\n ";  // Print the size of the puzzle

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                stringTiles += String.valueOf(tiles[i][j]);
                if (j < n - 1) stringTiles += " ";
            }
            stringTiles += "\n ";
        }
        return stringTiles;
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        return hammingDistance;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattanDistance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return (hammingDistance == 0);
    }

    // does this board equal y? (Equal when they have the same size and corresponding tiles in the same positions)
    public boolean equals(Object y) {

        // If y is null or not of type Board, return false
        if ((y == null) || (!(y.getClass().isInstance(Board.this)))) return false;

        // If the tiles are not of the same size or exactly equal, return false
        Board that = (Board) y;
        if ((this.n != that.n) || (!(Arrays.deepEquals(this.tiles, that.tiles)))) return false;

        return true;
    }

    // Swap two tiles
    private int[][] swap(int row1, int col1, int row2, int col2) {

        int[][] copyTiles = new int[n][n];
        for (int row = 0; row < n; row++)
            copyTiles[row] = tiles[row].clone();

        int temp = copyTiles[row1][col1];
        copyTiles[row1][col1] = copyTiles[row2][col2];
        copyTiles[row2][col2] = temp;

        return copyTiles;
    }

    // all neighboring boards (Check if there's room on the left, and swap left. Check if there's room up, and swap up...)
    public Iterable<Board> neighbors() {

        ArrayList<Board> neighborBoards = new ArrayList<>();

        if (blankRow > 0) // Up
            neighborBoards.add(new Board(swap(blankRow, blankCol, blankRow - 1, blankCol)));
        if (blankRow < n - 1) // Down
            neighborBoards.add(new Board(swap(blankRow, blankCol, blankRow + 1, blankCol)));
        if (blankCol > 0) // Left
            neighborBoards.add(new Board(swap(blankRow, blankCol, blankRow, blankCol - 1)));
        if (blankCol < n - 1) // Right
            neighborBoards.add(new Board(swap(blankRow, blankCol, blankRow, blankCol + 1)));

        return neighborBoards;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        return new Board(swap(rows[0], cols[0], rows[1], cols[1]));
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        In in = new In(args[0]);
        int size = in.readInt();
        int[][] testTiles = new int[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                testTiles[i][j] = in.readInt();
        Board testBoard = new Board(testTiles);

        StdOut.println("The board: ");
        StdOut.println(testBoard.toString());
        StdOut.println("Dimension: " + testBoard.dimension());
        StdOut.println("Hamming distance: " + testBoard.hamming());
        StdOut.println("Manhattan distance: " + testBoard.manhattan());
        StdOut.println("Is this the goal borad? " + testBoard.isGoal());
        StdOut.println("Neighbor boards: ");
        for (Board neighbor : testBoard.neighbors())
            StdOut.println(neighbor.toString());
        StdOut.println("Possible twin board: \n" + testBoard.twin().toString());
    }

}
