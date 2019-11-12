/* Starter code for LP2 */

/**
 * @author Nihal Abdulla PT - nxp171730
 * @author Amal Mohan - axm179030
 * @author Mihir Hindocha - mxh170027
 * Implementing Skip List Data Structure. The main operations are:
 * add(x): Add a new element x to the list. If x already exists in the skip list, replace it and return false.  Otherwise, insert x into the skip list and return true.
 * ceiling(x): Find smallest element that is greater or equal to x.
 * contains(x): Does list contain x?
 * first(): Return first element of list.
 * floor(x): Find largest element that is less than or equal to x.
 * get(n): Return element at index n of list.  First element is at index 0. Call either getLinear or getLog.
 * getLinear(n): O(n) algorithm for get(n).
 * getLog(n): O(log n) expected time algorithm for get(n). This method is optional, but code it correctly to earn EC.
 * isEmpty(): Is the list empty?
 * iterator(): Iterator for going through the elements of list in sorted order.
 * last(): Return last element of list.
 * rebuild(): Reorganize the elements of the list into a perfect skip list.
 * remove(x): Remove x from the list. If successful, removed element is returned.  Otherwise, return null.
 * size(): Return the number of elements in the list.
 */

package nxp171730;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;


/**
 * Class defining the skip list and all its functions.
 */
public class SkipList<T extends Comparable<? super T>> implements Iterable<T> {
    static final int PossibleLevels = 33;

    /**
     * Entry class represents the elements of the skip list.
     */
    static class Entry<E> {
        E element;
        Entry<E>[] next;
        int[] span;
        Entry<E> prev;

        /**
         * This constructor creates an object for the Entry class with parameters being the element, level and span level.
         *
         * @param x       the value for the element.
         * @param lev     the level to which next in initialized.
         * @param spanLev the length of the span[].
         */
        public Entry(E x, int lev, int spanLev) {
            element = x;
            next = new Entry[lev];
            this.span = new int[spanLev];

        }

        /**
         * A helper method to retuen the value of element.
         */
        public E getElement() {
            return element;
        }
    }

    /**
     * This defines the dummy head and tail for the skip list. These will be the two extreme ends of the skip list.
     */
    private Entry<T> head, tail; // Dummy nodes.

    // Stores the number of elements in the skip list.
    private int size;

    // Stores the maximum size of the next[] with the element.
    private int maxLevel;

    // Array of Entry used in the find(x) function.
    private Entry<T>[] last;
    private Random random;

    /**
     * This constructor creates an object for the SkipList class.
     */
    public SkipList() {
        this.head = new Entry<>(null, 33, 33);
        this.tail = new Entry<>(null, 33, 33);
        this.size = 0;
        this.maxLevel = 1;
        this.last = (Entry<T>[]) new Entry[33];
        this.random = new Random();
        for (int i = 0; i < 33; i++) {
            this.head.next[i] = this.tail;
            this.last[i] = this.head;
            this.head.span[i] = 0;
            this.tail.next[i] = null;
        }
    }

    /**
     * A helper method to check if the last or last[index] is a null.
     *
     * @param index int value at which the value is checked.
     * @return true if last and last[index] are not null, false otherwise.
     * @throws ArrayIndexOutOfBoundsException when the index is not between 0-32.
     */
    private boolean checkLastForIndex(int index) {
        if (index < 0 || index > 32)
            throw new ArrayIndexOutOfBoundsException();
        return this.last != null && this.last[index] != null;
    }

    /**
     * A helper method to check if the last[index].next or last[index].next[index] is null. It uses checkLastForIndex(index) function.
     *
     * @param index int value at which the value is checked
     * @return true if last[index].next and last[index].next[index] are not null, false otherwise.
     * @throws ArrayIndexOutOfBoundsException when the index is not between 0-32.
     */
    private boolean checkNextInLastForIndex(int index) {
        if (index < 0 || index > 32)
            throw new ArrayIndexOutOfBoundsException();
        return this.checkLastForIndex(index) && this.last[index].next != null && this.last[index].next[index] != null;
    }

    /**
     * A helper method to check if the last[index].element is null. It uses checkLastForIndex(index) function.
     *
     * @param index int value at which the value is checked
     * @return true if last[index].element is not null, false otherwise.
     * @throws ArrayIndexOutOfBoundsException when the index is not between 0-32.
     */
    private boolean checkElementInLastForIndex(int index) {
        if (index < 0 || index > 32)
            throw new ArrayIndexOutOfBoundsException();
        return this.checkLastForIndex(index) && this.last[index].getElement() != null;
    }

    /**
     * A helper method to check if the last[index].next[index].element is null. It uses checkNextInLastForIndex(index) function.
     *
     * @param index int value at which the value is checked
     * @return true if last[index].next[index].element is not null, false otherwise.
     * @throws ArrayIndexOutOfBoundsException when the index is not between 0-32.
     */
    private boolean checkElementInNextInLastForIndex(int index) {
        if (index < 0 || index > 32)
            throw new ArrayIndexOutOfBoundsException();
        return this.checkNextInLastForIndex(index) && this.last[index].next[index].getElement() != null;
    }

    /**
     * A helper method to get the value stored at last[index].element.
     *
     * @param index int value at which the value is stored.
     * @return the value stored at last[index].element.
     * @throws ArrayIndexOutOfBoundsException when the index is not between 0-32.
     */
    private T getElementAtIndexFromLast(int index) {
        if (index < 0 || index > 32)
            throw new ArrayIndexOutOfBoundsException();
        return this.last[index].getElement();
    }

    /**
     * A helper method to get the value stored at last[index].next[index].element.
     *
     * @param index int value at which the value is stored.
     * @return the value stored at last[index].next[index].element.
     * @throws ArrayIndexOutOfBoundsException when the index is not between 0-32.
     */
    private T getElementAtIndexFromNextInLast(int index) {
        if (index < 0 || index > 32)
            throw new ArrayIndexOutOfBoundsException();
        return (T) this.last[index].next[index].getElement();
    }

    /**
     * A helper method to search for element x in the SkipList. This method sets the last[i] = node at which the search came down to from level i to i-1.
     *
     * @param x the element to be searched in the SkipList
     */
    private void find(T x) {
        Entry<T> p = this.head;
        for (int i = this.maxLevel - 1; i >= 0; i--) {
            while (p.next[i] != null && p.next[i].getElement() != null && ((T) p.next[i].getElement()).compareTo(x) < 0)
                p = p.next[i];
            this.last[i] = p;
        }
    }

    /**
     * A helper method to check if the element x in present in the SkipList. This method uses the find(x) method.
     *
     * @param x the element to be checked in the SkipList.
     * @return true if list contains x, false otherwise.
     */
    public boolean contains(T x) {
        this.find(x);
        return this.checkNextInLastForIndex(0) && x.equals(this.last[0].next[0].getElement());
    }

    /**
     * A method defined to generate a random level for the element in the list. Prob(choosing level i) = 1/2 Prob(choosing level i-1)
     *
     * @return a randomly generated int value for level
     */
    private int chooseLevel() {
        int lev = 1 + Integer.numberOfTrailingZeros(this.random.nextInt());
        if (lev > this.maxLevel)
            this.maxLevel = lev;
        return lev;
    }

    /**
     * Method defined to add element x to the list. If the element already exists, x is rejected. It uses contains(x) method to check if the element already exists.
     *
     * @param x value to be entered in the list
     * @return true if the element is added to list, false otherwise
     */
    public boolean add(T x) {
        if (this.contains(x))
            return false;
        int lev = this.chooseLevel(); // Length of next[] for x's entry generated randomly.
        Entry<T> ent = new Entry<>(x, lev, lev);
        for (int i = 0; i < lev; i++) {
            if (this.checkLastForIndex(i)) {
                ent.next[i] = this.last[i].next[i];
                this.last[i].next[i] = ent;
                int len = 0;
                for (int j = 0; j <= i; j++) {
                    if (last[j] != last[j + 1]) {
                        len += last[j].span[j];
                    }
                }

                ent.span[i] = i != 0 ? this.last[i].span[i] - len : 1;
                last[i].span[i] = len;
            } else {
                ent.next[i] = this.tail;
            }
        }
        for (int i = lev; i < maxLevel; i++) {
            if (this.checkLastForIndex(i)) {
                last[i].span[i]++;
            }
        }
        this.size++;
        return true;
    }

    /**
     * Method to find the smallest element that is greater than or equal to x in the SkipList.
     *
     * @param x the element to be checked with.
     * @return x if it exists or the smallest element greater than the value of x.
     * @throws NoSuchElementException if there is no element matching the requirement.
     */
    public T ceiling(T x) {
        this.find(x);
        if (!checkElementInNextInLastForIndex(0))
            throw new NoSuchElementException();
        return getElementAtIndexFromNextInLast(0);
    }

    /**
     * Method returns the first element of the SkipList.
     *
     * @return first element in the SkipList.
     * @throws NoSuchElementException if the SkipList is empty.
     */
    public T first() {
        if (this.isEmpty())
            throw new NoSuchElementException();
        return (T) this.head.next[0].getElement();
    }

    /**
     * Method to find the largest element that is less than or equal to x in the SkipList.
     *
     * @param x the element to be checked with.
     * @return x if it exists or the largest element less than the value of x.
     * @throws NoSuchElementException if there is no element matching the requirement.
     */
    public T floor(T x) {
        if (this.isEmpty())
            throw new NoSuchElementException();
        this.find(x);
        if (this.checkNextInLastForIndex(0) && x.equals(this.last[0].next[0].getElement()))
            return x;
        if (!checkElementInLastForIndex(0))
            throw new NoSuchElementException();
        return getElementAtIndexFromLast(0);
    }

    /**
     * Method returns the element present at index n. It uses the getLinear function. First element is at index 0.
     *
     * @param n value of index at which element has to be checked.
     * @return the element found at index n if it exists.
     */
    public T get(int n) {
        return this.getLinear(n);
    }

    /**
     * The method uses an O(n) algorithm for get(n) function.
     *
     * @param n value of index at which element has to be checked.
     * @return element found at index n if it exists.
     * @throws ArrayIndexOutOfBoundsException if element is less than 0 or more than size - 1.
     */
    public T getLinear(int n) {

        if (n < 0 || n > this.size - 1)
            throw new ArrayIndexOutOfBoundsException();
        Entry<T> p = this.head;
        for (int k = 0; k <= n; k++) {
            p = p.next[0];
        }
        return p.getElement();
    }

    /**
     * The method uses an O(log n) expected time for get(n) function. Optional operation: Eligible for EC. Requires maintenance of spans.
     *
     * @param n value of index at which element has to be checked.
     * @return element found at index n if it exists.
     */
    public T getLog(int n) {
        if (n > size) {
            return null;
        }
        Entry<T> ele = head;
        n++;
        for (int i = maxLevel - 1; i >= 0; i--) {
            while (i > ele.span[i]) {
                i = i - ele.span[i];
                ele = ele.next[i];
            }
        }
        return ele.getElement();
    }

    /**
     * A helper method to check if the SkipList is empty.
     */
    public boolean isEmpty() {
        return this.head.next[0] == this.tail || this.size() == 0;
    }

    /**
     * Iterator used to iterate through the SkipList by calling the custom SKLIterator.
     */
    public Iterator<T> iterator() {
        return new SKLIterator();
    }

    /**
     * The implementation of the iterator interface.
     */
    protected class SKLIterator implements Iterator<T> {

        // The cursor keeps track of the current level.
        Entry<T> cursor, prev;
        boolean ready;

        // levNodes[i] maintains the most recent node encountered which has i levels.
        private Entry<T>[] levNodes;


        /**
         * This constructor create objects fot SKLIterator class.
         */
        SKLIterator() {
            cursor = head;
            prev = null;
            ready = false;
            levNodes = new Entry[maxLevel];
        }

        /**
         * Method to check if there is any element next by checking that the next element is not the tail.
         *
         * @return true is next element is not the tail, false otherwise.
         */
        public boolean hasNext() {
            return cursor.next[0] != tail;
        }

        /**
         * Method next moves the cursor to the next element. It updates each level i nodes of levNodes with prev if prev has next[i].
         *
         * @return the next element.
         */
        public T next() {
            prev = cursor;
            for (int i = 0; i < maxLevel; i++) {
                if (prev.next.length > i && !checkIfNullOrTailElement(prev.next[i]))
                    levNodes[i] = prev;
            }
            cursor = cursor.next[0];
            ready = true;
            return cursor.getElement();
        }

        /**
         * Method removes element pointed by the cursor. It updates each element at level i of levNodes array if it has a pointer to cursor.
         *
         * @return the next element.
         */
        public void remove() {
            if (!ready) {
                throw new NoSuchElementException();
            }
            for (int i = 0; i < cursor.next.length; i++) {
                // Checks if levNodes[i] points to current element.
                if (!checkIfNullOrTailElement(levNodes[i]) && !checkIfNullOrTailElement(levNodes[i].next[i]) && levNodes[i].next[i] == cursor) {
                    levNodes[i].next[i] = cursor.next[i];
                }
            }
            cursor = prev;
            ready = false;  // Calling remove again without calling next will result in exception thrown.
            size--;
        }
    }  // End of class SKLIterator


    /**
     * Method returns the last element of the skip list. It uses the get(x) function.
     *
     * @return last element in the skip list.
     * @throws NoSuchElementException if the skip list is empty.
     */
    public T last() {
        if (this.isEmpty())
            throw new NoSuchElementException();
        return get(this.size - 1);
    }

    /**
     * A helper method for the rebuild function that returns the maximum level a perfect skip list should have.
     *
     * @return maximum level for a perfect SkipList.
     */
    private int getPerfectSkipListLevels() {
        return Math.min((int) (Math.log(this.size) / Math.log(2)) + 1, 33);
    }

    /**
     * A helper method to check if the element is null or the tail of SkipList.
     *
     * @param element element of the list to be checked.
     * @return true if element is null or the tail of the list, false otherwise.
     */
    private boolean checkIfNullOrTailElement(Entry<T> element) {
        return element == null || element == this.tail;
    }

    /**
     * A helper method to reinitialize the next array to the specified level.
     *
     * @param arr   array of Entry type
     * @param level int value to which next array is reinitialised
     * @return an array of type entry.
     */
    private Entry<T>[] reInitializeNextArray(Entry<T>[] arr, int level) {
        Entry<T>[] tempStorage = new Entry[level + 1];
        int maxIter = Math.min(level + 1, arr.length);
        for (int i = 0; i < maxIter; i++)
            tempStorage[i] = arr[i];
        while (maxIter < (level + 1)) {
            tempStorage[maxIter++] = null;
        }
        return tempStorage;
    }

    //
    //Input Parameter: index of int type
    //Output Parameter: boolean

    /**
     * A helper method to see if last or last[index] is null ( true -> last != null && last[index] != null)
     */
    private Entry<T> updateRunner(Entry<T> runner, Entry<T> updateWith, int level) {
        runner.next[level] = updateWith;
        return updateWith;
    }

    /**
     * A method to reorganise the elements of the list into a perfect skip list. (Optional Operation. Not a standard operation in skip lists. Eligible for EC).
     */
    public void rebuild() {
        if (this.isEmpty())
            throw new NoSuchElementException();
        int pslLevels = this.getPerfectSkipListLevels();
        this.maxLevel = pslLevels;
        System.out.println("Levels: " + pslLevels);
        if (pslLevels == 0)
            return;
        int lev = 0;
        int prevLev = lev - 1;
        while (lev < pslLevels) {
            Entry<T> runner = this.head;
            while (!checkIfNullOrTailElement(runner)) {
                if (runner != this.head && runner.next.length != lev + 1)
                    runner.next = reInitializeNextArray(runner.next, lev);
                if (prevLev == -1) {
                    runner = updateRunner(runner, runner.next[0], lev);
                } else {
                    if (checkIfNullOrTailElement(runner.next[prevLev]) || checkIfNullOrTailElement(runner.next[prevLev].next[prevLev]))
                        break;
                    runner = updateRunner(runner, runner.next[prevLev].next[prevLev], lev);
                }
            }
            if (prevLev != -1 && !checkIfNullOrTailElement(runner)) {
                int levCounter = lev;
                while (runner.next[levCounter] == null) {
                    runner.next[levCounter--] = this.tail;
                }
                runner.next[lev] = this.tail;
            }
            prevLev = lev++;
        }
        int levCounter = this.maxLevel;
        while (levCounter < 33) {
            this.head.next[levCounter++] = this.tail;
        }
    }

    /**
     * Method to remove the element from the skip list. It uses the contains(x) method to check if x is present in the skip list.
     *
     * @param x element to be removed, if present.
     * @return element being removed from the skip list, null otherwise.
     */
    public T remove(T x) {
        if (!this.contains(x))
            return null;
        Entry<T> ent = this.last[0].next[0];
        for (int i = 0; i < ent.next.length; i++) {
            this.last[i].next[i] = ent.next[i];
            for (int j = i; j < this.last[i].span.length; j++) {
                this.last[i].span[j]--;
            }
        }
        this.size--;
        return ent.getElement();
    }

    /**
     * A helper method to return the number of elements in the skip list.
     *
     * @return int number of elements in the list.
     */
    public int size() {
        return this.size;
    }

    /**
     * A helper method to print the elements of the skip list in sorted order.
     */
    public void printSortedList() {
        Entry<T> temp = this.head.next[0];
        while (temp != null && temp.getElement() != null) {
            System.out.print(temp.getElement() + " " + temp.next.length + "->");
            temp = temp.next[0];
        }
    }

    /**
     * Sample Driver to show the various functions working in the Skip list.
     */
    public static void main(String[] args) {
        SkipList<Integer> sl = new SkipList<>();
        System.out.println("IsEmpty: " + sl.isEmpty());
        System.out.println("Skiplist Size: " + sl.size());
        for (int i = 1; i <= 136; i++)
            sl.add(i);
        System.out.println("Added elements 1 to 128 to list (indexed 0 to 99)");
        System.out.println("IsEmpty: " + sl.isEmpty());
        System.out.println("Skiplist Size: " + sl.size());
        System.out.println("Skiplist Maxlevel: " + sl.maxLevel);
        System.out.println("First element at Maxlevel: " + sl.head.next[sl.maxLevel - 1].getElement());
        System.out.println("Skiplist elements and length of next array for each element");
        sl.printSortedList();
        System.out.println();
        System.out.println("First Element: " + sl.first());
        System.out.println("Element at position 2: " + sl.getLinear(2));
        System.out.println("Last Element: " + sl.last());
        System.out.println("Floor(101): " + sl.floor(101));
        System.out.println("Ceiling(0): " + sl.ceiling(0));
        System.out.println("Removed elements {4, 23, 42, 34, 54, 68, 75, 79} using iterator remove method");
        Iterator<Integer> it = sl.iterator();
        while (it.hasNext()) {
            Integer s = it.next();
            if (s == 4 || s == 23 || s == 42 || s == 34 || s == 54 || s == 68 || s == 75 || s == 79) {
                it.remove();
            }
        }
        System.out.println("Floor(4): " + sl.floor(4));
        System.out.println("Ceiling(4): " + sl.ceiling(4));
        System.out.println("Skiplist Size: " + sl.size());
        System.out.println("Skiplist Maxlevel: " + sl.maxLevel);
        sl.printSortedList();
        System.out.println();
        System.out.println("Skiplist rebuilt");
        sl.rebuild();
        sl.printSortedList();
        System.out.println();
        System.out.println("Skiplist Maxlevel: " + sl.maxLevel);
        System.out.println("First element at Maxlevel: " + sl.head.next[sl.maxLevel - 1].getElement());
    }
}
