/** 
 * Retry this Perhaps at most the given number of times if it fails.
 * @param times the number of times, Long.MAX_VALUE means indefinitely
 * @return the new Perhaps instance
 */
public final Perhaps<T> retry(long times){
  if (times < 0L) {
    throw new IllegalArgumentException("times >= 0 required but it was " + times);
  }
  return onAssembly(new PerhapsRetry<T>(this,times));
}
