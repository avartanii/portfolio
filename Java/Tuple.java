/**
 * This class represents a tuple of integers, indexed from zero.
 */
public class Tuple {

    /**
     * Singleton constant representing an impossible tuple.
     */
    public static final Tuple IMPOSSIBLE = new Tuple(new int[0], false);

    private boolean possible;

    private int[] data;

    /**
     * Constructs a tuple of size n with all integers initialized to zero.
     *
     * @param n
     *            the number of elements in this tuple
     */
    public Tuple(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }

        data = new int[n];
        for (int i = 0; i < n; i++) {
            data[i] = 0;
        }
        possible = true;
    }

    /**
     * Constructs a tuple of size n from the given data, where n = data.length.
     *
     * @param data
     *            the ints that should populate the tuple
     */
    public Tuple(int[] data) {
        if (data == null) {
            throw new IllegalArgumentException();
        }

        this.data = new int[data.length];
        for (int i = 0; i < data.length; i++) {
            this.data[i] = data[i];
        }
        possible = true;
    }

    public Tuple(int[] data, boolean possible) {
        this(data);
        this.possible = possible;
    }

    /**
     * Returns whether the tuple is impossible.
     *
     * @return whether the tuple is impossible
     */
    public boolean isImpossible() {
        return !possible;
    }

    /**
     * Sets whether the tuple is possible.
     *
     * @param possible
     *            a boolean representation of whether this Tally is possible
     */
    public void setPossible(boolean possible) {
        this.possible = possible;
    }

    /**
     * Sets the int at position i to value j.
     *
     * @param i
     *            the position of the int to set
     * @param j
     *            the value to which the position should be set
     */
    public void setElement(int i, int j) {
        checkIndex(i);
        data[i] = j;
    }

    /**
     * Returns the int at position i.
     *
     * @param i
     *            the position whose int is requested
     * @return the int at position i
     */
    public int getElement(int i) {
        checkIndex(i);
        return data[i];
    }

    public int[] getElements() {
        return this.data;
    }

    /**
     * Returns the length (i.e., the number of elements) of this tuple.
     *
     * @return the length of this tuple
     */
    public int length() {
        return data.length;
    }

    /**
     * Returns the total of the elements in this tuple. For example, the
     * 3-element tuple (9, 3, 2) has a total of 14.
     *
     * @return the total of the elements of this tuple
     */
    public int total() {
        int sum = 0;
        for (int i = 0; i < length(); i++) {
            sum = sum + getElement(i);
        }
        return sum;
    }

    /**
     * Adds tuple t to this tuple, returning the "sum" as a new tuple.
     *
     * @param t
     *            the tuple to add to this tuple
     * @return the element-wise sum of this and t
     */
    public Tuple add(Tuple t) {

        if (length() != t.length()) {
            throw new IllegalArgumentException();
        }

        Tuple sum = new Tuple(length());
        for (int i = 0; i < length(); i++) {
            sum.setElement(i, getElement(i) + t.getElement(i));
        }

        return sum;
    }

    /**
     * Subtracts tuple t from this tuple, returning the "difference" as a new tuple.
     *
     * @param t
     *            the tuple to subtract from this tuple
     * @return the element-wise difference of this and t
     */
    public Tuple subtract(Tuple t) {

        if (length() != t.length()) {
            throw new IllegalArgumentException();
        }

        Tuple difference = new Tuple(length());
        for (int i = 0; i < length(); i++) {
            difference.setElement(i, getElement(i) - t.getElement(i));
        }

        return difference;
    }

    /**
     * Returns true iff this tuple is value-identical to t.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object t) {
        if ((t == null) || !(t instanceof Tuple) || (length() != ((Tuple)t).length())) {
            return false;
        }

        if(this.isImpossible() != ((Tuple)t).isImpossible()) {
            return false;
        }

        for (int i = 0; i < length(); i++) {
            if (getElement(i) != ((Tuple)t).getElement(i)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns the product of this tuple's elements as its hash code.
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int product = 0;
        for (int i = 0; i < length(); i++) {
            product = product * getElement(i);
        }

        return (product >= 0) ? product : -product;
    }

    /**
     * Returns a string representation of this tuple.
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        if (isImpossible()) {
            return "Impossible tuple";
        }

        String result = "<";
        for (int i = 0; i < length(); i++) {
            result += (i > 0 ? "," : "") + data[i];
        }
        return result + ">";
    }

    private void checkIndex(int i) {
        if (i < 0) {
            throw new IllegalArgumentException();
        }

        if (i >= length()) {
            throw new IllegalArgumentException();
        }
    }

}