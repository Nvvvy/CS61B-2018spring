public class LinkedListDeque<T> {

    private class ListNode {
        private T item;
        private ListNode next;
        private ListNode prev;

        public ListNode(T i, ListNode n, ListNode p) {
            item = i;
            next = n;
            prev = p;
        }
    }

    private ListNode sentinel = new ListNode((T) "sentinel", null, null);
    private int size;


    public LinkedListDeque() {
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    public void addFirst(T x) {
        sentinel.next = new ListNode(x, sentinel.next, sentinel);
        sentinel.next.prev = sentinel;
        sentinel.next.next.prev = sentinel.next;
        size += 1;
    }

    public void addLast(T x) {
        sentinel.prev.next = new ListNode(x, sentinel, sentinel.prev);
        sentinel.prev = sentinel.prev.next;
        size += 1;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return sentinel.next == sentinel;
    }

    public T removeFirst() {
        if (this.isEmpty()) {
            return null;
        }
        T removed = sentinel.next.item;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        size -= 1;
        return removed;
    }

    public T removeLast() {
        if (this.isEmpty()) {
            return null;
        }
        T removed = sentinel.prev.item;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        size -= 1;
        return removed;
    }

    private T getRecursiveHelper(ListNode p, int index) {
        if (index == 0) {
            if (p.next == sentinel) {
                return null;
            }
            return p.next.item;
        }
        return getRecursiveHelper(p.next, index - 1);
    }

    public T getRecursive(int index) {
        return getRecursiveHelper(sentinel, index);
    }

    public T get(int index) {
        ListNode p = sentinel.next;
        while (index > 0) {
            if (p == sentinel) {
                return null;
            } else {
                p = p.next;
                index -= 1;
            }
        }
        return p.item;
    }

    public void printDeque() {
        ListNode p = sentinel;
        while (p.next != sentinel) {
            System.out.print(p.next.item + " ");
            p = p.next;
        }
    }

}
