/** 
 * ????????????????????
 * @param decimals ?????0-9?????????1.23?233.30
 * @return ??????true???????false
 */
public static boolean checkDecimals(String decimals){
  String regex="\\-?[1-9]\\d+(\\.\\d+)?";
  return Pattern.matches(regex,decimals);
}
