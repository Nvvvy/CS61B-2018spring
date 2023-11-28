public class LinkedListDeque<ArbitraryType> {

    private class ListNode {
        public ArbitraryType item;
        public ListNode next;
        public ListNode prev;

        public ListNode(ArbitraryType i, ListNode n, ListNode p) {
            item = i;
            next = n;
            prev = p;
        }

        public ArbitraryType getRecursive(int i) {
            if (i == 0) {
                return item;
            }
            return next.getRecursive(i - 1);
        }
    }

    public ListNode sentinel = new ListNode((ArbitraryType) "sentinel", null, null);
    public int size;

    public LinkedListDeque(ArbitraryType x) {
        sentinel.next = new ListNode(x, sentinel, sentinel);
        sentinel.prev = sentinel.next;
        size = 1;
    }

    public LinkedListDeque() {
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    public void addFirst(ArbitraryType x) {
        sentinel.next = new ListNode(x, sentinel.next, sentinel);
        sentinel.next.prev = sentinel;
        size += 1;
    }

    public void addLast(ArbitraryType x) {
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

    public ArbitraryType removeFirst() {
        if (this.isEmpty()) {
            return null;
        }
        ArbitraryType removed = sentinel.next.item;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        size -= 1;
        return removed;
    }

    public ArbitraryType removeLast() {
        if (this.isEmpty()) {
            return null;
        }
        ArbitraryType removed = sentinel.prev.item;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        size -= 1;
        return removed;
    }

    public ArbitraryType getRecursive(int index) {
        ListNode p = sentinel.next;
        if (index == 0) {
            return p.item;
        }
        return p.next.getRecursive(index - 1);
    }

    public ArbitraryType get(int index) {
        ListNode p = sentinel.next;
        while (index > 0) {
            p = p.next;
            index -= 1;
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
        LinkedListDeque<String> l = new LinkedListDeque<>("hello");
//        LinkedListDeque k = new LinkedListDeque();
        l.addLast("world");
        l.addFirst("v, ");
        System.out.println(l.getRecursive(2));
        String removed = l.removeFirst();
        l.removeLast();
        l.addLast("world!");
        System.out.println(l.size());
        l.printDeque();
    }
}
