/** 
 * ???????edge[i][j]??id?i????j?????????????????null
 * @return
 */
public String[][] getEdgeArray(){
  String[][] edge=new String[word.length + 1][word.length + 1];
  for (  CoNLLWord coNLLWord : word) {
    edge[coNLLWord.ID][coNLLWord.HEAD.ID]=coNLLWord.DEPREL;
  }
  return edge;
}
