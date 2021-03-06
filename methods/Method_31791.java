/** 
 * Output the instant using the specified format pattern.
 * @param pattern  the pattern specification, null means use <code>toString</code>
 * @return the formatted string, not null
 * @see org.joda.time.format.DateTimeFormat
 */
public String toString(String pattern){
  if (pattern == null) {
    return toString();
  }
  return DateTimeFormat.forPattern(pattern).print(this);
}
