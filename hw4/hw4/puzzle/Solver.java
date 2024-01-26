package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;

public class Solver {
    private MinPQ<SearchNode> bestMoves;
    private SearchNode root;
    private SearchNode solution;

    public Solver(WorldState initial) {
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
                    bestMoves.insert(new SearchNode(w, p));
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
        ArrayList<WorldState> ans = new ArrayList<>(solution.move + 1);
        SearchNode p = solution;
        for (int i = 0; i <= solution.move; i += 1) {
            ans.set(solution.move - i, p.world);
            p = p.prev;
        }
        return ans;
    }


    private class SearchNode implements Comparable<SearchNode> {
        WorldState world;
        SearchNode prev;
        int move;

        private SearchNode(WorldState w, SearchNode p) {
            world = w;
            prev = p;
            move = p.move + 1;
        }

        public int compareTo(SearchNode o) {
            int myDis = world.estimatedDistanceToGoal() + move;
            int otherDis = o.world.estimatedDistanceToGoal() + o.move;
            return myDis - otherDis;
        }
    }
}
