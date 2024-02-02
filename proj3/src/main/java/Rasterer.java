import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    private Map<Integer, Double> depthMap;
    private final double mapWidth;
    private final double mapHeight;

    public Rasterer() {
        // YOUR CODE HERE

        mapWidth = MapServer.ROOT_LRLON - MapServer.ROOT_ULLON;
        mapHeight = MapServer.ROOT_ULLAT - MapServer.ROOT_LRLAT;
        depthMap = imgLonDPPMap();
    }

    private Map<Integer, Double> imgLonDPPMap() {
        Map<Integer, Double> map = new HashMap<>();

        for (int d = 0; d <= 7; d++) {
            double LonDPP = mapWidth / (MapServer.TILE_SIZE * Math.pow(2, d));
            map.put(d, LonDPP);
        }
        return map;
    }

    /* find depth with proper LonDPP of image to render the map */
    private int findDepth(double box) {
        int depth = 0;
        for (int i = 0; i <= 7; i++) {
            depth = i;
            if (box > depthMap.get(i)) {
                break;
            }
        }
        return depth;
    }

    /* find the upper-left column/x of the image grid */
    private int gridColumn(int depth, double lon) {
        double imgLon = mapWidth / Math.pow(2, depth);
        return (int) Math.floor((lon - MapServer.ROOT_ULLON) / imgLon);
    }

    /* find the upper-left row/y of the image grid */
    private int gridRow(int depth, double lat) {
        double imgLat = mapHeight / Math.pow(2, depth);
        return (int) Math.floor((MapServer.ROOT_ULLAT - lat) / imgLat);
    }

    /* Generates a filename grid of images with given start coordinate and size */
    private String[][] renderGridFile(int depth, int startX, int startY, int rows, int cols) {
        String[][] renderGrid = new String[rows][cols];
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                int imgX = x + startX;
                int imgY = y + startY;
                String fileName = "d" + depth + "_x" + imgX + "_y" + imgY + ".png";
                renderGrid[y][x] = fileName;
            }
        }
        return renderGrid;
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        System.out.println(params);
        Map<String, Object> results = new HashMap<>();
//        System.out.println("Since you haven't implemented getMapRaster, nothing is displayed in "
//                           + "your browser.");


        // get queryBox info
        double lrlon = params.get("lrlon");
        double lrlat = params.get("lrlat");
        double ullon = params.get("ullon");
        double ullat = params.get("ullat");
        double w = params.get("w");
        double h = params.get("h");

        // check corner case for invalid query
        boolean outOfRange = lrlon < MapServer.ROOT_ULLON
                || ullon > MapServer.ROOT_LRLON
                || ullat < MapServer.ROOT_LRLAT
                || lrlat > MapServer.ROOT_ULLAT;

        boolean invalidCoordinate = ullon > lrlon || ullat < lrlat;

        boolean querySuccess = !(outOfRange || invalidCoordinate);

        // find proper depth with the queryBox LonDPP
        double boxLonDPP = (lrlon - ullon) / w;
        int depth = findDepth(boxLonDPP);

        // find the upper-left index of the image grid
        int startRow = gridRow(depth, ullat);
        int startCol = gridColumn(depth, ullon);

        // calculates the column and row of the render grid
        int rows = gridRow(depth, lrlat) - startRow + 1;
        int columns = gridColumn(depth, lrlon) - startCol + 1;

        // calculates the coordinate of ul&lr img;
        double imgLonW = mapWidth / Math.pow(2, depth);
        double imgLatH = mapHeight / Math.pow(2, depth);
        double ulImgLon = MapServer.ROOT_ULLON + imgLonW * startCol;
        double ulImgLat = MapServer.ROOT_ULLAT - imgLatH * startRow;
        double lrImgLon = ulImgLon + imgLonW * columns;
        double lrImgLat = ulImgLat - imgLatH * rows;

        // generate a filename 2D grid of images to be displayed
        String[][] renderGrid = (querySuccess)?
                renderGridFile(depth, startCol, startRow, rows, columns) : null;



        results.put("render_grid", renderGrid);
        results.put("raster_ul_lon", ulImgLon);
        results.put("raster_ul_lat", ulImgLat);
        results.put("raster_lr_lon", lrImgLon);
        results.put("raster_lr_lat", lrImgLat);
        results.put("depth", depth);
        results.put("query_success", querySuccess);

        return results;
    }

    public static void main(String[] args) {
        Rasterer raster = new Rasterer();

    }

}
