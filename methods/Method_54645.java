/** 
 * Unsafe version of  {@link #m_objectUniqueId(int) m_objectUniqueId}. 
 */
public static void nm_objectUniqueId(long struct,int value){
  UNSAFE.putInt(null,struct + B3OverlappingObject.M_OBJECTUNIQUEID,value);
}
