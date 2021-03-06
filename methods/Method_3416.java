/** 
 * ????
 * @param sentence ??????????????????2014?????
 * @return ?????????????????????
 */
public boolean learn(Sentence sentence){
  CharTable.normalize(sentence);
  if (!getPerceptronSegmenter().learn(sentence))   return false;
  if (posTagger != null && !getPerceptronPOSTagger().learn(sentence))   return false;
  if (neRecognizer != null && !getPerceptionNERecognizer().learn(sentence))   return false;
  return true;
}
