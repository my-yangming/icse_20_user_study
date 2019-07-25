package io.lacuna.bifurcan.utils;

import java.util.Arrays;

/**
 * A series of utility functions that use a bit-vector to store a sorted set of integers with bit-lengths within [1,64].
 *
 * @author ztellman
 */
public class BitIntSet {

  /**
   * @return a bit-int set, with an implied size of 0.
   */
  public static long[] create() {
    return BitVector.create(0);
  }

  /**
   * @param set            the bit-int set
   * @param bitsPerElement the bits per element
   * @param idx            the table
   * @return the rowValue stored at the given table
   */
  public static long get(long[] set, int bitsPerElement, int idx) {
    return BitVector.get(set, idx * bitsPerElement, bitsPerElement);
  }

  /**
   * Performs a binary search for the given rowValue.
   *
   * @param set            the bit-int set
   * @param bitsPerElement the bits per element
   * @param size           the number of elements in the set
   * @param val            the rowValue to search for
   * @return If idx >= 0, the actual table of the rowValue.  Otherwise, the return rowValue represents the table where the
   * rowValue would be inserted, where -1 represents the 0th element, -2 represents the 1st element, and so on.
   */
  public static int indexOf(long[] set, int bitsPerElement, int size, long val) {
    int low = 0;
    int high = size - 1;
    int mid = 0;

    while (low <= high) {
      mid = (low + high) >>> 1;
      long curr = get(set, bitsPerElement, mid);

      if (curr < val) {
        low = mid + 1;
      } else if (curr > val) {
        high = mid - 1;
      } else {
        return mid;
      }
    }

    return -(low + 1);
  }

  /**
   * @param set            the bit-int set
   * @param bitsPerElement the bits per element
   * @param size           the number of elements in the set
   * @param val            the rowValue to add
   * @return an updated long[] array if the rowValue is not already in the set, otherwise 'set' is returned unchanged
   */
  public static long[] add(long[] set, int bitsPerElement, int size, long val) {
    int idx = indexOf(set, bitsPerElement, size, val);
    if (idx < 0) {
      idx = -idx - 1;
      return BitVector.insert(set, (bitsPerElement * size), val, (bitsPerElement * idx), bitsPerElement);
    } else {
      return set;
    }
  }

  /**
   * @param set            the bit-int set
   * @param bitsPerElement the bits per element
   * @param size           the number of elements in the set
   * @param val            the rowValue to remove
   * @return an updated long[] array if the rowValue was in the set, otherwise 'set' is returned unchanged
   */
  public static long[] remove(long[] set, int bitsPerElement, int size, long val) {
    int idx = indexOf(set, bitsPerElement, size, val);
    if (idx < 0) {
      return set;
    } else {
      return BitVector.remove(set, (bitsPerElement * size), (bitsPerElement * idx), bitsPerElement);
    }
  }
}
