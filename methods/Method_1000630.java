/** 
 * ????????????????????????????????? <p> ???
 * @param seg ????
 * @param context ?????
 * @return ???????
 */
public static String replace(Segment seg,Context context){
  if (null == seg)   return null;
  for (  String key : seg.keys())   if (!context.has(key))   context.set(key,"${" + key + "}");
  return seg.render(context).toString();
}
