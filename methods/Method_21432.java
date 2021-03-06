public Map<String,Object> getStatData(){
  final int activeCount;
  final int activePeak;
  final Date activePeakTime;
  final int poolingCount;
  final int poolingPeak;
  final Date poolingPeakTime;
  final long connectCount;
  final long closeCount;
  lock.lock();
  try {
    poolingCount=this.poolingCount;
    poolingPeak=this.poolingPeak;
    poolingPeakTime=this.getPoolingPeakTime();
    activeCount=this.activeCount;
    activePeak=this.activePeak;
    activePeakTime=this.getActivePeakTime();
    connectCount=this.connectCount;
    closeCount=this.closeCount;
  }
  finally {
    lock.unlock();
  }
  Map<String,Object> dataMap=new LinkedHashMap<String,Object>();
  dataMap.put("Identity",System.identityHashCode(this));
  dataMap.put("Name",this.getName());
  dataMap.put("DbType",this.dbType);
  dataMap.put("DriverClassName",this.getDriverClassName());
  dataMap.put("URL",this.getUrl());
  dataMap.put("UserName",this.getUsername());
  dataMap.put("FilterClassNames",this.getFilterClassNames());
  dataMap.put("WaitThreadCount",this.getWaitThreadCount());
  dataMap.put("NotEmptyWaitCount",this.getNotEmptyWaitCount());
  dataMap.put("NotEmptyWaitMillis",this.getNotEmptyWaitMillis());
  dataMap.put("PoolingCount",poolingCount);
  dataMap.put("PoolingPeak",poolingPeak);
  dataMap.put("PoolingPeakTime",poolingPeakTime);
  dataMap.put("ActiveCount",activeCount);
  dataMap.put("ActivePeak",activePeak);
  dataMap.put("ActivePeakTime",activePeakTime);
  dataMap.put("InitialSize",this.getInitialSize());
  dataMap.put("MinIdle",this.getMinIdle());
  dataMap.put("MaxActive",this.getMaxActive());
  dataMap.put("QueryTimeout",this.getQueryTimeout());
  dataMap.put("TransactionQueryTimeout",this.getTransactionQueryTimeout());
  dataMap.put("LoginTimeout",this.getLoginTimeout());
  dataMap.put("ValidConnectionCheckerClassName",this.getValidConnectionCheckerClassName());
  dataMap.put("ExceptionSorterClassName",this.getExceptionSorterClassName());
  dataMap.put("TestOnBorrow",this.isTestOnBorrow());
  dataMap.put("TestOnReturn",this.isTestOnReturn());
  dataMap.put("TestWhileIdle",this.isTestWhileIdle());
  dataMap.put("DefaultAutoCommit",this.isDefaultAutoCommit());
  dataMap.put("DefaultReadOnly",this.getDefaultReadOnly());
  dataMap.put("DefaultTransactionIsolation",this.getDefaultTransactionIsolation());
  dataMap.put("LogicConnectCount",connectCount);
  dataMap.put("LogicCloseCount",closeCount);
  dataMap.put("LogicConnectErrorCount",this.getConnectErrorCount());
  dataMap.put("PhysicalConnectCount",this.getCreateCount());
  dataMap.put("PhysicalCloseCount",this.getDestroyCount());
  dataMap.put("PhysicalConnectErrorCount",this.getCreateErrorCount());
  dataMap.put("ExecuteCount",this.getExecuteCount());
  dataMap.put("ExecuteUpdateCount",this.getExecuteUpdateCount());
  dataMap.put("ExecuteQueryCount",this.getExecuteQueryCount());
  dataMap.put("ExecuteBatchCount",this.getExecuteBatchCount());
  dataMap.put("ErrorCount",this.getErrorCount());
  dataMap.put("CommitCount",this.getCommitCount());
  dataMap.put("RollbackCount",this.getRollbackCount());
  dataMap.put("PSCacheAccessCount",this.getCachedPreparedStatementAccessCount());
  dataMap.put("PSCacheHitCount",this.getCachedPreparedStatementHitCount());
  dataMap.put("PSCacheMissCount",this.getCachedPreparedStatementMissCount());
  dataMap.put("StartTransactionCount",this.getStartTransactionCount());
  dataMap.put("TransactionHistogram",this.getTransactionHistogramValues());
  dataMap.put("ConnectionHoldTimeHistogram",this.getDataSourceStat().getConnectionHoldHistogram().toArray());
  dataMap.put("RemoveAbandoned",this.isRemoveAbandoned());
  dataMap.put("ClobOpenCount",this.getDataSourceStat().getClobOpenCount());
  dataMap.put("BlobOpenCount",this.getDataSourceStat().getBlobOpenCount());
  dataMap.put("KeepAliveCheckCount",this.getDataSourceStat().getKeepAliveCheckCount());
  dataMap.put("KeepAlive",this.isKeepAlive());
  dataMap.put("FailFast",this.isFailFast());
  dataMap.put("MaxWait",this.getMaxWait());
  dataMap.put("MaxWaitThreadCount",this.getMaxWaitThreadCount());
  dataMap.put("PoolPreparedStatements",this.isPoolPreparedStatements());
  dataMap.put("MaxPoolPreparedStatementPerConnectionSize",this.getMaxPoolPreparedStatementPerConnectionSize());
  dataMap.put("MinEvictableIdleTimeMillis",this.minEvictableIdleTimeMillis);
  dataMap.put("MaxEvictableIdleTimeMillis",this.maxEvictableIdleTimeMillis);
  dataMap.put("LogDifferentThread",isLogDifferentThread());
  dataMap.put("RecycleErrorCount",getRecycleErrorCount());
  dataMap.put("PreparedStatementOpenCount",getPreparedStatementCount());
  dataMap.put("PreparedStatementClosedCount",getClosedPreparedStatementCount());
  dataMap.put("UseUnfairLock",isUseUnfairLock());
  dataMap.put("InitGlobalVariants",isInitGlobalVariants());
  dataMap.put("InitVariants",isInitVariants());
  return dataMap;
}
