package byog.Core;

import byog.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;

public class GameUI {
    private final int uiWidth;
    private final int uiHeight;
    private static final int TILE_SIZE = 16;
    private int xOffset;
    private int yOffset;

    private long seed;
    private MapGenerator map;
    private boolean gameOn;
    private String gameCmd;

    public GameUI() {
        uiWidth = Game.WIDTH;
        uiHeight = Game.HEIGHT; // 2 more tiles height for HUD

        initCanvas();
        drawMenu();

        menuOperation();
        startGame();
    }

    // execute different command according to the options in menu
    // Q: Exit the program
    // L: Reload the game
    // N: Request user to input a number to start new game
    private void menuOperation() {
        gameOn = false;
        while (!gameOn) {
            if (StdDraw.isKeyPressed('N') || StdDraw.isKeyPressed('n')) {
                boolean gotSeed = false;
                while (!gotSeed) {
                    //get input seed
                    seed = Long.parseLong(getSeed('s'));
                    gotSeed = true;
                }
                map = new MapGenerator(seed); // instantiate the map
                gameOn = true; // break the loop
            } else if (StdDraw.isKeyPressed('Q') || StdDraw.isKeyPressed('q')) {
                System.exit(0);
            }
        }
    }

    public void initCanvas(int xOff, int yOff) {
        xOffset = xOff;
        yOffset = yOff;

        StdDraw.setCanvasSize(uiWidth * TILE_SIZE, uiHeight * TILE_SIZE);
        StdDraw.setXscale(0, uiWidth);
        StdDraw.setYscale(0, uiHeight);
        StdDraw.clear(new Color(0, 0, 0));

        StdDraw.enableDoubleBuffering();
    }

    public void initCanvas() {
        initCanvas(0, 0);
    }

    public void startGame() {
        gameOn = true;

        while (gameOn) {
            TETile[][] world = map.finalWorld();
            drawWorld(world);
            gameCmd = solicitNCharsInput('q');
            System.exit(0);
        }
    }

    public void drawWorld(TETile[][] world) {
        int numXTiles = world.length;
        int numYTiles = world[0].length;

        StdDraw.clear(new Color(0, 0, 0));
        Font font = new Font("Monaco", Font.BOLD, TILE_SIZE - 2);
        StdDraw.setFont(font);

        for (int x = 0; x < numXTiles; x += 1) {
            for (int y = 0; y < numYTiles; y += 1) {
                if (world[x][y] == null) {
                    throw new IllegalArgumentException("Tile at position x=" + x + ", y=" + y
                            + " is null.");
                }
                world[x][y].draw(x + xOffset, y + yOffset);
                map.renderOthers(); // render player, key, gate
            }
        }

        // Draw player, gate and key


        // Draw HUD
        StdDraw.setPenColor(Color.white);
        StdDraw.line(0, 2, uiWidth, 2);
        StdDraw.textLeft(2, 1, "Q: save & exit | W: up; | A: left | S: down | D: right");
        StdDraw.show();
    }


    public void drawMenu() {
        int midWidth = uiWidth / 2;
        int midHeight = uiHeight / 2;

        StdDraw.setPenColor(Color.white);
        // Draw title
        Font bigFont = new Font("Monaco", Font.BOLD, 40);
        StdDraw.setFont(bigFont);
        StdDraw.text(midWidth, uiHeight / 4 * 3, "CS61B: THE GAME");

        // Draw options
        Font smallFont = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(smallFont);
        StdDraw.text(midWidth, midHeight, "New Game (N)");
        StdDraw.text(midWidth, midHeight - 2, "Load Game (L)");
        StdDraw.text(midWidth, midHeight - 4, "Quit (Q)");

        StdDraw.show();
    }



    public String solicitNCharsInput(char c) {
        String input = "";

        while (gameOn) {
            StdDraw.clear(Color.black);
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char key = StdDraw.nextKeyTyped();
            if (key == c || key == Character.toUpperCase(c)) {
                break;
            } else {
                input += String.valueOf(key);
                map.movePlayer(key);
                TETile[][] world = map.finalWorld();
                drawWorld(world);
                StdDraw.show();
            }
        }
        return input;
    }

    // get the user input string, except the last char c
    private String getSeed(char c) {
        String input = "";

        while (true) {
            StdDraw.clear(Color.black);
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char key = StdDraw.nextKeyTyped();
            if (key == c || key == Character.toUpperCase(c)) {
                break;
            } else {
                drawMenu();
                StdDraw.text(uiWidth / 2, uiHeight / 2 - 8, "Input a number to start a new game:");
                StdDraw.text(uiWidth / 2, uiHeight / 2 - 12, "press S to START");
                input += String.valueOf(key);
                StdDraw.text(uiWidth / 2, uiHeight / 2 - 10, input.substring(1));
                StdDraw.show();
            }
        }
        return input.substring(1);
    }



}
