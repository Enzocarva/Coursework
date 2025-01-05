/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    // Last search node will be the solution
    private SearchNode lastNode;

    private class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final int moves;
        private final int priority;
        private final SearchNode previousNode;

        // Constructor for initializing root search node
        public SearchNode(Board board) {
            this.board = board;
            this.moves = 0;
            this.priority = moves + board.manhattan();
            this.previousNode = null;
        }

        // Constructor for all other nodes added
        public SearchNode(Board board, SearchNode previousNode) {
            this.board = board;
            this.previousNode = previousNode;
            this.moves = previousNode.moves + 1;
            this.priority = moves + board.manhattan();
        }

        public int compareTo(SearchNode that) {
            return this.priority - that.priority;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException("Initial is null");

        // Declare and initialize the min priority queue with the first node
        MinPQ<SearchNode> gameTree = new MinPQ<>();
        gameTree.insert(new SearchNode(initial));

        // Must have this twin game tree in order to check which ones are solvable or not (the unsolvable become solvable with a single tile swap, aka twin)
        MinPQ<SearchNode> twinGameTree = new MinPQ<>();
        twinGameTree.insert(new SearchNode(initial.twin()));

        // Remove the last node and add its neighbors until the last one is the goal board
        lastNode = gameTree.delMin();
        SearchNode twinLastNode = twinGameTree.delMin();

        while (!lastNode.board.isGoal() && !twinLastNode.board.isGoal()) {
            for (Board neighbor : lastNode.board.neighbors()) {
                if ((lastNode.moves == 0) || !neighbor.equals(lastNode.previousNode.board)) // Account for first node because previous node will be null
                    gameTree.insert(new SearchNode(neighbor, lastNode));
            }
            for (Board twinNeighbor : twinLastNode.board.neighbors()) {
                if ((twinLastNode.moves == 0) || !twinNeighbor.equals(twinLastNode.previousNode.board))
                    twinGameTree.insert(new SearchNode(twinNeighbor, twinLastNode));
            }

            lastNode = gameTree.delMin();
            twinLastNode = twinGameTree.delMin();
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        if (lastNode.board.isGoal())
            return true;
        return false;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (isSolvable())
            return lastNode.moves;
        return -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;

        // Make a stack to push in reverse order (solution to the initial node) to be able to print in normal order (from initial to solution)
        Stack<Board> solutionSequence = new Stack<>();
        SearchNode current = lastNode;
        while (current.previousNode != null) {
            solutionSequence.push(current.board);
            current = current.previousNode;
        }
        solutionSequence.push(current.board);
        return solutionSequence;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
