/** 
 * Returns a copy of this date with the partial set of fields replacing those from this instance. <p> For example, if the partial is a <code>LocalDate</code> then the date fields would be changed in the returned instance. If the partial is null, then <code>this</code> is returned.
 * @param partial  the partial set of fields to apply to this datetime, null ignored
 * @return a copy of this datetime with a different set of fields
 * @throws IllegalArgumentException if any value is invalid
 */
public DateMidnight withFields(ReadablePartial partial){
  if (partial == null) {
    return this;
  }
  return withMillis(getChronology().set(partial,getMillis()));
}
