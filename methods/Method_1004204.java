@Override public DataAccess<T> copy(){
  return new ByteableDataAccess<>(tClass());
}
