@Override public void onItemsUpdated(RecyclerView recyclerView,int positionStart,int itemCount,Object payload){
  mSpanSizeLookup.invalidateSpanIndexCache();
  mSpanSizeLookup.invalidateSpanGroupIndexCache();
}
