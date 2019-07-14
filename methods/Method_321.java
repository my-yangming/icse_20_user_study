/** 
 * hash ??2
 * @param data
 * @return
 */
private int hashcode_2(String data){
  final int p=16777619;
  int hash=(int)2166136261L;
  for (int i=0; i < data.length(); i++) {
    hash=(hash ^ data.charAt(i)) * p;
  }
  hash+=hash << 13;
  hash^=hash >> 7;
  hash+=hash << 3;
  hash^=hash >> 17;
  hash+=hash << 5;
  return Math.abs(hash);
}
