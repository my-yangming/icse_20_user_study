@Override public void onItemsAdded(RecyclerView recyclerView,int positionStart,int itemCount){
  mSpanSizeLookup.invalidateSpanIndexCache();
  mSpanSizeLookup.invalidateSpanGroupIndexCache();
}
