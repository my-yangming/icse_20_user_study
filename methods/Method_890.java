@Override public void addViolation(Object data,Node node,String arg){
  ViolationUtils.addViolationWithPrecisePosition(this,node,data,I18nResources.getMessage("java.exception.AvoidReturnInFinallyRule.violation.msg"));
}
