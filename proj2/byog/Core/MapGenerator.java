package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.IOException;
import java.io.Serializable;
import java.util.Random;

public class MapGenerator implements Serializable {
    private static final int MINROOMS = 30;
    private static int roomCount;
    private Room[] rooms;
    private static Random RANDOM;
    private TETile[][] world = new TETile[Game.WIDTH][Game.HEIGHT];

    //Phase 2: add player, key and gate
    private Player player;
    private Position keyPos;
    private Position gatePos;
    private boolean unlocked;

    public MapGenerator(long seed) {
        // phase 1: map generator
        RANDOM = new Random(seed);
        roomCount = RANDOM.nextInt(5) + MINROOMS;
        rooms = new Room[roomCount];
        fillWithNothing();
        placeRooms();
        randomlyConnectRooms();

        // phase 2: key, gate, player
        unlocked = false;
        player = new Player();
        keyPos = placeKey();
        gatePos = placeGate();
    }

    public TETile[][] finalWorld() {
        return world;
    }

    public static TETile[][] playWithString(String input) {
        SerializableHelper<String> saveHelper = new SerializableHelper<>();
        if (input.charAt(0) == 'l' || input.charAt(0) == 'L') {
            String savedCmd = null;
            try {
                savedCmd = saveHelper.deserializeObj();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            String mergeCmd = savedCmd.substring(0, savedCmd.length() - 3) + input.substring(1);
            try {
                saveHelper.serializeObj(mergeCmd);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return playWithString(mergeCmd);
        } else {
            long seed = seedParser(input);
            MapGenerator map = new MapGenerator(seed);

            String moveCmd = map.movePlayer(input);
            map.renderOthers();
            try {
                saveHelper.serializeObj(input);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            TETile[][] finalWorldFrame = map.finalWorld();
            return finalWorldFrame;
        }

    }

    /**
     *  Reads the argument of player's input, then convert it to seed
     *  @param input the string inputted by player */
    public static long seedParser(String input) {
        String seedStr = "";
        if (input.charAt(0) == 'n' || input.charAt(0) == 'N') {
            int i = 1;
            while (input.charAt(i) != 's' && input.charAt(i) != 'S') {
                seedStr += String.valueOf(input.charAt(i));
                i += 1;
            }
        }

        return Long.parseLong(seedStr);
    }

    /**
     * Reads the move command of the player's input argument,
     * then make Player move according to the command.
     */
    public String movePlayer(String input) {
        int len = input.length();
        // get the move cmd start index in input string
        int i = 1;
        while (input.charAt(i - 1) != 's' && input.charAt(i - 1) != 'S') {
            i += 1;
        }
        // move player step by step
        String cmdSeries = "";
        while (i < len) {
            if ((input.charAt(i) == ':')
                    && (input.charAt(i + 1) == 'q' || input.charAt(i + 1) == 'Q')) {
                if (i == len - 2) {
                    break;
                } else if (input.charAt(i + 2) == 'l' || input.charAt(i + 2) == 'L') {
                    i += 3;
                    continue;
                }
            }
            char cmd = input.charAt(i);
            cmdSeries += cmd;
            player.move(cmd);
            i += 1;
        }

        return cmdSeries;
    }

    /* Returns whether a position is out of the map */
    public static boolean outOfBound(Position pos) {
        return pos.x < 1 || pos.x >= Game.WIDTH - 1 || pos.y < 2 || pos.y >= Game.HEIGHT - 1;
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
            return !inOtherRoom;
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
      Returns a negative number or 0
      if two rooms can be connected with a horizontal straight line */
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
        Position A = randomPosition(roomA.position.x, roomA.position.y,
                roomA.roomWidth, roomA.roomHeight);
        Position B = randomPosition(roomB.position.x, roomB.position.y,
                roomB.roomWidth, roomB.roomHeight);
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

    /* Render player, gate and key */
    public void renderOthers() {
        if (unlocked) {
            world[gatePos.x][gatePos.y] = Tileset.UNLOCKED_DOOR;
        } else {
            world[gatePos.x][gatePos.y] = Tileset.LOCKED_DOOR;
        }

        world[player.currPos.x][player.currPos.y] = Tileset.PLAYER; // render a player

        if (!player.hasKey) {
            world[keyPos.x][keyPos.y] = Tileset.KEY;
        }
    }

    // Place a key in a random room
    private Position placeKey() {
        Room keyRoom = rooms[roomCount - 1];
        return randomPosition(keyRoom.position.x, keyRoom.position.y,
                keyRoom.roomWidth, keyRoom.roomHeight);
    }

    // generate a gate
    // TODO: there is a bug
    private Position placeGate() {
        Room gateRoom = rooms[roomCount / 2];
        Position randPos = randomPosition(gateRoom.position.x, gateRoom.position.y,
                gateRoom.roomWidth, gateRoom.roomWidth);
        int x = randPos.x;
        int y = randPos.y;
        int direction = RANDOM.nextInt(4);
        switch (direction) {
            case 1:
                // move up to find edge
                while (world[x][y + 1] != Tileset.NOTHING) {
                    y += 1;
                }
                break;
            case 2:
                // move down
                while (world[x][y - 1] != Tileset.NOTHING) {
                    y -= 1;
                }
                break;
            case 3:
                // move left
                while (world[x - 1][y] != Tileset.NOTHING) {
                    x -= 1;
                }
                break;
            case 4:
                // move right
                while (world[x + 1][y] != Tileset.NOTHING) {
                    x += 1;
                }
                break;
            default:
                break;
        }
        return new Position(x, y);
    }

    public void movePlayer(char cmd) {
        player.move(cmd);
    }


    /* the character Object */
    public class Player {
        private Position currPos;
        private boolean hasKey;

        public Player() {
            Room bornRoom = rooms[0];
            currPos = randomPosition(bornRoom.position.x, bornRoom.position.y,
                    bornRoom.roomWidth, bornRoom.roomHeight);
            hasKey = false;
        }

        public void move(char cmd) {
            int x = currPos.x;
            int y = currPos.y;
            world[x][y] = Tileset.FLOOR;
            if (cmd == 'w' || cmd == 'W') {
                if (world[x][y + 1] == Tileset.FLOOR) {
                    currPos = new Position(x, y + 1);
                }
            }
            if (cmd == 's' || cmd == 'S') {
                if (world[x][y - 1] == Tileset.FLOOR) {
                    currPos = new Position(x, y - 1);
                }
            }
            if (cmd == 'a' || cmd == 'A') {
                if (world[x - 1][y] == Tileset.FLOOR) {
                    currPos = new Position(x - 1, y);
                }
            }
            if (cmd == 'd' || cmd == 'D') {
                if (world[x + 1][y] == Tileset.FLOOR) {
                    currPos = new Position(x + 1, y);
                }
            }
            pickKey(); // automatically pick up key while moving
            unlockGate(); // automatically unlock door while moving
        }

        private boolean reachable(Position target) {
            return Math.abs(currPos.x - target.x) + Math.abs(currPos.y - target.y) <= 1;
        }

        public void pickKey() {
            if (reachable(keyPos)) {
                hasKey = true;
                world[keyPos.x][keyPos.y] = Tileset.FLOOR;
            }
        }

        public void unlockGate() {
            if (reachable(gatePos) && hasKey) {
                unlocked = true;
            }
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
        public static boolean samePosition(Position A, Position B) {
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
}
