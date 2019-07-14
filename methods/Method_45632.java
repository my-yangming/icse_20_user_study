/** 
 * ?????????????????RPC ?????????????  {@link Logger} ??????? Exception ???????????????
 * @param code ???
 * @return ????
 */
public static String getLog(String code){
  if (!LOG_CODES.containsKey(code)) {
    throw new LogCodeNotFoundException(code);
  }
  try {
    return String.format(LOG,code,LOG_CODES.get(code),LogCodes.NOTE);
  }
 catch (  Throwable e) {
    throw new LogFormatException(code);
  }
}