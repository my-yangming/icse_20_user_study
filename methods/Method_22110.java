/** 
 * ?????????????????????.
 * @param node ??????
 * @return ???????
 */
public String getJobNodeDataDirectly(final String node){
  return regCenter.getDirectly(jobNodePath.getFullPath(node));
}
