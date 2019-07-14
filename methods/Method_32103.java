/** 
 * Returns a copy of this datetime with the specified duration added. <p> If the addition is zero, then <code>this</code> is returned.
 * @param durationToAdd  the duration to add to this one, null means zero
 * @param scalar  the amount of times to add, such as -1 to subtract once
 * @return a copy of this datetime with the duration added
 * @throws ArithmeticException if the new datetime exceeds the capacity of a long
 */
public DateTime withDurationAdded(ReadableDuration durationToAdd,int scalar){
  if (durationToAdd == null || scalar == 0) {
    return this;
  }
  return withDurationAdded(durationToAdd.getMillis(),scalar);
}