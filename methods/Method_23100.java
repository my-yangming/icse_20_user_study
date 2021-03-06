/** 
 * Writes a <code>BCD2</code> to the underlying output stream.
 * @param v   an <code>int</code> to be written.
 * @exception IOException  if an I/O error occurs.
 * @see java.io.FilterOutputStream#out
 */
public void writeBCD2(int v) throws IOException {
  out.write(((v % 100 / 10) << 4) | (v % 10));
  incCount(1);
}
