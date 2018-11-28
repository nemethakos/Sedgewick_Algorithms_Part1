import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>A double-ended queue or deque (pronounced "deck") is a generalization of a stack and a queue
 * that supports adding and removing items from either the front or the back of the data structure.
 *
 * <p></p>Corner cases.  Throw the specified exception for the following corner cases:
 *
 * <ul>
 * <li>Throw a java.lang.IllegalArgumentException if the client calls either addFirst() or
 * addLast() with a null argument.
 * <li>Throw a java.util.NoSuchElementException if the client calls either removeFirst() or
 * removeLast when the deque is empty.
 * <li>Throw a java.util.NoSuchElementException if the client calls the next() method in the
 * iterator when there are no more items to return.
 * <li>Throw a java.lang.UnsupportedOperationException if the client calls the remove() method in
 * the iterator.
 * </ul>
 *
 * <p>Performance requirements:
 * <ul>
 * <li>Your deque implementation must support each deque operation
 * (including construction) in constant worst-case time.
 * <li>A deque containing n items must use at most 48n + 192 bytes of memory and use space
 * proportional to the number of items currently in the deque.
 * <li>Additionally, your iterator implementation must support each operation (including
 * construction) in constant worst-case time.
 * </ul>
 *
 * @param <Item> the data type
 */
public class Deque<Item> implements Iterable<Item> {

    private int size;
    private final Node<Item> head;
    private final Node<Item> tail;

    // construct an empty deque
    public Deque() {
        // dummy node
        head = new Node<>(null);
        // dummy tail
        tail = new Node<>(null);

        head.next = tail;
        head.prev = null;
        tail.prev = head;
        tail.next = null;

    }

    private void insertAfter(Node<Item> node, Node<Item> newNode) {
        Node<Item> next = node.next;
        node.next = newNode;
        next.prev = newNode;
        newNode.next = next;
        newNode.prev = node;
    }

    private void insertBefore(Node<Item> node, Node<Item> newNode) {
        Node<Item> prev = node.prev;
        node.prev = newNode;
        newNode.next = node;
        newNode.prev = prev;
        prev.next = newNode;
    }

    private Node<Item> removeAfter(Node<Item> node) {
        if (node == null || node == tail || node == tail.prev) {
            throw new IllegalArgumentException("Cannot remove tail or tail.prev: " + node);
        }
        Node<Item> removed = node.next;
        Node<Item> next = node.next.next;
        removed.prev = null;
        removed.next = null;
        node.next = next;
        next.prev = node;

        return removed;
    }

    private Node<Item> removeBefore(Node<Item> node) {
        if (node == null || node == head || node == head.next) {
            throw new IllegalArgumentException("Cannot remove node or node.prev: " + node);
        }
        Node<Item> prev = node.prev.prev;
        Node<Item> removed = node.prev;
        removed.next = null;
        removed.prev = null;
        node.prev = prev;
        prev.next = node;

        return removed;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    /**
     * <p>Add the item to the front.
     * <p>Throw a java.lang.IllegalArgumentException if the client calls
     * either addFirst() or addLast() with a null argument.
     *
     * @param item the data to add to the queue
     */
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Null argument is illegal!");
        }
        insertBefore(tail, new Node<Item>(item));
        size++;
    }

    /**
     * <p>Add the item to the end.
     * <p>Throw a java.lang.IllegalArgumentException if the client calls
     * either addFirst() or addLast() with a null argument.
     *
     * @param item the data to add to the queue
     */
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Null argument is illegal!");
        }
        insertAfter(head, new Node<Item>(item));
        size++;
    }

    /**
     * <p>Remove and return the item from the front
     * <p>Throw a java.util.NoSuchElementException if the
     * client calls either removeFirst() or removeLast when the deque is empty.
     *
     * @return the data from the from
     */
    public Item removeFirst() {
        if (size == 0) {
            throw new NoSuchElementException("Queue is empty!");
        }
        size--;
        return removeBefore(tail).value;
    }

    /**
     * <p>Remove and return the item from the end
     * <p>Throw a java.util.NoSuchElementException if the
     * client calls either removeFirst() or removeLast when the deque is empty.
     *
     * @return the data from the back
     */
    public Item removeLast() {
        if (size == 0) {
            throw new NoSuchElementException("Queue is empty!");
        }
        size--;
        return removeAfter(head).value;
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new DequeIterator(this);
    }

    // unit testing (optional)
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();

        for (int i = 1; i <= 10; i++) {
            deque.addLast(i);
        }

        for (int i : deque) {
            System.out.println(i);
        }

        for (int i = 1; i <= 10; i++) {
            System.out.println("queue:" + deque.removeFirst());
        }

        for (int i = 1; i <= 10; i++) {
            deque.addFirst(i);
        }
        for (int i = 1; i <= 10; i++) {
            System.out.println("stack:" + deque.removeFirst());
        }

    }

    private class DequeIterator implements Iterator<Item> {

        private final Deque<Item> deque;

        private Node<Item> node;

        public DequeIterator(Deque<Item> deque) {
            this.deque = deque;
            this.node = deque.tail.prev;
        }

        @Override
        public boolean hasNext() {
            return this.node != this.deque.head;
        }

        @Override
        /**
         * Throw a java.util.NoSuchElementException if the client calls the next()
         * method in the iterator when there are no more items to return.
         */
        public Item next() {
            if (this.node == this.deque.head) {
                throw new NoSuchElementException("No more elements!");
            }
            Item result = this.node.value;
            this.node = this.node.prev;
            return result;
        }
    }

    /**
     * Doubly linked list node
     *
     * @param <Item> data type
     */
    private class Node<Item> {
        private Node<Item> prev;
        private Node<Item> next;
        private Item value;

        public Node(Item value) {
            this.value = value;
        }


        public Node<Item> getNext() {
            return next;
        }

        public void setNext(Node<Item> next) {
            this.next = next;
        }

        public Item getValue() {
            return value;
        }

        public void setValue(Item value) {
            this.value = value;
        }

        public Node<Item> getPrev() {
            return prev;
        }

        public void setPrev(Node<Item> prev) {
            this.prev = prev;
        }
    }
}
