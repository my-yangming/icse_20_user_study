/** 
 * ?????????
 * @param context RPC???
 * @param request ????
 */
protected void beforeSend(RpcInternalContext context,SofaRequest request){
  currentRequests.incrementAndGet();
  context.getStopWatch().tick().read();
  context.setLocalAddress(localAddress());
  if (EventBus.isEnable(ClientBeforeSendEvent.class)) {
    EventBus.post(new ClientBeforeSendEvent(request));
  }
}
