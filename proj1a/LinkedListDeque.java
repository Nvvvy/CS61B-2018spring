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

        public T getRecursive(int i) {
            if (i == 0) {
                return item;
            }
            return next.getRecursive(i - 1);
        }
    }

    private ListNode sentinel = new ListNode((T) "sentinel", null, null);
    private int size;

//    public LinkedListDeque(T x) {
//        sentinel.next = new ListNode(x, sentinel, sentinel);
//        sentinel.prev = sentinel.next;
//        size = 1;
//    }

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

    public T getRecursive(int index) {
        ListNode p = sentinel.next;
        if (index == 0) {
            return p.item;
        }
        return p.next.getRecursive(index - 1);
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

    public static void main(String[] args) {
//        LinkedListDeque<String> l = new LinkedListDeque<>("hello");

//        l.addLast("world");
//        l.addFirst("v, ");
//        System.out.println(l.getRecursive(2));
//        String removed = l.removeFirst();
//        l.removeLast();
//        l.addLast("world!");
//        System.out.println(l.size());
//
//        boolean r = l.isEmpty();
//        l.printDeque();

        LinkedListDeque k = new LinkedListDeque();
        k.addFirst(0);
        k.addFirst(1);
        k.addFirst(2);
        k.addFirst(3);
        k.removeLast();
        boolean rr = k.isEmpty();
        k.printDeque();
    }
}
