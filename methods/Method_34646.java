public static HystrixThreadPoolConfiguration sample(HystrixThreadPoolKey threadPoolKey,HystrixThreadPoolProperties threadPoolProperties){
  return new HystrixThreadPoolConfiguration(threadPoolKey,threadPoolProperties);
}
