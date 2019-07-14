/** 
 * Whether the existing scheduling data (with same identifiers) will be  overwritten.  If false, and <code>IgnoreDuplicates</code> is not false, and jobs or  triggers with the same names already exist as those in the file, an  error will occur.
 * @see #isIgnoreDuplicates()
 */
public boolean isOverWriteExistingData(){
  return overWriteExistingData;
}
