import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private static final int DEFAULT_CAPACITY = 2;
    private Item[] items;
    private int size;

    // construct an empty randomized queue
    public RandomizedQueue() {
        items = (Item[]) new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size <= 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null)
            throw new IllegalArgumentException();

        if (size == items.length)
            resize(items.length * 2);
        
        items[size++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty())
            throw new NoSuchElementException();

//        int randomIndex = (int) (Math.random() * (size));
        int randomIndex = StdRandom.uniformInt(size);
        Item item = items[randomIndex]; // grab value to return
        items[randomIndex] = items[--size]; // "fill in" the empty space with the last item of the items array/queue and decrement size
        items[size] = null; // make the last item null

        if (size == items.length / 4)
            resize(items.length / 2);

        return item;
    }

    private void resize(int newCapacity) {
        Item[] tempItems = (Item[]) new Object[newCapacity];

        int n = 0;
        for (Item current : items) {
            tempItems[n] = items[n];
            n++;
        }

        items = tempItems;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty())
            throw new NoSuchElementException();

        return items[StdRandom.uniformInt(size)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new Iter(size);
    }

    private class Iter implements Iterator<Item> {
        int[] randomIndexes; // Make an array with the values being the random order indexes to access the "items" queue/array
        int current;

        private Iter(int size) {
            current = size;
            randomIndexes = StdRandom.permutation(size);
        }

        public boolean hasNext() {
            return current != 0;
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();

            Item returnVal = items[randomIndexes[--current]];
            items[randomIndexes[current]] = null; // Avoids Loitering (Garbage Collector can reclaim memory)
            return returnVal;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<String>();
        randomizedQueue.enqueue("A 1");
        randomizedQueue.enqueue("B 2");
        randomizedQueue.enqueue("C 3");
        randomizedQueue.enqueue("D 4");
        randomizedQueue.enqueue("E 5");
        StdOut.println("Size: " + randomizedQueue.size());
        StdOut.println("Deque: " + randomizedQueue.dequeue());
        StdOut.println("Size :" + randomizedQueue.size());
        StdOut.println("Sample: " + randomizedQueue.sample());
        StdOut.println("Size: " + randomizedQueue.size());

        StdOut.println("Entire queue: ");
        for (String s : randomizedQueue)
            StdOut.println(s);
    }
}