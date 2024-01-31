import edu.princeton.cs.algs4.Queue;

public class MergeSort {
    /**
     * Removes and returns the smallest item that is in q1 or q2.
     *
     * The method assumes that both q1 and q2 are in sorted order, with the smallest item first. At
     * most one of q1 or q2 can be empty (but both cannot be empty).
     *
     * @param   q1  A Queue in sorted order from least to greatest.
     * @param   q2  A Queue in sorted order from least to greatest.
     * @return      The smallest item that is in q1 or q2.
     */
    private static <Item extends Comparable> Item getMin(
            Queue<Item> q1, Queue<Item> q2) {
        if (q1.isEmpty()) {
            return q2.dequeue();
        } else if (q2.isEmpty()) {
            return q1.dequeue();
        } else {
            // Peek at the minimum item in each queue (which will be at the front, since the
            // queues are sorted) to determine which is smaller.
            Comparable q1Min = q1.peek();
            Comparable q2Min = q2.peek();
            if (q1Min.compareTo(q2Min) <= 0) {
                // Make sure to call dequeue, so that the minimum item gets removed.
                return q1.dequeue();
            } else {
                return q2.dequeue();
            }
        }
    }

    /** Returns a queue of queues that each contain one item from items. */
    private static <Item extends Comparable> Queue<Queue<Item>>
            makeSingleItemQueues(Queue<Item> items) {
        // Your code here!
        Queue<Queue<Item>> ret = new Queue<>();
        for (Item i : items) {
            Queue<Item> single = new Queue<>();
            single.enqueue(i);
            ret.enqueue(single);
        }
        return ret;
    }

    /**
     * Returns a new queue that contains the items in q1 and q2 in sorted order.
     *
     * This method should take time linear in the total number of items in q1 and q2.  After
     * running this method, q1 and q2 will be empty, and all of their items will be in the
     * returned queue.
     *
     * @param   q1  A Queue in sorted order from least to greatest.
     * @param   q2  A Queue in sorted order from least to greatest.
     * @return      A Queue containing all of the q1 and q2 in sorted order, from least to
     *              greatest.
     *
     */
    private static <Item extends Comparable> Queue<Item> mergeSortedQueues(
            Queue<Item> q1, Queue<Item> q2) {
        // Your code here!
        Queue<Item> merged = new Queue<>();
        while (!q1.isEmpty() || !q2.isEmpty()) {
            merged.enqueue(getMin(q1, q2));
        }
        return merged;
    }

    /** Returns a Queue that contains the given items sorted from least to greatest. */
    public static <Item extends Comparable> Queue<Item> mergeSort(
            Queue<Item> items) {
        // Your code here!
        if (items.size() <= 1) {
            return items;
        }
//        Queue<Item> halfLeft = new Queue<>();
//        for (int i = 0; i < (items.size() / 2); i++) {
//            halfLeft.enqueue(items.dequeue());
//        }
//        return mergeSortedQueues(mergeSort(halfLeft), mergeSort(items));
        Queue<Queue<Item>> tmp = makeSingleItemQueues(items);
        while (tmp.size() != 1) {
            Queue<Queue<Item>> tmpp = new Queue<>();
            while (!tmp.isEmpty()) {
                Queue<Item> q1 = tmp.dequeue();
                Queue<Item> q2 = tmp.isEmpty() ? new Queue<>() : tmp.dequeue();
                tmpp.enqueue(mergeSortedQueues(q1, q2));
            }
            tmp = tmpp;
        }
        return tmp.dequeue();
    }

    /* A lightweight test */
    public static void main(String[] args) {
        Queue<String> students = new Queue<String>();
        students.enqueue("1");
        students.enqueue("3");
        students.enqueue("5");
        students.enqueue("9");
        students.enqueue("0");
        students.enqueue("2");

        String before = students.toString();
        Queue<String> sorted = MergeSort.mergeSort(students);
        String after = sorted.toString();

        System.out.println("before sorting: " + before);
        System.out.println("after  sorting: " + after);
        System.out.println(!before.equals(after));
    }
}
