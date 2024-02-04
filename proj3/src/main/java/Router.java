import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {
    /**
     * Return a List of longs representing the shortest path from the node
     * closest to a start location and the node closest to the destination
     * location.
     * @param g The graph to use.
     * @param stlon The longitude of the start location.
     * @param stlat The latitude of the start location.
     * @param destlon The longitude of the destination location.
     * @param destlat The latitude of the destination location.
     * @return A list of node id's in the order visited on the shortest path.
     */
    public static List<Long> shortestPath(GraphDB g, double stlon, double stlat,
                                          double destlon, double destlat) {
        // find closest node near start and destination
        long startId = g.closest(stlon, stlat);
        long destId = g.closest(destlon, destlat);

        AStarSP aStar = new AStarSP(g, startId, destId);

        return aStar.SP();
    }


    /**
     * Create the list of directions corresponding to a route on the graph.
     * @param g The graph to use.
     * @param route The route to translate into directions. Each element
     *              corresponds to a node from the graph in the route.
     * @return A list of NavigatiionDirection objects corresponding to the input
     * route.
     */
    public static List<NavigationDirection> routeDirections(GraphDB g, List<Long> route) {
        return null; // FIXME
    }


    /**
     * Helper class to run A* algo
     */
    public static class AStarSP {
        private GraphDB g;
        private long s;
        private long t;
        private HashSet<Long> marked;
        private HashMap<Long, Double> disTo;
        private HashMap<Long, Long> edgeTo;
        private PriorityQueue<NodeDisPair> fringe;
        private List<Long> shortestPath;

        public AStarSP(GraphDB g, long s, long t) {
            this.g = g;
            this.s = s;
            this.t = t;
            marked = new HashSet<>();
            disTo = new HashMap<>();
            edgeTo = new HashMap<>();
            fringe = new PriorityQueue<>();
            shortestPath = aStarSearch();
        }

        /**
         * Run A* algorithm
         */
        private List<Long> aStarSearch() {

            double initEst = heuristic(s, t);
            disTo.put(s, (double) 0);
            fringe.add(new NodeDisPair(s, initEst));

            while (!fringe.isEmpty()) {
                NodeDisPair v = fringe.poll();
                if (v.id == t) {
                    break;
                }
                if (marked.contains(v.id)) {
                    continue;
                }
                marked.add(v.id);
                for (long w : g.adjacent(v.id)) {
                    relaxAdj(v.id, w);
                }
            }
            return findPath();
        }


        private List<Long> findPath() {
            LinkedList<Long> route = new LinkedList<>();
            route.addFirst(t);
            long curr = t;

            while (route.getFirst() != s) {
                long vToCurr = edgeTo.get(curr);
                route.addFirst(vToCurr);
                curr = vToCurr;
            }
            return route;
        }

        /**
         * Returns the estimated distance between the vertex curr and dest
         * @return great-circle distance.
         */
        private double heuristic(long startId, long destId) {
            double stlon = g.vertices.get(startId).lon;
            double stlat = g.vertices.get(startId).lat;

            double destlon = g.vertices.get(destId).lon;
            double destlat = g.vertices.get(destId).lat;
            return GraphDB.distance(stlon, stlat, destlon, destlat);
        }

        /**
         * Relaxes the adjacent node: update disTo, edgeTo and fringe
         */
        private void relaxAdj(long v, long w) {
            if (marked.contains(w)) {
                return;
            }

            if (!fringe.isEmpty() && marked.contains(fringe.peek().id)) {
                fringe.poll();
            }

            /* if w has not been marked or disTo(w) < disTo(v) + actualDis(v, w),
               then update disTo and edgeTo  */
            double vwDis = actualDis(v, w);
            if (!edgeTo.containsKey(w) || disTo.get(v) + vwDis < disTo.get(w)) {
                edgeTo.put(w, v);
                disTo.put(w, disTo.get(v) + vwDis);
            }

            double totalDis = heuristic(w, t) + disTo.get(w);
            fringe.add(new NodeDisPair(w, totalDis));
        }

        /**
         * Calculates the actual great-circle distance between v and w
         */
        private double actualDis(long v, long w) {
            double vLon = g.vertices.get(v).lon;
            double vLat = g.vertices.get(v).lat;
            double wLon = g.vertices.get(w).lon;
            double wLat = g.vertices.get(w).lat;
            return GraphDB.distance(vLon, vLat, wLon, wLat);
        }

        public List<Long> SP() {
            return shortestPath;
        }
    }

    /**
     * A helper class for shortestPath method to implement a minPQ for A* algorithm.
     */
    private static class NodeDisPair implements Comparable<NodeDisPair> {
        private long id;
        private double distance;

        NodeDisPair(long id, double distance) {
            this.id = id;
            this.distance = distance;
        }

        public int compareTo(NodeDisPair o) {
            double cmp = distance - o.distance;
            if (cmp < 0) {
                return -1;
            } else if (cmp == 0) {
                return 0;
            } else {
                return 1;
            }
        }
    }


    /**
     * Class to represent a navigation direction, which consists of 3 attributes:
     * a direction to go, a way, and the distance to travel for.
     */
    public static class NavigationDirection {

        /** Integer constants representing directions. */
        public static final int START = 0;
        public static final int STRAIGHT = 1;
        public static final int SLIGHT_LEFT = 2;
        public static final int SLIGHT_RIGHT = 3;
        public static final int RIGHT = 4;
        public static final int LEFT = 5;
        public static final int SHARP_LEFT = 6;
        public static final int SHARP_RIGHT = 7;

        /** Number of directions supported. */
        public static final int NUM_DIRECTIONS = 8;

        /** A mapping of integer values to directions.*/
        public static final String[] DIRECTIONS = new String[NUM_DIRECTIONS];

        /** Default name for an unknown way. */
        public static final String UNKNOWN_ROAD = "unknown road";

        /** Static initializer. */
        static {
            DIRECTIONS[START] = "Start";
            DIRECTIONS[STRAIGHT] = "Go straight";
            DIRECTIONS[SLIGHT_LEFT] = "Slight left";
            DIRECTIONS[SLIGHT_RIGHT] = "Slight right";
            DIRECTIONS[LEFT] = "Turn left";
            DIRECTIONS[RIGHT] = "Turn right";
            DIRECTIONS[SHARP_LEFT] = "Sharp left";
            DIRECTIONS[SHARP_RIGHT] = "Sharp right";
        }

        /** The direction a given NavigationDirection represents.*/
        int direction;
        /** The name of the way I represent. */
        String way;
        /** The distance along this way I represent. */
        double distance;

        /**
         * Create a default, anonymous NavigationDirection.
         */
        public NavigationDirection() {
            this.direction = STRAIGHT;
            this.way = UNKNOWN_ROAD;
            this.distance = 0.0;
        }

        public String toString() {
            return String.format("%s on %s and continue for %.3f miles.",
                    DIRECTIONS[direction], way, distance);
        }

        /**
         * Takes the string representation of a navigation direction and converts it into
         * a Navigation Direction object.
         * @param dirAsString The string representation of the NavigationDirection.
         * @return A NavigationDirection object representing the input string.
         */
        public static NavigationDirection fromString(String dirAsString) {
            String regex = "([a-zA-Z\\s]+) on ([\\w\\s]*) and continue for ([0-9\\.]+) miles\\.";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(dirAsString);
            NavigationDirection nd = new NavigationDirection();
            if (m.matches()) {
                String direction = m.group(1);
                if (direction.equals("Start")) {
                    nd.direction = NavigationDirection.START;
                } else if (direction.equals("Go straight")) {
                    nd.direction = NavigationDirection.STRAIGHT;
                } else if (direction.equals("Slight left")) {
                    nd.direction = NavigationDirection.SLIGHT_LEFT;
                } else if (direction.equals("Slight right")) {
                    nd.direction = NavigationDirection.SLIGHT_RIGHT;
                } else if (direction.equals("Turn right")) {
                    nd.direction = NavigationDirection.RIGHT;
                } else if (direction.equals("Turn left")) {
                    nd.direction = NavigationDirection.LEFT;
                } else if (direction.equals("Sharp left")) {
                    nd.direction = NavigationDirection.SHARP_LEFT;
                } else if (direction.equals("Sharp right")) {
                    nd.direction = NavigationDirection.SHARP_RIGHT;
                } else {
                    return null;
                }

                nd.way = m.group(2);
                try {
                    nd.distance = Double.parseDouble(m.group(3));
                } catch (NumberFormatException e) {
                    return null;
                }
                return nd;
            } else {
                // not a valid nd
                return null;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof NavigationDirection) {
                return direction == ((NavigationDirection) o).direction
                    && way.equals(((NavigationDirection) o).way)
                    && distance == ((NavigationDirection) o).distance;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, way, distance);
        }
    }
}
