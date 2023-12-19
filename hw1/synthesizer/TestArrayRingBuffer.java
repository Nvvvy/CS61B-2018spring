package synthesizer;

import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void someTest() {
        ArrayRingBuffer arb = new ArrayRingBuffer(10);
        for (int i=0; i<10; i+=1) {
            arb.enqueue(i);
        }
        assertTrue(arb.isFull());

        Iterator<Integer> iter = arb.iterator();
        int k = 0;
        while (iter.hasNext()) {
            assertEquals((Integer) k, (Integer) iter.next());
            k += 1;
        }

        for (int i=0; i<10; i+=1) {
            assertEquals(i, arb.peek());
            Object poped = arb.dequeue();
            assertEquals(i, poped);
        }
        assertTrue(arb.isEmpty());
        assertEquals(0, arb.fillCount);

    }

    /** Calls tests for ArrayRingBuffer. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestArrayRingBuffer.class);
    }
}
