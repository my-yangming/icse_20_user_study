synchronized Optional<ApolloPrefetch.Callback> terminate(){
switch (state.get()) {
case ACTIVE:
    tracker.unregisterPrefetchCall(this);
  state.set(TERMINATED);
return Optional.fromNullable(originalCallback.getAndSet(null));
case CANCELED:
return Optional.fromNullable(originalCallback.getAndSet(null));
case IDLE:
case TERMINATED:
throw new IllegalStateException(CallState.IllegalStateMessage.forCurrentState(state.get()).expected(ACTIVE,CANCELED));
default :
throw new IllegalStateException("Unknown state");
}
}
