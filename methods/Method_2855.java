/** 
 * ???????,?????????,??????????
 * @param name ?????
 * @return Enum??
 */
public static final Nature create(String name){
  Nature nature=fromString(name);
  if (nature == null)   return new Nature(name);
  return nature;
}