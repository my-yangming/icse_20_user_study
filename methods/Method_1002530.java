public BatchGetRequestBuilder<K,V> ids(Collection<K> ids){
  addKeys(ids);
  return this;
}
