public static Object convertRealResult(Object result){
  if (result instanceof ResponseEntity) {
    result=((ResponseEntity)result).getBody();
  }
  if (result instanceof ResponseMessage) {
    result=((ResponseMessage)result).getResult();
  }
  if (result instanceof PagerResult) {
    result=((PagerResult)result).getData();
  }
  return result;
}
