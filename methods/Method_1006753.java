/** 
 * ??????10s????????
 * @param date
 */
private void clean(Date date){
  long difference=0;
  boolean delete;
  if (deque.size() == 0) {
    return;
  }
  delete=false;
  do {
    Event e=deque.getLast();
    difference=date.getTime() - e.getDate().getTime();
    if (difference > 10000) {
      System.out.printf("Cleaner: %s \n",e.getEvent());
      deque.removeLast();
      delete=true;
    }
  }
 while (difference > 10000);
  if (delete) {
    System.out.printf("Cleaner: Size of the queue: %d\n",deque.size());
  }
}
