package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

public class MapGenerator {
    private static int rooms;
    private static Random RANDOM;
    private TETile[][] world = new TETile[Game.WIDTH][Game.HEIGHT];

    public MapGenerator(long seed) {
        RANDOM = new Random(seed);
        rooms = RANDOM.nextInt(5) + 6;
        fillWithNothing();
        placeRooms();
    }

    /**
     *  Reads the argument of player's input, then convert it to seed
     *  @param input the string inputted by player */
    public static long inputParser(String input) {
        if ((! input.startsWith("N")) && (! input.endsWith("S"))) {
           throw new RuntimeException("the init command starts with N and ends up with S");
        }
        String seedStr = input.substring(1, input.length() - 1);
        return Long.parseLong(seedStr);
    }

    /* Returns whether a position is out of the map */
    public static boolean outOfBound(Position pos) {
        return pos.x < 0 || pos.x >= Game.WIDTH || pos.y < 0 || pos.y >= Game.HEIGHT;
    }

    /* Returns whether a room could be placed on the map */
    public static boolean isLegalRoom(Room room) {
        Position pos = room.position;
        int width = room.roomWidth;
        int height = room.roomHeight;
        return !(pos.x < 0 || pos.x + width >= Game.WIDTH || pos.y < 0 || pos.y + height >= Game.HEIGHT);
    }

    /* generate a  */
    public Room randomRoom(Position pos) {
        int height = RANDOM.nextInt(5) + 3;
        int width = RANDOM.nextInt(5) + 3;

        return new Room(width, height, pos);
    }

    /* Returns a random position */
    public static Position randomPosition() {
        int xxPos = RANDOM.nextInt(Game.WIDTH);
        int yyPos = RANDOM.nextInt(Game.HEIGHT);
        return new Position(xxPos, yyPos);
    }

    /* allocate given number of legal rooms on the map, then draw rooms */
    public void placeRooms() {
        int count = 0;
        while (count < rooms) {
            Position randomPos = randomPosition();
            Room randomRoom = randomRoom(randomPos);
            if (isLegalRoom(randomRoom)) {
                drawRoom(randomRoom); // fill the grid with walls and floors
                count += 1;
            }
        }
    }

    public void drawRoom(Room room) {
        for (int y = room.position.y; y <= room.position.y + room.roomHeight; y += 1){
            for (int x = room.position.x; x <= room.position.x + room.roomWidth; x += 1) {
                Position currPos = new Position(x, y);
                if (room.onEdge(currPos)) {
                    world[x][y] = Tileset.WALL;
                }
                if (room.inRoom(currPos)) {
                    world[x][y] = Tileset.FLOOR;
                }
            }
        }
    }

    private void fillWithNothing() {
        for (int x = 0; x < Game.WIDTH; x += 1) {
            for (int y = 0; y < Game.HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }



    /* a Room object with random size and entries at given position,
    which specifies the bottom-left location of the room */
    public class Room {
        private int roomWidth;
        private int roomHeight;
        private Position position;

        public Room(int width, int height, Position pos) {
            roomWidth = width;
            roomHeight = height;
            position = pos;
        }

        public boolean onEdge(Position pos) {
            boolean onXXRoll = (pos.y == position.y || pos.y == position.y + roomHeight)
                    && (pos.x >= position.x && pos.x <= position.x + roomWidth);
            boolean onYYRoll = (pos.x == position.x || pos.x == position.x + roomWidth)
                    && (pos.y >= position.y && pos.y < position.y + roomHeight);
            return onXXRoll || onYYRoll;
        }

        public boolean inRoom(Position pos) {
            return (pos.x > position.x && pos.x < position.x + roomWidth
                    && pos.y > position.y && pos.y < position.y + roomHeight);
        }
    }

    /* an object to represent the position in the world */
    public static class Position {
        public int x;
        public int y;

        public Position(int xPos, int yPos) {
            x = xPos;
            y = yPos;
        }
    }


    public static void main(String[] args) {
        // read the arg, AKA: seed. convert input string to long
//        String input = args[0]; the real command
        String input = "N22029278579S"; // a test arg for Junit
        long seed = inputParser(input);

        // Test: initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(Game.WIDTH, Game.HEIGHT);

        // init the world
        MapGenerator initMap = new MapGenerator(seed);


        // Test: draws the world to the screen
        ter.renderFrame(initMap.world);

    }

}
