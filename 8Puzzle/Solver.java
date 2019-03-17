/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Collections;

public class Solver {
    private class Node implements Comparable<Node> {
        public final Board board;
        public final int steps;
        public final Node predecessor;

        public Node(Board board, int steps, Node predecessor) {
            this.board = board;
            this.steps = steps;
            this.predecessor = predecessor;
        }

        public int compareTo(Node node) {
            int thisPriority = board.manhattan() + steps;
            int thatPriority = node.board.manhattan() + node.steps;
            if (thisPriority < thatPriority) {
                return -1;
            }
            else if (thisPriority == thatPriority) {
                return 0;
            }
            else {
                return 1;
            }
        }
    }

    private boolean isSolvable = false;
    private ArrayList<Board> solution;
    private int moves = -1;

    public Solver(Board initial) {
        if (initial == null) {
            throw new java.lang.IllegalArgumentException("Null board passed to solver");
        }

        MinPQ<Node> queue = new MinPQ<>();
        queue.insert(new Node(initial, 0, null));

        Board initialTwin = initial.twin();
        MinPQ<Node> queueTwin = new MinPQ<>();
        queueTwin.insert(new Node(initialTwin, 0, null));

        ArrayList<Node> visitedNodes = new ArrayList<>();

        boolean solved = false;
        boolean solvedTwin = false;

        while (!solved && !solvedTwin) {
            Node item = step(queue);
            visitedNodes.add(item);
            solved = item.board.isGoal();

            Node itemTwin = step(queueTwin);
            solvedTwin = itemTwin.board.isGoal();
        }

        isSolvable = solved;
        if (isSolvable) {
            solution = findShortestPath(visitedNodes);
            // trim first value, which is the initial board
            //solution = new ArrayList<Board>(path.subList(1, path.size()));
            moves = solution.size() - 1;
        }
    }

    private Node step(MinPQ<Node> gameQueue) {
        // get the smallest value off the priority queue
        Node nextItem = gameQueue.delMin();

        if (!nextItem.board.isGoal()) {
            // Get all the neighbors of this board and insert them onto the queue
            for (Board b : nextItem.board.neighbors()) {
                // But only do this if the neighbor isn't equal to the predecessor
                if (nextItem.predecessor == null || !b.equals(nextItem.predecessor.board)) {
                    gameQueue.insert(new Node(b, nextItem.steps + 1, nextItem));
                }
            }
        }

        return nextItem;
    }

    private ArrayList<Board> findShortestPath(ArrayList<Node> visitedNodes) {
        ArrayList<Board> boardStates = new ArrayList<>();
        Node inspect = visitedNodes.get(visitedNodes.size() - 1);
        while (inspect != null) {
            boardStates.add(inspect.board);
            inspect = inspect.predecessor;
        }
        Collections.reverse(boardStates);
        return boardStates;
    }

    public boolean isSolvable() {
        return isSolvable;
    }

    public int moves() {
        return moves;
    }

    public Iterable<Board> solution() {
        return solution;
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
