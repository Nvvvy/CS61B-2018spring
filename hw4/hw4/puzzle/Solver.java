package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.LinkedList;

public class Solver {
    private MinPQ<SearchNode> bestMoves;
    private SearchNode root;
    private SearchNode solution;

    public Solver(WorldState initial) {
        bestMoves = new MinPQ<>();
        root = new SearchNode(initial, null);
        solution = aStarSearch();
    }

    private SearchNode aStarSearch() {
        bestMoves.insert(root);
        while (!bestMoves.isEmpty()) {
            SearchNode p = bestMoves.delMin();
            if (p.world.isGoal()) {
                return p;
            } else {
                for (WorldState w : p.world.neighbors()) {
                    if (p.prev == null || !w.equals(p.prev.world)) {
                        bestMoves.insert(new SearchNode(w, p));
                    }
                }
            }
        }
        return null;
    }


    /**Returns the minimum number of moves to solve the puzzle starting
     at the initial WorldState.*/
    public int moves() {
        return solution.move;
    }

    /**Returns a sequence of WorldStates from the initial WorldState
     to the solution.*/
    public Iterable<WorldState> solution() {
        LinkedList<WorldState> ans = new LinkedList<>();
        SearchNode p = solution;
        for (int i = 0; i <= solution.move; i += 1) {
            ans.addFirst(p.world);
            p = p.prev;
        }
        return ans;
    }


    private class SearchNode implements Comparable<SearchNode> {
        WorldState world;
        SearchNode prev;
        int move;
        int estimatedDis;

        private SearchNode(WorldState w, SearchNode p) {
            world = w;
            prev = p;
            if (p == null) {
                move = 0;
            } else {
                move = p.move + 1;
            }
            estimatedDis = world.estimatedDistanceToGoal();
        }

        public int compareTo(SearchNode o) {
            return (estimatedDis + move) - (o.estimatedDis + o.move);
        }
    }
}
