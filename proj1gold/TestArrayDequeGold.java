import org.junit.Test;
import static org.junit.Assert.*;

public class TestArrayDequeGold {

    @Test
    public void testStudentArrayDeque() {

        ArrayDequeSolution<Integer> sol1 = new ArrayDequeSolution<>();
        StudentArrayDeque<Integer> sad1 = new StudentArrayDeque<>();

        for (int i = 0; i < 10; i += 1) {
            double numberBetweenZeroAndOne = StdRandom.uniform();

            if (numberBetweenZeroAndOne < 0.5) {
                sad1.addLast(i);
                sol1.addLast(i);
            } else {
                sad1.addFirst(i);
                sol1.addFirst(i);
            }
        }


        for (int i = 0; i < 10; i += 1) {
            assertEquals(sad1.get(i),sol1.get(i));
        }
//        assertEquals(sad1.get(0),sol1.get(0));
//        assertEquals(sad1.get(1),sol1.get(1));
//        assertEquals(sad1.get(2),sol1.get(2));
//        assertEquals(sad1.get(3),sol1.get(3));
//        assertEquals(sad1.get(4),sol1.get(4));
//        assertEquals(sad1.get(5),sol1.get(5));
//        assertEquals(sad1.get(6),sol1.get(6));
    }
}
