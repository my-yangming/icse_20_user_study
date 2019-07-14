/** 
 * <p> Returns a date that is rounded to the next even minute above the given date. </p> <p> For example an input date with a time of 08:13:54 would result in a date with the time of 08:14:00. If the date's time is in the 59th minute, then the hour (and possibly the day) will be promoted. </p>
 * @param date the Date to round, if <code>null</code> the current time will be used
 * @return the new rounded date
 */
public static Date evenMinuteDate(Date date){
  if (date == null) {
    date=new Date();
  }
  Calendar c=Calendar.getInstance();
  c.setTime(date);
  c.setLenient(true);
  c.set(Calendar.MINUTE,c.get(Calendar.MINUTE) + 1);
  c.set(Calendar.SECOND,0);
  c.set(Calendar.MILLISECOND,0);
  return c.getTime();
}
