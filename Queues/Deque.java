/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private int length = 0;
    private ListNode<Item> head;
    private ListNode<Item> tail;

    private class ListNode<Item> {
        public Item val;
        public ListNode<Item> prev;
        public ListNode<Item> next;

        public ListNode(Item item) {
            val = item;
            prev = null;
            next = null;
        }
    }

    private class ListIterator implements Iterator<Item> {
        private ListNode<Item> current = head;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new java.lang.UnsupportedOperationException("Can't call remove on this iterator");
        }

        public Item next() {
            if (current == null) {
                throw new java.util.NoSuchElementException();
            }

            Item item = current.val;
            current = current.next;
            return item;
        }
    }

    public Deque() {
    }

    public boolean isEmpty() {
        return length == 0;
    }

    public int size() {
        return length;
    }

    public void addFirst(Item item) {
        if (item == null) {
            throw new java.lang.IllegalArgumentException("Input item was null!");
        }

        ListNode<Item> temp = new ListNode<Item>(item);
        temp.next = head;
        if (head != null) {
            head.prev = temp;
        }
        head = temp;
        if (tail == null) {
            // List was previously empty
            tail = head;
        }
        length++;
    }

    public void addLast(Item item) {
        if (item == null) {
            throw new java.lang.IllegalArgumentException("Input item was null!");
        }

        ListNode<Item> temp = new ListNode<Item>(item);
        temp.prev = tail;
        if (tail != null) {
            tail.next = temp;
        }
        tail = temp;
        if (head == null) {
            // List was previously empty
            head = tail;
        }
        length++;
    }

    public Item removeFirst() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }

        Item result = head.val;
        head.prev = null;
        head = head.next;
        length--;
        return result;
    }

    public Item removeLast() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }

        Item result = tail.val;
        tail.next = null;
        tail = tail.prev;
        length--;
        return result;
    }

    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    public static void main(String[] args) {
        Deque<Integer> d = new Deque<>();

        d.addFirst(1);
        d.removeLast();
    }
}
