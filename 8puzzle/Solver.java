import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;
import java.util.Deque;

public class Solver {

    private Deque<Board> solutionSteps = new LinkedList<Board>();

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("initial board should not be null!");
        }

        SearchNode finalSolution = solve(initial);
        while (finalSolution != null && finalSolution.steps >= 0) {

            solutionSteps.addFirst(finalSolution.board);
            finalSolution = finalSolution.predecessor;
        }
        if (solutionSteps.size() == 0) {
            solutionSteps = null;
        }
    }

    /**
     * Solves the puzzle, returns the final {@link SearchNode}
     *
     * @param initial the initial {@link SearchNode}
     * @return the final {@link SearchNode}
     */
    private SearchNode solve(Board initial) {


        MinPQ<SearchNode> normalPQ = new MinPQ<>();
        MinPQ<SearchNode> alternativePQ = new MinPQ<>();

        SearchNode nullSearchNode = new SearchNode(-1, initial, null);

        normalPQ.insert(new SearchNode(0, initial, nullSearchNode));
        alternativePQ.insert(new SearchNode(0, initial.twin(), nullSearchNode));

        SearchNode normalNode = null;
        SearchNode alternativeNode = null;

        do {
            normalNode = normalPQ.delMin();

            // solve one iteration of normal board
            Iterable<Board> normalNeighbours = normalNode.board.neighbors();
            for (Board normalNeighbour : normalNeighbours) {
                /**
                 * A critical optimization. Best-first search has one annoying feature:
                 * search nodes corresponding to the same board are enqueued on the priority queue
                 * many times. To reduce unnecessary exploration of useless search nodes,
                 * when considering the neighbors of a search node, don't enqueue a neighbor
                 * if its board is the same as the board of the predecessor search node.
                 */
                if (!normalNeighbour.equals(normalNode.predecessor.board)) {
                    SearchNode normalSearchNode = new SearchNode(normalNode.steps + 1,
                                                                 normalNeighbour,
                                                                 normalNode);
                    normalPQ.insert(normalSearchNode);
                }
            }

            alternativeNode = alternativePQ.delMin();

            // solve one iteration of twin board
            Iterable<Board> alternativeneighbours = alternativeNode.board.neighbors();
            for (Board alternativeNeighbour : alternativeneighbours) {
                if (!alternativeNeighbour.equals(alternativeNode.predecessor.board)) {
                    SearchNode alternativeSearchNode = new SearchNode(alternativeNode.steps + 1,
                                                                      alternativeNeighbour,
                                                                      alternativeNode);
                    alternativePQ.insert(alternativeSearchNode);
                }
            }
        } while (!(normalNode.board.manhattan() == 0 || alternativeNode.board.manhattan() == 0));

        // if the alternative board is solved, this means that the normal board is unsolvable
        if (alternativeNode.board.isGoal()) {
            return null;
        }

        return normalNode;
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

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

    // is the initial board solvable?
    public boolean isSolvable() {
        return solutionSteps != null && solutionSteps.size() > 0;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (solutionSteps == null) {
            return -1;
        }
        return solutionSteps.size() - 1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return solutionSteps;
    }

    /**
     * The Search Node
     */
    private class SearchNode implements Comparable<SearchNode> {

        /**
         * Number of steps so far
         */
        private final int steps;

        /**
         * The board
         */
        private final Board board;

        /**
         * The previous {@link SearchNode}
         */
        private final SearchNode predecessor;

        public SearchNode(int steps, Board board, SearchNode predecessor) {
            this.steps = steps;
            this.board = board;
            this.predecessor = predecessor;
        }

        @Override
        public String toString() {
            return "SearchNode{" +
                    "\r\npriority=" + getPriority() +
                    "\r\nsteps=" + steps +
                    "\r\nboard=" + board +
                    "\r\npredecessor=" + predecessor +
                    "-------------------------------------------";
        }

        /**
         * Returns the priority: steps + manhattan distance
         *
         * @return the priority: steps + manhattan distance
         */
        public int getPriority() {
            return board.manhattan() + steps;
        }

        /**
         * Compares this object with the specified object for order.  Returns a negative integer,
         * zero, or a positive integer as this object is less than, equal to, or greater than the
         * specified object.
         *
         * <p>The implementor must ensure
         * {@code sgn(x.compareTo(y)) == -sgn(y.compareTo(x))} for all {@code x} and {@code y}.
         * (This implies that {@code x.compareTo(y)} must throw an exception iff {@code
         * y.compareTo(x)} throws an exception.)
         *
         * <p>The implementor must also ensure that the relation is transitive:
         * {@code (x.compareTo(y) > 0 && y.compareTo(z) > 0)} implies {@code x.compareTo(z) > 0}.
         *
         * <p>Finally, the implementor must ensure that {@code x.compareTo(y)==0}
         * implies that {@code sgn(x.compareTo(z)) == sgn(y.compareTo(z))}, for all {@code z}.
         *
         * <p>It is strongly recommended, but <i>not</i> strictly required that
         * {@code (x.compareTo(y)==0) == (x.equals(y))}.  Generally speaking, any class that
         * implements the {@code Comparable} interface and violates this condition should clearly
         * indicate this fact.  The recommended language is "Note: this class has a natural ordering
         * that is inconsistent with equals."
         *
         * <p>In the foregoing description, the notation
         * {@code sgn(}<i>expression</i>{@code )} designates the mathematical
         * <i>signum</i> function, which is defined to return one of {@code -1},
         * {@code 0}, or {@code 1} according to whether the value of
         * <i>expression</i> is negative, zero, or positive, respectively.
         *
         * @param other the object to be compared.
         * @return a negative integer, zero, or a positive integer as this object is less than,
         * equal to, or greater than the specified object.
         * @throws NullPointerException if the specified object is null
         * @throws ClassCastException   if the specified object's type prevents it from being
         *                              compared to this object.
         */
        @Override
        public int compareTo(SearchNode other) {
            if (other == null) {
                throw new NullPointerException();
            }
            int thisPriority = this.getPriority();
            int otherPriority = other.getPriority();
            return thisPriority - otherPriority;
        }
    }
}
