/** 
 * Unsafe version of  {@link #m_objectUniqueId}. 
 */
public static int nm_objectUniqueId(long struct){
  return UNSAFE.getInt(null,struct + B3VisualShapeData.M_OBJECTUNIQUEID);
}
