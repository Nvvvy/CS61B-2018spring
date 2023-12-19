import org.junit.Test;
import static org.junit.Assert.*;

public class TestHorribleSteve {

    @Test
    public void testSteve() {
        assertTrue(!Flik.isSameNumber(128, 128));
        assertFalse(Flik.isSameNumber(128, 128));

        assertTrue(!Flik.isSameNumber(200, 200));
        assertFalse(Flik.isSameNumber(200, 200));

        assertTrue(Flik.isSameNumber(127, 127));
        assertFalse(!Flik.isSameNumber(127, 127));

        assertTrue(128 == 128);
        assertTrue(200 == 200);

        for (int i = 200, j = 200; i < 203; i++, j++) {
            System.out.println(Flik.isSameNumber(i, j));
            System.out.println(i == j);
            System.out.println(equalsOrNot(i, j));
            System.out.println(equalsOrNot(500, 500));
            System.out.println(equalsOrNot(127, 127));
            if (!Flik.isSameNumber(i, j)) {
                break; // break exits the for loop!
            }
        }
    }

    private static boolean equalsOrNot(Integer a, Integer b) {
        return (a == b);
    }
}
