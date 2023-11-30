public class ArrayDeque<T> {

    private T[] items;
    private int size;
    private int nextFirst;
    private int nextLast;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        nextFirst = 3;
        nextLast = 4;
    }

    /* expand or shrink the capacity of <items> on demand */
    private void reSize(int capacity) {
        T[] newItems = (T[]) new Object[capacity];

        /* copy the elements */
        int theFirst = plusOne(nextFirst); //index of the first item in <items>
        int rightItems = Math.min(size, items.length - theFirst);
        int leftItems = size - rightItems;
        int copyStartIndex = capacity - rightItems; // index where starts to copy the right side items

        System.arraycopy(items, theFirst, newItems, copyStartIndex, rightItems);
        System.arraycopy(items, 0, newItems, 0, leftItems);
        items = newItems;

        /* update the indices of elements */
        nextFirst = minusOne(copyStartIndex);
        if (copyStartIndex + size > items.length) {
            nextLast = leftItems;
        } else {
            nextLast = plusOne(copyStartIndex + size - 1);
        }
    }


    /**
     * Compute the target index before the give index
     * Note that the empty place exists
     */
    private int minusOne(int index) {
        /* there is empty space on the left */
        if (index > 0) {
            return index - 1;
        }
        /* no empty space on the left, return the last place on right side */
        return items.length - 1;
    }

    /**
     * Compute the target index after the give index
     * Note that the empty place exists
     */
    private int plusOne(int index) {
        if (index < items.length - 1) {
            return index + 1;
        }
        return 0;
    }

    public void addFirst(T item) {
        if (size == items.length) {
            reSize(size * 2);
        }

        items[nextFirst] = item;

        /*nextFirst move to the left by default */
        nextFirst = minusOne(nextFirst);

        size += 1;
    }

    public void addLast(T item) {
        if (size == items.length) {
            reSize(size * 2);
        }
        items[nextLast] = item;

        /*nextFirst move to the right by default */
        nextLast = plusOne(nextLast);

        size += 1;
    }


    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public T removeFirst() {
        if (this.isEmpty()) {
            return null;
        }

        T removed = items[plusOne(nextFirst)];
        items[plusOne(nextFirst)] = null;
        nextFirst = plusOne(nextFirst);
        size -= 1;

        double capacityUsage = Double.valueOf(size) / Double.valueOf(items.length);
        if (capacityUsage < 0.25 & items.length > 8) {
            reSize(items.length / 2);
        }

        return removed;
    }

    public T removeLast() {
        if (this.isEmpty()) {
            return null;
        }

        int theLast = minusOne(nextLast);
        T removed = items[theLast];
        items[theLast] = null;
        nextLast = theLast;
        size -= 1;

        double capacityUsage = Double.valueOf(size) / Double.valueOf(items.length);

        if (capacityUsage < 0.25 & items.length > 8) {
            reSize(items.length / 2);
        }

        return removed;
    }

    public T get(int index) {
        if (index + plusOne(nextFirst) < items.length) {
            return items[index + plusOne(nextFirst)];
        } else {
            return items[index + plusOne(nextFirst) - items.length];
        }
    }


    public void printDeque() {
        int i = plusOne(nextFirst);
        int count = 0;
        while (count < size) {
            System.out.print(items[i] + " ");
            i = plusOne(i);
            count += 1;
        }
    }

}
