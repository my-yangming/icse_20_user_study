public static String timeFormat(String time){
  SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
  Date date=null;
  try {
    date=sdf.parse(time);
  }
 catch (  Exception e) {
    DebugUtil.debug("--????-->","??");
    return time;
  }
  SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd HH:mm");
  return sdf1.format(date);
}
