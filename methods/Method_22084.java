/** 
 * ???????????????.
 * @param items ?????????????????
 * @return ?????????????
 */
public List<Integer> getMisfiredJobItems(final Collection<Integer> items){
  List<Integer> result=new ArrayList<>(items.size());
  for (  int each : items) {
    if (jobNodeStorage.isJobNodeExisted(ShardingNode.getMisfireNode(each))) {
      result.add(each);
    }
  }
  return result;
}
