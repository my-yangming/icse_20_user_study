/** 
 * ???????? ???????????????
 */
@Bean("timebuskerd") public Queue timebusker(){
  return new Queue(DIRECT_ROUTING_KEY_TIMEBUSKER);
}
