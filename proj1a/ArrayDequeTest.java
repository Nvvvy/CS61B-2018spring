public class ArrayDequeTest {
    public static void main(String[] args) {
        ArrayDeque k = new ArrayDeque();
        System.out.println(k.isEmpty());
//        k.addLast(0);
//        k.addLast(1);
//        k.addLast(2);
//        k.addLast(3);
//        k.addLast(4);
//        k.addLast(5);
//        k.addLast(6);
//        k.addLast(7);
//        k.addLast(8);
//        k.addLast(9);
//        k.addLast(10);
//        k.addLast(11);
//        k.addLast(12);
//        k.addLast(13);
//        k.addLast(14);
//        k.addLast(15);
//        k.addLast(16);
//        Object a = k.get(8);
//        Object b = k.removeFirst();
//        Object c = k.removeLast();

        k.addLast(0);
        Object a = k.get(0); //     ==> 0
        Object b = k.removeLast(); //      ==> 0
        k.addLast(3);
        k.addFirst(4);
        Object a1 = k.removeFirst();  //   ==> 4
        k.addFirst(6);
        Object a2 = k.removeLast();  //    ==> 3
        k.addLast(8);
        k.addFirst(9);
        k.addFirst(10);
        k.addLast(11);
        k.addLast(12);
        Object a3 = k.removeFirst(); //    ==> 10
        Object a4 = k.get(4);   //   ==> 12
        k.addLast(15);
        k.addLast(16);
        k.addLast(17);
        k.addLast(18);
        k.addLast(19);
        k.addLast(20);
        Object a5 = k.removeLast();   //   ==> 20
        Object a6 = k.get(5); //     ==> 15

//        k.addFirst(0);
//        k.addFirst(1);
//        k.addFirst(2);
//        k.addFirst(3);
//        k.addFirst(4);
//        k.addFirst(5);
//        k.addFirst(6);
//        k.addFirst(7);
//        k.addFirst(8);
//        k.removeFirst();
//        k.removeFirst();
//        k.removeFirst();
//        k.removeFirst();
//        k.removeFirst();
//        k.removeFirst();//here
//        k.removeFirst();
//        k.removeFirst();
//        k.removeFirst();
//        k.addFirst(0);
//        k.addFirst(1);
//        k.addFirst(2);
//        k.addFirst(3);
//        k.addFirst(4);
//        k.addFirst(5);
//        k.addFirst(6);
//        k.addFirst(7);
//        k.addFirst(8);


        k.printDeque();
    }
}
