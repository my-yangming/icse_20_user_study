/** 
 * Transfers point from the specified from id to the specified to id with type, sum, data id and time.
 * @param fromId the specified from id, may be system "sys"
 * @param toId   the specified to id, may be system "sys"
 * @param type   the specified type
 * @param sum    the specified sum
 * @param dataId the specified data id
 * @param time   the specified time
 * @param memo   the specified memo
 * @return transfer record id, returns {@code null} if transfer failed
 */
public synchronized String transfer(final String fromId,final String toId,final int type,final int sum,final String dataId,final long time,final String memo){
  if (StringUtils.equals(fromId,toId)) {
    LOGGER.log(Level.WARN,"The from id is equal to the to id [" + fromId + "]");
    return null;
  }
  final Transaction transaction=pointtransferRepository.beginTransaction();
  try {
    int fromBalance=0;
    if (!Pointtransfer.ID_C_SYS.equals(fromId)) {
      final JSONObject fromUser=userRepository.get(fromId);
      fromBalance=fromUser.optInt(UserExt.USER_POINT) - sum;
      if (fromBalance < 0) {
        throw new Exception("Insufficient balance");
      }
      fromUser.put(UserExt.USER_POINT,fromBalance);
      fromUser.put(UserExt.USER_USED_POINT,fromUser.optInt(UserExt.USER_USED_POINT) + sum);
      userRepository.update(fromId,fromUser,UserExt.USER_POINT,UserExt.USER_USED_POINT);
    }
    int toBalance=0;
    if (!Pointtransfer.ID_C_SYS.equals(toId)) {
      final JSONObject toUser=userRepository.get(toId);
      toBalance=toUser.optInt(UserExt.USER_POINT) + sum;
      toUser.put(UserExt.USER_POINT,toBalance);
      userRepository.update(toId,toUser,UserExt.USER_POINT);
    }
    final JSONObject pointtransfer=new JSONObject();
    pointtransfer.put(Pointtransfer.FROM_ID,fromId);
    pointtransfer.put(Pointtransfer.TO_ID,toId);
    pointtransfer.put(Pointtransfer.SUM,sum);
    pointtransfer.put(Pointtransfer.FROM_BALANCE,fromBalance);
    pointtransfer.put(Pointtransfer.TO_BALANCE,toBalance);
    pointtransfer.put(Pointtransfer.TIME,time);
    pointtransfer.put(Pointtransfer.TYPE,type);
    pointtransfer.put(Pointtransfer.DATA_ID,dataId);
    pointtransfer.put(Pointtransfer.MEMO,memo);
    final String ret=pointtransferRepository.add(pointtransfer);
    transaction.commit();
    return ret;
  }
 catch (  final Exception e) {
    if (transaction.isActive()) {
      transaction.rollback();
    }
    LOGGER.log(Level.ERROR,"Transfer [fromId=" + fromId + ", toId=" + toId + ", sum=" + sum + ", type=" + type + ", dataId=" + dataId + ", memo=" + memo + "] error",e);
    return null;
  }
}
