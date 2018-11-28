import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Performance requirements.  Your randomized queue implementation must support each randomized
 * queue operation (besides creating an iterator) in constant amortized time. That is, any sequence
 * of m randomized queue operations (starting from an empty queue) must take at most cm steps in the
 * worst case, for some constant c. A randomized queue containing n items must use at most 48n + 192
 * bytes of memory. Additionally, your iterator implementation must support operations next() and
 * hasNext() in constant worst-case time; and construction in linear time; you may (and will need
 * to) use a linear amount of extra memory per iterator.
 *
 * @param <Item> the data type for this ADT
 */
public class RandomizedQueue<Item> implements Iterable<Item> {

    private final DynamicArray<Item> array = new DynamicArray<>();

    /**
     * construct an empty randomized queue
     */
    public RandomizedQueue() {
    }

    /**
     * unit testing (optional)
     *
     * @param args arguments
     */
    public static void main(String[] args) {
        int num = 1000;
        int[] hist = new int[num];
        RandomizedQueue<Integer> randomizedQueue = new RandomizedQueue<>();
        for (int i = 0; i < num; i++) {
            randomizedQueue.enqueue(i);
        }

        for (int i : randomizedQueue) {
            hist[i]++;
        }

        System.out.println(Arrays.toString(hist));
    }

    /**
     * Add the item
     * <p>Throw a java.lang.IllegalArgumentException if the client calls enqueue() with a
     * null argument.
     */
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("null items are not allowed!");
        }
        array.addLast(item);
    }

    /**
     * Is the randomized queue empty?
     */
    public boolean isEmpty() {
        return array.size == 0;
    }

    /**
     * Return the number of items on the randomized queue
     */
    public int size() {
        return array.size;

    }

    @Override
    public String toString() {
        return "RandomizedQueue{" +
                "array=" + array +
                '}';
    }

    /**
     * remove and return a random item Throw a java.util.NoSuchElementException if the client calls
     * either sample() or dequeue() when the randomized queue is empty.
     */
    public Item dequeue() {
        if (array.size == 0) {
            throw new NoSuchElementException("Empty array!");
        }
        array.randomSwapLast();
        return array.removeLast();

    }

    /**
     * Return a random item (but do not remove it) Throw a java.util.NoSuchElementException if the
     * client calls either sample() or dequeue() when the randomized queue is empty.
     */
    public Item sample() {
        if (array.size == 0) {
            throw new NoSuchElementException("Empty array!");
        }
        array.randomSwapLast();
        return array.get(array.size - 1);
    }

    /**
     * <p>Each iterator must return the items in uniformly random order. The order of two or more
     * iterators to the same randomized queue must be mutually independent; each iterator must
     * maintain its own random order.
     * <p>
     * Corner cases.  Throw the specified exception for the following corner cases:
     * <ul>
     * <li>Throw a java.lang.IllegalArgumentException if the client calls enqueue() with a null
     * argument.
     * <li>Throw a java.util.NoSuchElementException if the client calls either sample() or
     * dequeue() when the randomized queue is empty.
     * <li>Throw a java.util.NoSuchElementException if the client calls the next() method in the
     * iterator when there are no more items to return.
     * <li>Throw a java.lang.UnsupportedOperationException if the client calls the remove() method
     * in the iterator.
     * </ul>
     * <p>Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator<Item>(this);
    }

    /**
     * Dynamic array (LIFO)
     *
     * @param <T> the data type
     */
    private static class DynamicArray<T> {
        private T[] array;
        private int size;
        public DynamicArray() {
            this.array = createArray(1);
            this.size = 0;
        }

        private T[] createArray(int newSize) {
            return (T[]) new Object[newSize];
        }

        @Override
        public String toString() {
            return "DynamicArray{" +
                    "array=" + Arrays.toString(array) +
                    ", size=" + size +
                    '}';
        }

        /**
         * Swaps the last element with a random element
         */
        private void randomSwapLast() {
            int index1 = StdRandom.uniform(0, size);
            int index2 = size - 1;
            swap(index1, index2);
        }

        /**
         * Swaps two elements
         *
         * @param index1 first element
         * @param index2 second element
         */
        private void swap(int index1, int index2) {
            T tmp = this.array[index1];
            this.array[index1] = this.array[index2];
            this.array[index2] = tmp;
        }

        public T get(int index) {
            if (index < 0) {
                throw new IllegalArgumentException("index should be >= 0");
            }
            else if (index >= size) {
                throw new IllegalArgumentException("index should be < size");
            }
            return array[index];
        }

        public void addLast(T value) {
            if (size == array.length) {
                changeSize(array.length * 2);
            }
            array[size++] = value;
        }

        private void changeSize(int newSize) {
            if (newSize < this.size) {
                throw new IllegalArgumentException("newSize should be > size");
            }

            T[] newArray = createArray(newSize);
            System.arraycopy(this.array, 0, newArray, 0, size);

            for (int i = 0; i < size; i++) {
                this.array[i] = null;
            }
            this.array = newArray;
        }

        public T removeLast() {
            if (size == 0) {
                throw new IllegalArgumentException("Empty array!");
            }

            T value = array[--size];
            array[size] = null;

            if (size < array.length / 2) {
                changeSize(array.length / 2);
            }
            return value;
        }

    }

    private class RandomizedQueueIterator<Item> implements Iterator<Item> {

        private final RandomizedQueue<Item> queue;
        private final int[] order;
        private int size;

        public RandomizedQueueIterator(RandomizedQueue<Item> queue) {
            this.queue = queue;
            this.order = new int[queue.size()];
            for (int i = 0; i < queue.size(); i++) {
                this.order[i] = i;
            }
            this.size = this.order.length;
            StdRandom.shuffle(order);
        }

        /**
         * Returns {@code true} if the iteration has more elements. (In other words, returns {@code
         * true} if {@link #next} would return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        @Override
        public boolean hasNext() {
            return size > 0;
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Empty iterator!");
            }
            return queue.array.get(order[--size]);
        }
    }
}
