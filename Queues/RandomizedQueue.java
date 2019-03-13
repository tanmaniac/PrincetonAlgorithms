/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] buffer;
    private int size = 0;

    private class ListIterator<Item> implements Iterator<Item> {
        private final int[] indices;
        private int idx;

        public ListIterator() {
            indices = new int[size()];
            for (int i = 0; i < size(); i++) {
                indices[i] = i;
            }

            StdRandom.shuffle(indices);

            idx = 0;
        }

        public boolean hasNext() {
            return idx < size;
        }

        public void remove() {
            throw new java.lang.UnsupportedOperationException("Can't call remove on this iterator");
        }

        public Item next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }

            Item val = (Item) buffer[indices[idx]];
            idx++;
            return val;
        }
    }

    public RandomizedQueue() {
        buffer = (Item[]) new Object[1];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void enqueue(Item item) {
        if (item == null) {
            throw new java.lang.IllegalArgumentException("Tried to add a null value to the queue!");
        }

        // Determine if there's space
        if (buffer.length < size * 2) {
            // Create a new buffer and copy the values
            Item[] tmpBuffer = (Item[]) new Object[buffer.length * 2];
            for (int i = 0; i < size; i++) {
                tmpBuffer[i] = buffer[i];
            }
            buffer = tmpBuffer;
        }
        // Insert the value
        buffer[size] = item;
        size++;
    }

    public Item dequeue() {
        // Check if we even have any values
        if (size == 0) {
            throw new java.util.NoSuchElementException("Queue is empty!");
        }

        // Fetch a random value
        int randomIdx = StdRandom.uniform(0, size);
        Item result = buffer[randomIdx];
        buffer[randomIdx] = buffer[size - 1];

        // Resize buffer if necessary
        if (buffer.length >= size * 4) {
            Item[] tmpBuffer = (Item[]) new Object[size * 2];
            for (int i = 0; i < size - 1; i++) {
                tmpBuffer[i] = buffer[i];
            }
            buffer = tmpBuffer;
        }

        size--;
        return result;
    }

    public Item sample() {
        if (size == 0) {
            throw new java.util.NoSuchElementException("Queue is empty!");
        }

        int randomIdx = StdRandom.uniform(0, size);
        return buffer[randomIdx];
    }

    public Iterator<Item> iterator() {
        return new ListIterator<>();
    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> random = new RandomizedQueue<>();

        random.enqueue(453);
        random.dequeue();
        random.enqueue(242);
    }
}
