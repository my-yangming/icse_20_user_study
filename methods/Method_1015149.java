default boolean equals(IEntry<K,V> o,BiPredicate<K,K> keyEquals,BiPredicate<V,V> valEquals){
  return keyEquals.test(key(),o.key()) && valEquals.test(value(),o.value());
}
