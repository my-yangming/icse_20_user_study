/** 
 * Class[]?String[] <br> ??????String????????Class.forName????getClasses(String[])????
 * @param types Class[]
 * @return ????
 * @see #getClasses(String[]) 
 */
public static String[] getTypeStrs(Class[] types){
  return getTypeStrs(types,false);
}