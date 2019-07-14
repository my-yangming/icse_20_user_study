/** 
 * ????
 * @param email
 * @return
 */
public static boolean checkEmail(String email){
  boolean flag=false;
  try {
    String check="^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    Pattern regex=Pattern.compile(check);
    Matcher matcher=regex.matcher(email);
    flag=matcher.matches();
  }
 catch (  Exception e) {
    flag=false;
  }
  return flag;
}
