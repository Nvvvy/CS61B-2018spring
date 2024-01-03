package byog.Core;

import byog.TileEngine.TETile;
import org.junit.Test;

import static org.junit.Assert.*;

public class MapGeneratorTest {

//    @Test
//    public void inputParserTest() {
//        String input = "N22000929S";
//        long expect = 22000929;
//        long actual = MapGenerator.seedParser(input);
//        assertEquals(expect, actual);
//
////        String input2 = "A22000929E";
////        long actual2 = MapGenerator.inputParser(input2);
////        assertEquals(expect, actual2);
//    }

    @Test
    public void OutOfBoundTest() {
        MapGenerator.Position p1 = new MapGenerator.Position(-1, -2);
        assertTrue(MapGenerator.outOfBound(p1));

        MapGenerator.Position p2 = new MapGenerator.Position(40, 100);
        assertTrue(MapGenerator.outOfBound(p2));

        MapGenerator.Position p3 = new MapGenerator.Position(60, 18);
        assertFalse(MapGenerator.outOfBound(p3));
    }

    @Test
    public void moveCmdTest() {
        MapGenerator map = new MapGenerator(223);


        String cmd = "N999SDDDWWWDDD";
        String actual = map.movePlayer(cmd);
        assertEquals("DDDWWWDDD", actual);

        String cmd2 = "N999SD:qlDDW:qlWWDDD";
        String actual2 = map.movePlayer(cmd2);
        assertEquals("DDDWWWDDD", actual2);
    }

    @Test
    public void seedParserTest() {
        MapGenerator map = new MapGenerator(223);

        String cmd = "N999SDDDWWWDDD";
        long actual = MapGenerator.seedParser(cmd);
        assertEquals(999, actual);
    }

    @Test
    public void serializeTest() throws Exception {
        String cmd = "N999SDDDWWWDDD";
        String cmd1 = "N999SDD:q";
        String cmd2 = "LDWWWDDD";

        Game game = new Game();
        TETile[][] world = game.playWithInputString(cmd);

        Game game1 = new Game();
        TETile[][] world1 = game.playWithInputString(cmd1);

        Game game2 = new Game();
        TETile[][] world2 = game.playWithInputString(cmd2);

        String strW = TETile.toString(world);
        String strW2 = TETile.toString(world2);
        assertEquals(strW, strW2);
    }
}
