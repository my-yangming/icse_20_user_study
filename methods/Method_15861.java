/** 
 * ?????bean,???????,??? {@link org.hswebframework.web.validate.ValidationException}
 * @param group ????
 * @param < T >   ??????
 * @return ????
 */
default <T extends ValidateBean>T tryValidate(Class... group){
  BeanValidator.tryValidate(this,group);
  return (T)this;
}