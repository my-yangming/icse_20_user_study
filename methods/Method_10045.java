/** 
 * Increments a field of the specified liveness.
 * @param userId the specified user id
 * @param field  the specified field
 */
@Transactional public void incLiveness(final String userId,final String field){
  Stopwatchs.start("Inc liveness");
  final String date=DateFormatUtils.format(System.currentTimeMillis(),"yyyyMMdd");
  try {
    JSONObject liveness=livenessRepository.getByUserAndDate(userId,date);
    if (null == liveness) {
      liveness=new JSONObject();
      liveness.put(Liveness.LIVENESS_USER_ID,userId);
      liveness.put(Liveness.LIVENESS_DATE,date);
      liveness.put(Liveness.LIVENESS_POINT,0);
      liveness.put(Liveness.LIVENESS_ACTIVITY,0);
      liveness.put(Liveness.LIVENESS_ARTICLE,0);
      liveness.put(Liveness.LIVENESS_COMMENT,0);
      liveness.put(Liveness.LIVENESS_PV,0);
      liveness.put(Liveness.LIVENESS_REWARD,0);
      liveness.put(Liveness.LIVENESS_THANK,0);
      liveness.put(Liveness.LIVENESS_VOTE,0);
      liveness.put(Liveness.LIVENESS_VOTE,0);
      liveness.put(Liveness.LIVENESS_ACCEPT_ANSWER,0);
      livenessRepository.add(liveness);
    }
    liveness.put(field,liveness.optInt(field) + 1);
    livenessRepository.update(liveness.optString(Keys.OBJECT_ID),liveness);
  }
 catch (  final RepositoryException e) {
    LOGGER.log(Level.ERROR,"Updates a liveness [" + date + "] field [" + field + "] failed",e);
  }
 finally {
    Stopwatchs.end();
  }
}
