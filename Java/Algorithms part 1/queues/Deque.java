import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node first, last;
    private int size;

    private class Node {
        Item item;
        Node next;
    }

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        if (size <= 0)
            return true;
        return false;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null)
            throw new IllegalArgumentException();

        // Create a new node and assign its item
        Node newNode = new Node();
        newNode.item = item;

        // Check if first is null (list is empty), else add the new node ahead of the current first
        if (first == null || last == null) {
            first = newNode;
            last = first;
        } else {
            newNode.next = first;
            first = newNode;
        }
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null)
            throw new IllegalArgumentException();

        Node newNode = new Node();
        newNode.item = item;

        // Check if first is null (list is empty), else add the new node behind the current last
        if (first == null || last == null) {
            first = newNode;
            last = first;
        } else {
            last.next = newNode;
            last = newNode;
        }
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty())
            throw new NoSuchElementException();

        Node removed = first;
        first = first.next;
        size--;

        // Check if the queue is now empty, if so make everything null
        if (isEmpty())
            last = null;
        return removed.item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty())
            throw new NoSuchElementException();

        Node removed = last;
        Node current = first;

        if (first == last) {
            first = null;
            last = null;
            size--;
            return removed.item;
        }

        while (current != null && current.next != null && current.next.next != null) {
            current = current.next;
        }

        if (current == null)
            throw new java.util.NoSuchElementException("Queue is empty!");

        last = current;
        last.next = null;
        size--;
        return removed.item;
    }

    //--------------------------------------------------------------------------------------------------------------------------------------------------------------
    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new Iter();
    }

    // Iterator class with implemented methods from interface
    private class Iter implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();

            Item item = current.item;
            current = current.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    //--------------------------------------------------------------------------------------------------------------------------------------------------------------
    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<Integer>();
        StdOut.println(String.format("is empty? %1$s", deque.isEmpty()));
        StdOut.println(String.format("size? %1$s", deque.size()));
        StdOut.println("Adding three items");
        deque.addFirst(1);
        deque.addLast(2);
        deque.addLast(3);
        Iterator<Integer> iter = deque.iterator();
        int i = 1;
        StdOut.println("List items:");
        while (iter.hasNext()) {
            StdOut.println(String.format("#%1$s: %2$s", i++, iter.next()));
        }
        StdOut.println(String.format("size? %1$s", deque.size()));

        StdOut.println("Remove the first item");
        deque.removeFirst();
        Iterator<Integer> iter2 = deque.iterator();
        i = 1;
        StdOut.println("List items:");
        while (iter2.hasNext()) {
            StdOut.println(String.format("#%1$s: %2$s", i++, iter2.next()));
        }

        StdOut.println("Remove the last item");
        deque.removeLast();
        Iterator<Integer> iter3 = deque.iterator();
        i = 1;
        StdOut.println("List items:");
        while (iter3.hasNext()) {
            StdOut.println(String.format("#%1$s: %2$s", i++, iter3.next()));
        }
        StdOut.println(String.format("is empty? %1$s", deque.isEmpty()));
        StdOut.println(String.format("size? %1$s", deque.size()));
    }
}
