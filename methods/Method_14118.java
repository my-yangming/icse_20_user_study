/** 
 * Tests if character is a vowel
 * @param inChar character to be tested in string to be encoded
 * @return true if character is a vowel, false if not
 */
boolean IsVowel(char inChar){
  if ((inChar == 'A') || (inChar == 'E') || (inChar == 'I') || (inChar == 'O') || (inChar == 'U') || (inChar == 'Y') || (inChar == '�') || (inChar == '�') || (inChar == '�') || (inChar == '�') || (inChar == '�') || (inChar == '�') || (inChar == '�') || (inChar == '�') || (inChar == '�') || (inChar == '�') || (inChar == '�') || (inChar == '�') || (inChar == '�') || (inChar == '�') || (inChar == '�') || (inChar == '�') || (inChar == '�') || (inChar == '�') || (inChar == '�') || (inChar == '�') || (inChar == '?') || (inChar == '�') || (inChar == '�') || (inChar == '�') || (inChar == '�') || (inChar == '�') || (inChar == '�') || (inChar == '?')) {
    return true;
  }
  return false;
}
