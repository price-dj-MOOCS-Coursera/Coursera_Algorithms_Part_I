import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

/**
 * 
 */

/**
 * @author David Price
 *
 */
public class Solver {
    private SearchNode lastSearchNode = null;
    
    
    // Private inner class for search node
    private class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private int numMoves;
        private SearchNode previous;
        
        public SearchNode(Board board, SearchNode previous) {
            this.board = board;
            this.previous = previous;
            if (previous == null) { this.numMoves = 0; }
            else { this.numMoves = previous.numMoves + 1; }
        }
        
        private int priority() {
            return numMoves + board.manhattan();
        }
        
        @Override
        public int compareTo(SearchNode that) {
            if (this.priority() < that.priority()) {
                return -1;
            }
            
            if (this.priority() > that.priority()) {
                return 1;
            }
            return 0;
        }
    }
    
    
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        
        MinPQ<SearchNode> minPQ = new MinPQ<SearchNode>();
        MinPQ<SearchNode> minPQTwin = new MinPQ<SearchNode>();
        
        Board board = initial;
        Board boardTwin = initial.twin();
        SearchNode node = new SearchNode(board, null);
        SearchNode nodeTwin = new SearchNode(boardTwin, null);
        minPQ.insert(node);
        minPQTwin.insert(nodeTwin);
        
        boolean boardSolvable = false;
        boolean boardTwinSolvable = false;
        
        while(!boardSolvable && !boardTwinSolvable) {
            lastSearchNode = AStarPart(minPQ);
            boardSolvable = (lastSearchNode != null);
            boardTwinSolvable = (AStarPart(minPQTwin) != null);
        }
        
        
    }
    
    private SearchNode AStarPart(MinPQ<SearchNode> minPQ) {
        if (minPQ.isEmpty()) { return null; }
        
        SearchNode current = minPQ.delMin();
        
        if (current.board.isGoal()) { return current; }
        
        for (Board bd : current.board.neighbors()) {
            if (current.previous == null || !bd.equals(current.previous.board)) {
                minPQ.insert(new SearchNode(bd, current));
            }
        }
        return null;
        
    }
    
    // is the initial board solvable?
    public boolean isSolvable() {
        return lastSearchNode != null;
    }
    
    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (lastSearchNode != null) { 
            int moves = lastSearchNode.numMoves;
            return moves;
        }
        else { return -1; }
    }
    
    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (lastSearchNode != null) {
            Stack<Board> solution = new Stack<Board>();
            for (SearchNode tail = lastSearchNode; tail != null; tail = tail.previous) {
                solution.push(tail.board);
            }
            return solution;
        }
        else { return null; }
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
}
