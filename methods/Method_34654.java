public ExecutionResult setInvocationStartTime(long startTimestamp){
  return new ExecutionResult(eventCounts,startTimestamp,executionLatency,userThreadLatency,failedExecutionException,executionException,executionOccurred,isExecutedInThread,collapserKey);
}