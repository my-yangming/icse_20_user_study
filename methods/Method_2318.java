/** 
 * ????????????????????
 * @param taskParameter ????????
 * @param ownSign ??????
 * @param taskItemNum ?????????????
 * @param taskItemList ?????????????????
 * @param eachFetchDataNum ?????????
 * @return
 * @throws Exception
 */
@Override public List<Map> selectTasks(String taskParameter,String ownSign,int taskItemNum,List<TaskItemDefine> taskItemList,int eachFetchDataNum) throws Exception {
  List<Map> allDrawList=new ArrayList<>();
  Map map=new HashMap(1);
  map.put("ID",System.currentTimeMillis());
  allDrawList.add(map);
  return allDrawList;
}
