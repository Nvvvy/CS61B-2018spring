package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

public class MapGenerator {
    private static final int MINROOMS = 30;
    private static int roomCount;
    private Room[] rooms;
    private static Random RANDOM;
    public TETile[][] world = new TETile[Game.WIDTH][Game.HEIGHT];

    public MapGenerator(long seed) {
        RANDOM = new Random(seed);
        roomCount = RANDOM.nextInt(5) + MINROOMS;
        rooms = (Room []) new Room[roomCount];
        fillWithNothing();
        placeRooms();
        randomlyConnectRooms();
    }

    /**
     *  Reads the argument of player's input, then convert it to seed
     *  @param input the string inputted by player */
    public static long inputParser(String input) {
        if ((!input.startsWith("N")) && (!input.endsWith("S"))) {
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
    public boolean isLegalRoom(Room room) {
        Position pos = room.position;
        int width = room.roomWidth;
        int height = room.roomHeight;
        Position bottomRight = new Position(pos.x + width, pos.y);
        Position topLeft = new Position(pos.x, pos.y + height);
        Position topRight = new Position(pos.x + width, pos.y + height);
        boolean outOfBound = outOfBound(pos) || outOfBound(bottomRight)
                || outOfBound(topLeft) || outOfBound(topRight);
        if (outOfBound) {
            return false;
        } else {
            boolean inOtherRoom = world[pos.x][pos.y] != Tileset.NOTHING
                || world[bottomRight.x][bottomRight.y] != Tileset.NOTHING
                || world[topLeft.x][topLeft.y] != Tileset.NOTHING
                || world[topRight.x][topRight.y] != Tileset.NOTHING;
            if (inOtherRoom) {
                return false;
            } else {
                return true;
            }
        }
    }

    /* generate a room pseudorandomly */
    public Room randomRoom(Position pos) {
        int height = RANDOM.nextInt(3) + Room.MINHEIGHT;
        int width = RANDOM.nextInt(3) + Room.MINDIDTH;

        return new Room(width, height, pos);
    }

    /* Returns a random position in a certain range:
    * minX < position.x < minX + width
    * minY < position.y < minY + height */
    public static Position randomPosition(int minX, int minY, int width, int height) {
        int xxPos = RANDOM.nextInt(width - 1) + minX + 1;
        int yyPos = RANDOM.nextInt(height - 1) + minY + 1;
        return new Position(xxPos, yyPos);
    }

    /* allocate given number of legal rooms on the map, then draw rooms */
    public void placeRooms() {
        int count = 0;
        while (count < roomCount) {
            Position randomPos = randomPosition(0, 0, Game.WIDTH, Game.HEIGHT);
            Room randomRoom = randomRoom(randomPos);
            if (isLegalRoom(randomRoom)) {
                drawRoom(randomRoom); // fill the grid with walls and floors
                rooms[count] = randomRoom;
                count += 1;
            }
        }
    }

    public void drawRoom(Room room) {
        for (int y = room.position.y; y <= room.position.y + room.roomHeight; y += 1) {
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

    /* Returns true if two rooms can be connected with straight line*/
    private boolean straightAccessible(Room A, Room B) {
        return horizontalDistance(A, B) <= -2 || verticalDistance(A, B) <= -2;
    }

    /* Calculates the horizontal distance between A and B
      Returns a negative number or 0 if two rooms can be connected with a vertical straight line */
    private int horizontalDistance(Room A, Room B) {
        if (A.position.x < B.position.x) {
            return (B.position.x - A.position.x) - A.roomWidth;
        } else {
            return (A.position.x - B.position.x) - B.roomWidth;
        }
    }

    /* Calculates the vertical distance between A and B
      Returns a negative number or 0 if two rooms can be connected with a horizontal straight line */
    private int verticalDistance(Room A, Room B) {
        if (A.position.y < B.position.y) {
            return  (B.position.y - A.position.y) - A.roomHeight;
        } else {
            return  (A.position.y - B.position.y) - B.roomHeight;
        }
    }

    /* Connect room A,B with a straight line hallway */
    private void straightConnect(Room A, Room B) {
        int verticalDist = verticalDistance(A, B);
        int horizontalDist = horizontalDistance(A, B);

        /* Draw a horizontal line */
        if (verticalDist <= -2) {
            int Y = Math.max(A.position.y, B.position.y) + 1;
            int startXXPos = Math.min(A.position.x, B.position.x) + 1;
            int endXXPos = Math.max(A.position.x, B.position.x);
            Position start = new Position(startXXPos, Y);
            Position end = new Position(endXXPos, Y);
            drawHorizontal(start, end);
        }

        /* Draw a vertical line */
        if (horizontalDist <= -2) {
            int X = Math.max(A.position.x, B.position.x) + 1;
            int startYYPos = Math.min(A.position.y, B.position.y) + 1;
            int endYYPos = Math.max(A.position.y, B.position.y);
            Position start = new Position(X, startYYPos);
            Position end = new Position(X, endYYPos);
            drawVertical(start, end);
        }
    }

    /* Draw a vertical line */
    private void drawVertical(Position A, Position B) {
        int X = A.x;
        int startY = Math.min(A.y, B.y);
        int endY = Math.max(A.y, B.y);
        for (int y = startY; y <= endY; y += 1) {
            if (world[X - 1][y] == Tileset.NOTHING) {
                world[X - 1][y] = Tileset.WALL;
            }
            if (world[X + 1][y] == Tileset.NOTHING) {
                world[X + 1][y] = Tileset.WALL;
            }
            world[X][y] = Tileset.FLOOR;
        }
    }

    /* Draw a horizontal line */
    private void drawHorizontal(Position A, Position B) {
        int Y = A.y;
        int startX = Math.min(A.x, B.x);
        int endX = Math.max(A.x, B.x);
        for (int x = startX; x <= endX; x += 1) {
            if (world[x][Y + 1] == Tileset.NOTHING) {
                world[x][Y + 1] = Tileset.WALL;
            }
            if (world[x][Y - 1] == Tileset.NOTHING) {
                world[x][Y - 1] = Tileset.WALL;
            }
            world[x][Y] = Tileset.FLOOR;
        }
    }

    /* Connect room A,B with L hallway */
    private void lConnect(Room roomA, Room roomB) {
        //Generate a random point in Room A,B respectively
        Position A = randomPosition(roomA.position.x, roomA.position.y, roomA.roomWidth, roomA.roomHeight);
        Position B = randomPosition(roomB.position.x, roomB.position.y, roomB.roomWidth, roomB.roomHeight);
        // find top-left and bottom-left of the probably L hallway routine;
        Position bottomLeft = Position.findBottomLeft(A, B);
        Position topLeft = Position.findTopLeft(A, B);

        //Connect point A & B with line
        if (Position.samePosition(A, bottomLeft) || Position.samePosition(A, bottomLeft)) {
            connectPoint(topLeft, A);
            connectPoint(topLeft, B);
            if (world[topLeft.x - 1][topLeft.y + 1] == Tileset.NOTHING) {
                world[topLeft.x - 1][topLeft.y + 1] = Tileset.WALL;
            }
        } else {
            connectPoint(bottomLeft, A);
            connectPoint(bottomLeft, B);
            if (world[bottomLeft.x - 1][bottomLeft.y - 1] == Tileset.NOTHING) {
                world[bottomLeft.x - 1][bottomLeft.y - 1] = Tileset.WALL;
            }
        }
    }

    /* connect rooms depends on their vertical and horizontal distance */
    private void connectRoom(Room A, Room B) {
        if (straightAccessible(A, B)) {
            straightConnect(A, B);
        } else {
            lConnect(A, B);
        }
    }

    /* connect two points with a line hallway */
    private void connectPoint(Position A, Position B) {
        if (A.x == B.x) {
            drawVertical(A, B);
        }
        if (A.y == B.y) {
            drawHorizontal(A, B);
        }
    }

    /* connect rooms pseudorandomly */
    private void randomlyConnectRooms() {
        for (int i = 1; i < roomCount; i += 1) {
            connectRoom(rooms[i - 1], rooms[i]);
        }
    }


    /* A Room object with random size and entries at given position,
    which specifies the bottom-left location of the room */
    public class Room {
        private static final int MINHEIGHT = 3;
        private static final int MINDIDTH = 3;
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
        private int x;
        private int y;

        public Position(int xPos, int yPos) {
            x = xPos;
            y = yPos;
        }

        /* Check whether A and B at the same point */
        private static boolean samePosition(Position A, Position B) {
            return A.x == B.x && A.y == B.y;
        }

        /* Returns the bottom-left point */
        private static Position findBottomLeft(Position A, Position B) {
            int xPos = Math.min(A.x, B.x);
            int yPos = Math.min(A.y, B.y);
            return new Position(xPos, yPos);
        }

        /* Returns the top-left point */
        private static Position findTopLeft(Position A, Position B) {
            int xPos = Math.min(A.x, B.x);
            int yPos = Math.max(A.y, B.y);
            return new Position(xPos, yPos);
        }
    }


//    public static void main(String[] args) {
//        // read the arg, AKA: seed. convert input string to long
//        // String input = args[0]; the real command
//        String input = "N22029278579S"; // a test arg for Junit
//        long seed = inputParser(input);
//
//        // Test: initialize the tile rendering engine with a window of size WIDTH x HEIGHT
//        TERenderer ter = new TERenderer();
//        ter.initialize(Game.WIDTH, Game.HEIGHT);
//
//        // init the world
//        MapGenerator initMap = new MapGenerator(seed);
//
//        // Test: draws the world to the screen
//        ter.renderFrame(initMap.world);
//    }
}
