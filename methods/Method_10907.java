/** 
 * ?????????
 * @param birthday ??????1992-09-03??1992.09.03
 * @return ??????true???????false
 */
public static boolean checkBirthday(String birthday){
  String regex="[1-9]{4}([-./])\\d{1,2}\\1\\d{1,2}";
  return Pattern.matches(regex,birthday);
}