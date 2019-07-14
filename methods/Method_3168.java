/** 
 * ??????????
 * @param reader          ?reader????
 * @param size            ?????????
 * @param newWordsOnly    ?????????????
 * @param max_word_len    ??????
 * @param min_freq        ??????
 * @param min_entropy     ?????
 * @param min_aggregation ???????
 * @return ??????
 */
public static List<WordInfo> extractWords(BufferedReader reader,int size,boolean newWordsOnly,int max_word_len,float min_freq,float min_entropy,float min_aggregation) throws IOException {
  NewWordDiscover discover=new NewWordDiscover(max_word_len,min_freq,min_entropy,min_aggregation,newWordsOnly);
  return discover.discover(reader,size);
}